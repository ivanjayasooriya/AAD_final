package com.ijse.gdse73.harmoniq_backend.repo;

import com.ijse.gdse73.harmoniq_backend.entity.BanAppeal;
import com.ijse.gdse73.harmoniq_backend.entity.UserBan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BanAppealRepo extends JpaRepository<BanAppeal, Long> {
    BanAppeal findByUserBan(UserBan userBan);
}
