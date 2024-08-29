package com.scm.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String uploadImge(MultipartFile image, String filename);
    String getURlFromPublicId(String publicId);
    
}
