package com.scm.entities;

// import jakarta.persistence.Entity;
import jakarta.persistence.Id;
// import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// @Entity
// @Table(name = "userform")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserForm {
    @Id
    private String id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Min 3 characters is required")
    private String name;

    @Email(message = "Invalid Email Address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Min 6 characters required")
    private String password;

    @NotBlank(message = "About is required")
    private String about;

    @Size(min = 10, max = 10, message = "Invalid phone number")
    private String phoneNumber;
}
