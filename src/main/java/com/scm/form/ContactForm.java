package com.scm.form;


import org.springframework.web.multipart.MultipartFile;

import com.scm.validators.ValidFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ContactForm {
    @NotBlank(message = "Contact name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String contactName;

    @Email(message = "Invalid Email Address")
    private String contactEmail;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp="^[0-9]{10}$", message = "Invalid Contact Number")
    private String number;
    private String contactAddress;
    private String contactDescription;
    private String websiteLink;
    private String linkedInLink;
    private boolean favourite;

    @ValidFile
    private MultipartFile image;

    private String picture;



}
