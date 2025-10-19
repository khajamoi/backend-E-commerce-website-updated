package com.fruit_ecommerce_backend.fruit_ecommerce.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();

    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(email, new OtpData(otp, LocalDateTime.now().plusMinutes(5)));
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        OtpData data = otpStore.get(email);
        if (data == null || LocalDateTime.now().isAfter(data.expiry)) {
            otpStore.remove(email);
            return false;
        }
        boolean valid = data.otp.equals(otp);
        if (valid) otpStore.remove(email);
        return valid;
    }

    private static class OtpData {
        String otp;
        LocalDateTime expiry;
        OtpData(String otp, LocalDateTime expiry) {
            this.otp = otp;
            this.expiry = expiry;
        }
    }
}
