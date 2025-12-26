package com.security.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.entity.PasswordResetToken;
import com.security.entity.User;
import com.security.repository.PasswordResetTokenRepository;
import com.security.repository.UserRepository;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordResetTokenRepository tokenRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void sendResetLink(String email) {
        User user = userRepo.findByEmail(email);
//                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken =
                new PasswordResetToken(token, user);

        tokenRepo.save(resetToken);

        String link = "http://localhost:4200/reset-password?token=" + token;
        emailService.sendResetEmail(user.getEmail(), link);
        System.out.println(link);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken =
                tokenRepo.findByToken(token)
                        .orElseThrow(() -> new RuntimeException("Invalid Token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
        	System.out.println("token expired");
            throw new RuntimeException("Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        System.out.println("Reset successful");
        tokenRepo.delete(resetToken);
        System.out.println("Reset successful");
    }
}
