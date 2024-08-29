package com.scm.validators;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile>{

    private static final long MAX_FILE_SIZE = 1024 * 1024 * 5;
    private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList(
        "image/png",
        "image/jpeg",
        "image/jpg"
    );

    
    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if(file.isEmpty() || file == null) return true;

        if(file.getSize() > MAX_FILE_SIZE){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File size cannot be more than 5MB").addConstraintViolation();
            return false;
        }

        if(!ALLOWED_FILE_TYPES.contains(file.getContentType())){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File type is not valid. Upload JPEG, JPG or PNG only").addConstraintViolation();
            return false;
        }

        return true;
    }
    
}
