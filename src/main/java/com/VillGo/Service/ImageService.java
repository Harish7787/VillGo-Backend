package com.VillGo.Service;

import com.VillGo.DTO.Responce.MessageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    MessageResponse uploadImage(MultipartFile file);

    MessageResponse deleteImage(String imageUrl);
}
