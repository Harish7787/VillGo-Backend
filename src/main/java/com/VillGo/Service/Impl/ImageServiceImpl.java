package com.VillGo.Service.Impl;

import com.VillGo.DTO.Responce.MessageResponse;
import com.VillGo.Service.ImageService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final Cloudinary cloudinary;

    @Override
    public MessageResponse uploadImage(MultipartFile file) {

        try {

            if (file == null || file.isEmpty()) {
                return new MessageResponse(false, HttpStatus.BAD_REQUEST,
                        "File is empty", null);
            }

            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "ecommerce")
            );

            String imageUrl = uploadResult.get("secure_url").toString();

            return new MessageResponse(true, HttpStatus.OK,
                    "Image uploaded successfully", imageUrl);

        } catch (Exception e) {

            return new MessageResponse(false, HttpStatus.INTERNAL_SERVER_ERROR,
                    "Image upload failed", null);
        }
    }

    @Override
    public MessageResponse deleteImage(String imageUrl) {

        try {

            if (imageUrl == null || imageUrl.isBlank()) {
                return new MessageResponse(false, HttpStatus.BAD_REQUEST,
                        "Invalid image URL", null);
            }

            String publicId = extractPublicId(imageUrl);

            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            return new MessageResponse(true, HttpStatus.OK,
                    "Image deleted successfully", null);

        } catch (Exception e) {

            return new MessageResponse(false, HttpStatus.INTERNAL_SERVER_ERROR,
                    "Image deletion failed", null);
        }
    }

    private String extractPublicId(String imageUrl) {

        String[] parts = imageUrl.split("/");
        String fileName = parts[parts.length - 1];

        return "ecommerce/" + fileName.substring(0, fileName.lastIndexOf("."));
    }
}
