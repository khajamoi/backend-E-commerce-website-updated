package com.fruit_ecommerce_backend.fruit_ecommerce.service;

import com.fruit_ecommerce_backend.fruit_ecommerce.dto.PaymentInfo;
import com.fruit_ecommerce_backend.fruit_ecommerce.dto.PaymentItem;
import com.fruit_ecommerce_backend.fruit_ecommerce.entity.*;
import com.fruit_ecommerce_backend.fruit_ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Base64;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final UserPaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Value("${app.payment.merchantVpa:merchant@upi}")
    private String merchantVpa;

    // --- Helpers ---
    private String tokenizeCard(String cardNumber) {
        String salt = UUID.randomUUID().toString();
        return DigestUtils.sha256Hex(cardNumber + salt);
    }

    private String last4(String cardNumber) {
        if (cardNumber == null) return null;
        String digits = cardNumber.replaceAll("\\D", "");
        return digits.length() <= 4 ? digits : digits.substring(digits.length() - 4);
    }

    private String generateQrBase64(String text, int w, int h) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, w, h);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }

    private String encode(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    // --- Create order ---
    public OrderEntity createOrderFromItems(List<PaymentItem> itemsDto, double total, Long userId) {
        List<OrderItem> items = (itemsDto == null ? List.of() :
            itemsDto.stream().map(pi -> {
                if (pi.getPrice() == null) pi.setPrice(0.0);
                Product product = new Product();
                product.setId(pi.getProductId());
                return OrderItem.builder()
                        .product(product)
                        .quantity(pi.getQty())
                        .price(pi.getPrice())
                        .build();
            }).collect(Collectors.toList()));

        OrderEntity order = OrderEntity.builder()
                .items(items)
                .total(total)
                .status("PENDING")
                .createdAt(Instant.now())
                .build();

        if (userId != null) userRepository.findById(userId).ifPresent(order::setUser);

        return orderRepository.save(order);
    }

    // --- Card Payment ---
    public UserPayment processCardPayment(OrderEntity order, PaymentInfo info, User user) throws Exception {
        UserPayment p = UserPayment.builder()
                .user(user)
                .order(order)
                .amount(order.getTotal())
                .cardHolder(info.getCardHolder())
                .cardLast4(last4(info.getCardNumber()))
                .expiryMasked(info.getExpiry())
                .cardToken(tokenizeCard(info.getCardNumber()))
                .createdAt(Instant.now())
                .build();

        boolean ok = info.getCardNumber().replaceAll("\\D", "").length() == 16 && info.getCvv().length() >= 3;
        p.setStatus(ok ? "SUCCESS" : "FAILED");

        String ref = "CARD-" + UUID.randomUUID();
        String qrData = "ref:" + ref + "|order:" + order.getId() + "|amt:" + order.getTotal();
        p.setBarcodeBase64(generateQrBase64(qrData, 300, 300));

        return paymentRepository.save(p);
    }

    // --- UPI Payment ---
    public UserPayment createUpiPayment(OrderEntity order, User user) throws Exception {
        String upiUri = "upi://pay?pa=" + merchantVpa +
                "&pn=" + encode("Fruit Shop") +
                "&am=" + encode(String.format(Locale.US, "%.2f", order.getTotal())) +
                "&cu=INR&tn=" + encode("Order#" + order.getId());

        UserPayment p = UserPayment.builder()
                .user(user)
                .order(order)
                .amount(order.getTotal())
                .status("UPI_PENDING")
                .createdAt(Instant.now())
                .barcodeBase64(generateQrBase64(upiUri, 400, 400))
                .build();

        return paymentRepository.save(p);
    }

    // --- COD Payment ---
    public UserPayment createCodPayment(OrderEntity order, User user) throws Exception {
        String ref = "COD-" + UUID.randomUUID();
        String qrData = "COD_REF:" + ref + "|order:" + order.getId() + "|amt:" + order.getTotal();

        UserPayment p = UserPayment.builder()
                .user(user)
                .order(order)
                .amount(order.getTotal())
                .status("COD_PENDING")
                .createdAt(Instant.now())
                .barcodeBase64(generateQrBase64(qrData, 300, 300))
                .build();

        return paymentRepository.save(p);
    }
}
