package com.ijse.gdse73.harmoniq_backend.service;

import com.ijse.gdse73.harmoniq_backend.dto.BanAppealDTO;

public interface BanAppealService {
    void addBanAppeal(BanAppealDTO banAppealDTO);
    String getAppeal(Long userId);
}
