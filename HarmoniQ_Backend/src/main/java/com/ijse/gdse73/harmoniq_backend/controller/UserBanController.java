package com.ijse.gdse73.harmoniq_backend.controller;

import com.ijse.gdse73.harmoniq_backend.dto.APIResponse;
import com.ijse.gdse73.harmoniq_backend.dto.BanAppealDTO;
import com.ijse.gdse73.harmoniq_backend.dto.BanUserDTO;
import com.ijse.gdse73.harmoniq_backend.service.BanAppealService;
import com.ijse.gdse73.harmoniq_backend.service.UserBanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user/ban")
@CrossOrigin
@RequiredArgsConstructor
public class UserBanController {
    private final UserBanService userBanService;
    private final BanAppealService banAppealService;

    @PutMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse> banUser(@RequestBody BanUserDTO banUserDTO){
        userBanService.banUser(banUserDTO);
        return ResponseEntity.ok(new APIResponse(
                200,"User Banned",null
        ));
    }

    @GetMapping("/check/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<APIResponse> checkBanStatus(@PathVariable Long userId){
        return ResponseEntity.ok(new APIResponse(
                200,"OK",userBanService.checkBanStatus(userId)
        ));
    }

    @GetMapping("/unban/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse> unbanUser(@PathVariable Long id){
        return ResponseEntity.ok(new APIResponse(
                200,"OK",userBanService.unbanUser(id)
        ));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse> getBannedUsers(){
        return ResponseEntity.ok(new APIResponse(
                200,"OK",userBanService.getAllBannedUsers()
        ));
    }

    @PutMapping("/appeal")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<APIResponse> addBanAppeal(@RequestBody BanAppealDTO banAppealDTO){
        banAppealService.addBanAppeal(banAppealDTO);

        return ResponseEntity.ok(new APIResponse(
                200,"OK",null
        ));
    }

    @GetMapping("/get-appeal/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse> getUserAppeal(@PathVariable Long id){
        return ResponseEntity.ok(new APIResponse(
                200,"OK", banAppealService.getAppeal(id)
        ));
    }
}
