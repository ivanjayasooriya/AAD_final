package com.ijse.gdse73.harmoniq_backend.service;

import com.ijse.gdse73.harmoniq_backend.dto.ActivityDTO;

import java.util.List;

public interface ActivityService {
    void addActivity(ActivityDTO activityDTO);
    List<ActivityDTO> loadActivity();
}
