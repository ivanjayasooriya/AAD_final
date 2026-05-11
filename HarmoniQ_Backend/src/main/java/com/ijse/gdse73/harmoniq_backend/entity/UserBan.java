package com.ijse.gdse73.harmoniq_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDateTime banExpiry;
    private String banReason;

    @OneToOne(mappedBy = "userBan", cascade = CascadeType.ALL, orphanRemoval = true)
    private BanAppeal banAppeal;
}
