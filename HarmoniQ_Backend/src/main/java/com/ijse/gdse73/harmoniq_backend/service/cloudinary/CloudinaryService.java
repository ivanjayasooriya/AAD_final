package com.ijse.gdse73.harmoniq_backend.service.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    /**
     * Uploads audio/video files to Cloudinary.
     */
    public String uploadAudio(MultipartFile file) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "resource_type", "video", // Cloudinary categorizes audio under "video"
                "folder", "harmoniq/music"
        ));
        return uploadResult.get("secure_url").toString();
    }

    /**
     * Uploads thumbnail images to Cloudinary.
     */
    public String uploadImage(MultipartFile file) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "resource_type", "image",
                "folder", "harmoniq/thumbnails"
        ));
        return uploadResult.get("secure_url").toString();
    }

    /**
     * Extracts public_id from Cloudinary URL and deletes the asset.
     */
    public void deleteFileByUrl(String fileUrl, String resourceType) {
        if (fileUrl == null || fileUrl.isEmpty()) return;
        try {
            String publicId = extractPublicId(fileUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", resourceType));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String extractPublicId(String url) {
        // Example URL: https://res.cloudinary.com/cloud_name/video/upload/v123456/harmoniq/music/file.mp3
        String[] parts = url.split("/upload/");
        if (parts.length > 1) {
            String pathAfterUpload = parts[1];
            // Remove version prefix if present (e.g., "v123456789/")
            pathAfterUpload = pathAfterUpload.replaceFirst("^v\\d+/", "");
            // Remove file extension
            int lastDotIdx = pathAfterUpload.lastIndexOf('.');
            return (lastDotIdx != -1) ? pathAfterUpload.substring(0, lastDotIdx) : pathAfterUpload;
        }
        return url;
    }
}
