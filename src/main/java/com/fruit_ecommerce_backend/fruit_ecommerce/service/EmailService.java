//package com.fruit_ecommerce_backend.fruit_ecommerce.service;
//
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class EmailService {
//
//    private final JavaMailSender mailSender;
//
//    public void sendOtpEmail(String to, String otp) {
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setTo(to);
//        msg.setSubject("Your Login OTP Verification");
//        msg.setText("Your OTP is: " + otp + "\nIt will expire in 5 minutes.");
//        mailSender.send(msg);
//    }
//    
//    public void sendSimple(String to, String subject, String body) {
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setTo(to);
//        msg.setSubject(subject);
//        msg.setText(body);
//        mailSender.send(msg);
//    }
//    
//    //
//}
