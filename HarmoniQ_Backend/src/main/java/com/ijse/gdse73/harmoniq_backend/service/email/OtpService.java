package com.ijse.gdse73.harmoniq_backend.service.email;

import com.ijse.gdse73.harmoniq_backend.entity.Otp;
import com.ijse.gdse73.harmoniq_backend.repo.OtpRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepo otpRepo;
    private final PasswordEncoder passwordEncoder;

    private static final long OTP_VALIDITY_SECONDS = 60;

    // Generate OTP
    public String generateOtp(String email) {
        String rawOtp = String.valueOf(100000 + new Random().nextInt(900000));
        String encodedOtp = passwordEncoder.encode(rawOtp);

        LocalDateTime expiryTime = LocalDateTime.now().plusSeconds(OTP_VALIDITY_SECONDS);

        Otp otpEntity = Otp.builder()
                .email(email)
                .otp(encodedOtp)
                .expiryTime(expiryTime)
                .build();

        otpRepo.deleteByEmail(email);
        otpRepo.save(otpEntity);

        return rawOtp;
    }

    // Validate OTP
    public boolean validateOtp(String email, String otp) {
        return otpRepo.findByEmail(email)
                .map(otpEntity -> {
                    // Check expiry
                    if (otpEntity.getExpiryTime().isBefore(LocalDateTime.now())) {
                        otpRepo.delete(otpEntity);
                        return false;
                    }

                    // Check match
                    boolean matches = passwordEncoder.matches(otp, otpEntity.getOtp());
                    if (matches) otpRepo.delete(otpEntity);
                    return matches;
                })
                .orElse(false);
    }

    @Scheduled(fixedRate = 60000)
    public void removeExpiredOtps() {
        LocalDateTime now = LocalDateTime.now();
        otpRepo.findAll().stream()
                .filter(otp -> otp.getExpiryTime().isBefore(now))
                .forEach(otpRepo::delete);
    }
}