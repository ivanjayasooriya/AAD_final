package com.ijse.gdse73.harmoniq_backend.service.impl;

import com.ijse.gdse73.harmoniq_backend.dto.BanUserDTO;
import com.ijse.gdse73.harmoniq_backend.dto.BannedUserDTO;
import com.ijse.gdse73.harmoniq_backend.entity.User;
import com.ijse.gdse73.harmoniq_backend.entity.UserBan;
import com.ijse.gdse73.harmoniq_backend.exception.CustomException;
import com.ijse.gdse73.harmoniq_backend.repo.UserBanRepo;
import com.ijse.gdse73.harmoniq_backend.repo.UserRepo;
import com.ijse.gdse73.harmoniq_backend.service.UserBanService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBanServiceImpl implements UserBanService {
    private final UserBanRepo userBanRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    @Override
    public void banUser(BanUserDTO banUserDTO) {
        User user = userRepo.findById(banUserDTO.getUserId())
                .orElseThrow(() -> new CustomException("User not found"));

        boolean isBanned = userBanRepo.existsUserBanByUser(user);
        UserBan userBan;

        if (isBanned) {
            userBan = userBanRepo.findByUser(user);
            userBan.setBanExpiry(calculateBanExpiry(banUserDTO.getBanDuration()));
            userBan.setBanReason(banUserDTO.getBanReason());

        } else {
            userBan = UserBan.builder()
                    .user(user)
                    .banExpiry(calculateBanExpiry(banUserDTO.getBanDuration()))
                    .banReason(banUserDTO.getBanReason())
                    .build();
        }

        userBanRepo.save(userBan);
    }

    @Override
    public String checkBanStatus(Long userId) {
        User user = userRepo.findUserById(userId);
        UserBan bannedUser = userBanRepo.findByUser(user);
        boolean isExist = bannedUser != null;

        if (!isExist) {
            return "Not Banned";
        }

        if (bannedUser.getBanExpiry().isBefore(LocalDateTime.now())) {
            userBanRepo.delete(bannedUser);
            return "Not Banned";

        } else {
            return "Banned";
        }
    }

    @Override
    @Transactional
    public String unbanUser(Long userId) {
        User user = userRepo.findUserById(userId);
        UserBan userBan = userBanRepo.findByUser(user);

        if (userBan != null) {
            user.setUserBan(null);
            userRepo.save(user);

            userBanRepo.delete(userBan);
            return "User unbanned successfully";
        }

        return "User is not banned";
    }

    @Override
    public List<BannedUserDTO> getAllBannedUsers() {
        List<UserBan> bannedUsers = userBanRepo.findAll();

        return bannedUsers.stream().map(ban -> {
            User user = ban.getUser();

            BannedUserDTO dto = new BannedUserDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setBanReason(ban.getBanReason());

            return dto;
        }).toList();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void unbanExpiredUsers() {
        List<UserBan> bannedUsers = userBanRepo.findAll();

        for (UserBan bannedUser : bannedUsers) {
            if (bannedUser.getBanExpiry().isBefore(LocalDateTime.now())) {
                userBanRepo.delete(bannedUser);
            }
        }
    }

    public LocalDateTime calculateBanExpiry(String duration) {
        LocalDateTime now = LocalDateTime.now();

        return switch (duration) {
            case "24h" -> now.plusHours(24);
            case "7d" -> now.plusDays(7);
            case "30d" -> now.plusDays(30);
            case "1y" -> now.plusYears(1);
            case "permanent" -> null; // no expiry
            default -> throw new IllegalArgumentException("Invalid duration");
        };
    }
}
