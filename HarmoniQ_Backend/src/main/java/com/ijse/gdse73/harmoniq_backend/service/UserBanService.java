package com.ijse.gdse73.harmoniq_backend.service;

import com.ijse.gdse73.harmoniq_backend.dto.BanUserDTO;
import com.ijse.gdse73.harmoniq_backend.dto.BannedUserDTO;

import java.util.List;

public interface UserBanService {
    void banUser(BanUserDTO banUserDTO);
    String checkBanStatus(Long userId);
    String unbanUser(Long id);
    List<BannedUserDTO> getAllBannedUsers();
}
