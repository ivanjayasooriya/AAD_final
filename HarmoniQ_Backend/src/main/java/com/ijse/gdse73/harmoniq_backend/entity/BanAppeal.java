package com.ijse.gdse73.harmoniq_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BanAppeal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userMessage;

    @OneToOne
    @JoinColumn(name = "user_ban_id")
    private UserBan userBan;
}