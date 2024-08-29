package com.scm.service.serviceImpl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm.service.ImageService;

@Service
public class ImageServiceImpl implements ImageService{

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String uploadImge(MultipartFile image, String filename) {

        

        
        try {
            if (image == null || image.isEmpty()) {
                return null; 
            }
            byte[] data = new byte[image.getInputStream().available()];
            image.getInputStream().read(data);
            cloudinary.uploader().upload(data, ObjectUtils.asMap(
                "public_id", filename));
            
            return this.getURlFromPublicId(filename);
        } catch (IOException e) {
            
            e.printStackTrace();
            return null;
        }
        
        
    }

    @Override
    public String getURlFromPublicId(String publicId) {
        return cloudinary
        .url()
        .transformation(
            new Transformation<>()
                .width(500)
                .height(500)
                .crop("fill")
        )
        .generate(publicId);

    }
    
}
