package com.ijse.gdse73.harmoniq_backend.controller;

import com.ijse.gdse73.harmoniq_backend.dto.APIResponse;
import com.ijse.gdse73.harmoniq_backend.dto.ArtistDTO;
import com.ijse.gdse73.harmoniq_backend.entity.Artist;
import com.ijse.gdse73.harmoniq_backend.exception.CustomException;
import com.ijse.gdse73.harmoniq_backend.service.ArtistService;
import com.ijse.gdse73.harmoniq_backend.service.cloudinary.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("api/v1/artist")
@CrossOrigin
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;
    private final CloudinaryService cloudinaryService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse> addArtist(@RequestParam("name") String name,
                                                 @RequestParam("bio") String bio,
                                                 @RequestParam("profilePic") MultipartFile profilePic) throws IOException {

        // 1. Upload profile image to Cloudinary
        String pfpUrl = cloudinaryService.uploadImage(profilePic);

        // 2. Prepare DTO with Cloudinary CDN URL
        ArtistDTO artistDTO = new ArtistDTO();
        artistDTO.setName(name);
        artistDTO.setBio(bio);
        artistDTO.setPfpPath(pfpUrl);

        artistService.addArtist(artistDTO);

        return ResponseEntity.ok(new APIResponse(200, "OK", null));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse> deleteArtist(@PathVariable Long id) {
        Artist artist = artistService.deleteArtist(id);

        // Remove profile image asset from Cloudinary
        cloudinaryService.deleteFileByUrl(artist.getPfpPath(), "image");

        return ResponseEntity.ok(new APIResponse(200, "OK", null));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse> updateArtist(@PathVariable Long id,
                                                    @RequestParam("name") String name,
                                                    @RequestParam("bio") String bio,
                                                    @RequestParam(value = "profilePic", required = false) MultipartFile profilePic) throws IOException {

        ArtistDTO existingDTO = artistService.findArtist(id);

        ArtistDTO updatedDTO = new ArtistDTO();
        updatedDTO.setId(id);
        updatedDTO.setName(name);
        updatedDTO.setBio(bio);
        updatedDTO.setPfpPath(existingDTO.getPfpPath());

        // Update profile picture if provided
        if (profilePic != null && !profilePic.isEmpty()) {
            // Delete old picture from Cloudinary
            cloudinaryService.deleteFileByUrl(existingDTO.getPfpPath(), "image");

            // Upload new picture
            String newPfpUrl = cloudinaryService.uploadImage(profilePic);
            updatedDTO.setPfpPath(newPfpUrl);
        }

        artistService.updateArtist(updatedDTO);

        return ResponseEntity.ok(new APIResponse(200, "OK", null));
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<APIResponse> findArtist(@PathVariable Long id) {
        return ResponseEntity.ok(new APIResponse(
                200, "OK", artistService.findArtist(id)
        ));
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<APIResponse> getAllArtists() {
        return ResponseEntity.ok(new APIResponse(
                200, "OK", artistService.getAllArtists()
        ));
    }

    /**
     * Preserved Route: Profile Picture Access
     * Automatically redirects HTTP requests to the Cloudinary CDN URL.
     */
    @GetMapping("/profile-pic/{id}")
    public ResponseEntity<Void> getProfilePic(@PathVariable Long id) {
        ArtistDTO artistDTO = artistService.findArtist(id);

        if (artistDTO == null || artistDTO.getPfpPath() == null) {
            throw new CustomException("Profile Picture not found");
        }

        // HTTP 302 Redirect directly to Cloudinary URL
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(artistDTO.getPfpPath()))
                .build();
    }
}