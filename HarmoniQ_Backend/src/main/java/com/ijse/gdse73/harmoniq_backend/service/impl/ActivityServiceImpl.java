package com.ijse.gdse73.harmoniq_backend.service.impl;

import com.ijse.gdse73.harmoniq_backend.dto.ActivityDTO;
import com.ijse.gdse73.harmoniq_backend.entity.Activity;
import com.ijse.gdse73.harmoniq_backend.repo.ActivityRepo;
import com.ijse.gdse73.harmoniq_backend.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final ModelMapper modelMapper;
    private final ActivityRepo activityRepo;

    @Override
    public void addActivity(ActivityDTO activityDTO) {
        activityRepo.save(modelMapper.map(activityDTO, Activity.class));
    }

    @Override
    public List<ActivityDTO> loadActivity() {
        List<Activity> activities = activityRepo.findAll();
        if (!activities.isEmpty()) {
            return activities.stream()
                    .map(activity -> modelMapper.map(activity, ActivityDTO.class))
                    .toList();
        }
        return List.of();
    }
}
