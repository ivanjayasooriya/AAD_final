package com.ijse.gdse73.harmoniq_backend.controller;

import com.ijse.gdse73.harmoniq_backend.dto.APIResponse;
import com.ijse.gdse73.harmoniq_backend.dto.MusicDTO;
import com.ijse.gdse73.harmoniq_backend.entity.Music;
import com.ijse.gdse73.harmoniq_backend.exception.CustomException;
import com.ijse.gdse73.harmoniq_backend.service.cloudinary.CloudinaryService;
import com.ijse.gdse73.harmoniq_backend.service.MusicService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/music")
@CrossOrigin
@RequiredArgsConstructor
public class MusicController {

    private final MusicService musicService;
    private final CloudinaryService cloudinaryService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse> uploadMusic(
            @RequestParam("musicFile") MultipartFile musicFile,
            @RequestParam("thumbnail") MultipartFile thumbnail,
            @RequestParam("musicTitle") String musicTitle,
            @RequestParam("musicArtist") String musicArtist,
            @RequestParam("genreId") Long musicGenreId) throws IOException {

        // Upload files to Cloudinary
        String musicUrl = cloudinaryService.uploadAudio(musicFile);
        String thumbnailUrl = cloudinaryService.uploadImage(thumbnail);

        // Save DTO with Cloudinary CDN URLs
        MusicDTO musicDTO = new MusicDTO();
        musicDTO.setFileName(musicFile.getOriginalFilename());
        musicDTO.setMusicPath(musicUrl);
        musicDTO.setThumbnailPath(thumbnailUrl);
        musicDTO.setMusicTitle(musicTitle);
        musicDTO.setMusicArtist(musicArtist);
        musicDTO.setMusicGenreId(musicGenreId);

        musicService.saveMusic(musicDTO);

        return ResponseEntity.ok(new APIResponse(200, "OK", null));
    }

    /**
     * Preserved Route: Audio Streaming
     * Redirects the client automatically to Cloudinary CDN URL.
     */
    @GetMapping("/stream/{id}")
    public ResponseEntity<Void> streamMusic(@PathVariable Long id) {
        MusicDTO musicDTO = musicService.getMusicById(id);

        if (musicDTO == null || musicDTO.getMusicPath() == null) {
            throw new CustomException("Music file not found");
        }

        // HTTP 302 Redirect to Cloudinary URL (Audio tags follow this seamlessly)
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(musicDTO.getMusicPath()))
                .build();
    }

    /**
     * Preserved Route: Thumbnail Display
     * Redirects the client automatically to Cloudinary CDN URL.
     */
    @GetMapping("/thumbnail/{id}")
    public ResponseEntity<Void> getThumbnail(@PathVariable Long id) {
        MusicDTO musicDTO = musicService.getMusicById(id);

        if (musicDTO == null || musicDTO.getThumbnailPath() == null) {
            throw new CustomException("Thumbnail not found");
        }

        // HTTP 302 Redirect to Cloudinary URL (Img tags follow this seamlessly)
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(musicDTO.getThumbnailPath()))
                .build();
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<APIResponse> getAllMusic() {
        return ResponseEntity.ok(new APIResponse(
                200, "OK", musicService.getAllMusic()
        ));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<APIResponse> deleteMusic(@PathVariable Long id) {
        Music music = musicService.deleteMusic(id);

        // Delete media files from Cloudinary
        cloudinaryService.deleteFileByUrl(music.getMusicPath(), "video");
        cloudinaryService.deleteFileByUrl(music.getThumbnailPath(), "image");

        return ResponseEntity.ok(new APIResponse(200, "OK", null));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<APIResponse> updateMusic(
            @PathVariable Long id,
            @RequestParam(value = "musicFile", required = false) MultipartFile musicFile,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestParam("musicTitle") String musicTitle,
            @RequestParam("musicArtist") String musicArtist,
            @RequestParam("genreId") Long musicGenreId) throws IOException {

        MusicDTO existingMusic = musicService.getMusicById(id);

        MusicDTO updatedDTO = new MusicDTO();
        updatedDTO.setId(id);
        updatedDTO.setMusicTitle(musicTitle);
        updatedDTO.setMusicArtist(musicArtist);
        updatedDTO.setMusicGenreId(musicGenreId);
        updatedDTO.setFileName(existingMusic.getFileName());
        updatedDTO.setMusicPath(existingMusic.getMusicPath());
        updatedDTO.setThumbnailPath(existingMusic.getThumbnailPath());

        // Update audio file if provided
        if (musicFile != null && !musicFile.isEmpty()) {
            cloudinaryService.deleteFileByUrl(existingMusic.getMusicPath(), "video");
            String newMusicUrl = cloudinaryService.uploadAudio(musicFile);
            updatedDTO.setMusicPath(newMusicUrl);
            updatedDTO.setFileName(musicFile.getOriginalFilename());
        }

        // Update thumbnail file if provided
        if (thumbnail != null && !thumbnail.isEmpty()) {
            cloudinaryService.deleteFileByUrl(existingMusic.getThumbnailPath(), "image");
            String newThumbnailUrl = cloudinaryService.uploadImage(thumbnail);
            updatedDTO.setThumbnailPath(newThumbnailUrl);
        }

        musicService.updateMusic(updatedDTO);

        return ResponseEntity.ok(new APIResponse(200, "Music updated successfully", null));
    }
}