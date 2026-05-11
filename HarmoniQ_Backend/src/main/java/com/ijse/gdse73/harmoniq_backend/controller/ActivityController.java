package com.ijse.gdse73.harmoniq_backend.controller;

import com.ijse.gdse73.harmoniq_backend.dto.APIResponse;
import com.ijse.gdse73.harmoniq_backend.dto.ActivityDTO;
import com.ijse.gdse73.harmoniq_backend.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/activity")
@CrossOrigin
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping("/load")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse> loadActivity(){

        return ResponseEntity.ok(new APIResponse(
                200,"OK",activityService.loadActivity()
        ));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse> addActivity(@RequestBody ActivityDTO activityDTO){
        activityService.addActivity(activityDTO);

        return ResponseEntity.ok(new APIResponse(
                200,"OK",null
        ));
    }
}
