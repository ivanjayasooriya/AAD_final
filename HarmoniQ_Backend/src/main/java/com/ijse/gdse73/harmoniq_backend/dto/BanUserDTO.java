package com.ijse.gdse73.harmoniq_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BanUserDTO {
    private Long id;
    private Long userId;
    private String banDuration;
    private String banReason;
}
