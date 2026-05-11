package com.ijse.gdse73.harmoniq_backend.service.impl;

import com.ijse.gdse73.harmoniq_backend.dto.BanAppealDTO;
import com.ijse.gdse73.harmoniq_backend.entity.BanAppeal;
import com.ijse.gdse73.harmoniq_backend.entity.User;
import com.ijse.gdse73.harmoniq_backend.entity.UserBan;
import com.ijse.gdse73.harmoniq_backend.exception.CustomException;
import com.ijse.gdse73.harmoniq_backend.repo.BanAppealRepo;
import com.ijse.gdse73.harmoniq_backend.repo.UserBanRepo;
import com.ijse.gdse73.harmoniq_backend.repo.UserRepo;
import com.ijse.gdse73.harmoniq_backend.service.BanAppealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BanAppealServiceImpl implements BanAppealService {
    private final UserBanRepo userBanRepo;
    private final BanAppealRepo banAppealRepo;
    private final UserRepo userRepo;

    @Override
    public void addBanAppeal(BanAppealDTO banAppealDTO) {
        User user = userRepo.findById(banAppealDTO.getUserId())
                .orElseThrow(() -> new CustomException("User not found"));

        UserBan userBan = userBanRepo.findByUser(user);
        BanAppeal banAppeal = banAppealRepo.findByUserBan(userBan);

        if (banAppeal != null) {
            banAppeal.setUserMessage(banAppealDTO.getMessage());

        } else {
            banAppeal = BanAppeal.builder()
                    .userBan(userBan)
                    .userMessage(banAppealDTO.getMessage())
                    .build();
        }

        banAppealRepo.save(banAppeal);
    }

    @Override
    public String getAppeal(Long userId) {
        User user = userRepo.findUserById(userId);
        UserBan userBan = userBanRepo.findByUser(user);
        BanAppeal banAppeal = banAppealRepo.findByUserBan(userBan);

        if (banAppeal == null) {
            return "No appeal message";
        } else {
            return banAppeal.getUserMessage();
        }
    }
}
