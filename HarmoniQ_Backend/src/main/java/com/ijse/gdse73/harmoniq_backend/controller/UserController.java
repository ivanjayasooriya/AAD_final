package com.ijse.gdse73.harmoniq_backend.controller;

import com.ijse.gdse73.harmoniq_backend.dto.APIResponse;
import com.ijse.gdse73.harmoniq_backend.dto.UserDTO;
import com.ijse.gdse73.harmoniq_backend.dto.UserProfilePicDTO;
import com.ijse.gdse73.harmoniq_backend.exception.CustomException;
import com.ijse.gdse73.harmoniq_backend.service.cloudinary.CloudinaryService;
import com.ijse.gdse73.harmoniq_backend.service.UserProfilePicService;
import com.ijse.gdse73.harmoniq_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("api/v1/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserProfilePicService userProfilePicService;
    private final CloudinaryService cloudinaryService;

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<APIResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(new APIResponse(
                200, "User Fetched Successfully!", userService.getUser(id)
        ));
    }

    /**
     * Preserved Route: Get Profile Picture
     * Automatically redirects HTTP requests directly to the Cloudinary CDN URL.
     */
    @GetMapping("/get-profile-pic/{id}")
    public ResponseEntity<Void> getProfilePic(@PathVariable Long id) {
        UserProfilePicDTO userProfilePicDTO = userProfilePicService.findProfilePic(id);

        if (userProfilePicDTO == null || userProfilePicDTO.getProfilePic() == null) {
            throw new CustomException("Profile Picture not found");
        }

        // HTTP 302 Redirect directly to Cloudinary URL
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(userProfilePicDTO.getProfilePic()))
                .build();
    }

    @PostMapping("/upload-profile-pic")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<APIResponse> uploadProfilePic(
            @RequestParam("userId") Long userId,
            @RequestParam("profilePic") MultipartFile profilePic) throws IOException {

        // 1. Check if user already has a profile picture and delete old image from Cloudinary
        UserProfilePicDTO existingPicDTO = userProfilePicService.findProfilePic(userId);
        if (existingPicDTO != null && existingPicDTO.getProfilePic() != null) {
            cloudinaryService.deleteFileByUrl(existingPicDTO.getProfilePic(), "image");
        }

        // 2. Upload new profile picture to Cloudinary
        String pfpUrl = cloudinaryService.uploadImage(profilePic);

        // 3. Save Cloudinary CDN URL to Database
        UserProfilePicDTO userProfilePicDTO = new UserProfilePicDTO();
        userProfilePicDTO.setUserId(userId);
        userProfilePicDTO.setProfilePic(pfpUrl);

        userProfilePicService.uploadProfilePic(userProfilePicDTO);

        return ResponseEntity.ok(new APIResponse(200, "OK", null));
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse> getAllUser() {
        return ResponseEntity.ok(new APIResponse(
                200, "Get All Users Successfully!", userService.getAllUsers()
        ));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<APIResponse> deleteUser(@PathVariable Long id) {
        // Clean up Cloudinary asset before deleting user
        UserProfilePicDTO profilePicDTO = userProfilePicService.findProfilePic(id);
        if (profilePicDTO != null && profilePicDTO.getProfilePic() != null) {
            cloudinaryService.deleteFileByUrl(profilePicDTO.getProfilePic(), "image");
        }

        userService.deleteUser(id);
        return ResponseEntity.ok(new APIResponse(
                200, "User Deleted Successfully", null
        ));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<APIResponse> updateUser(@RequestBody @Valid UserDTO userDTO) {
        userService.updateUser(userDTO);
        return ResponseEntity.ok(new APIResponse(
                200, "User updated successfully", null
        ));
    }

    @PutMapping("/update-role/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String role = body.get("role");
        userService.updateUserRole(id, role);
        return ResponseEntity.ok(new APIResponse(
                200, "User role updated successfully", null
        ));
    }
}