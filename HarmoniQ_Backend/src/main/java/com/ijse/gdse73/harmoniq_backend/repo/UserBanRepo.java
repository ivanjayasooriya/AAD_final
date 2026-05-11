package com.ijse.gdse73.harmoniq_backend.repo;

import com.ijse.gdse73.harmoniq_backend.entity.User;
import com.ijse.gdse73.harmoniq_backend.entity.UserBan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBanRepo extends JpaRepository<UserBan, Long> {
    boolean existsUserBanByUser(User user);
    UserBan findByUser(User user);
}
