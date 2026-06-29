package com.example.authify.io;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {
    @NotBlank(message = "Name should not be Empty")
    private String name;
    @Email(message = "Enter Valid Email Address")
    @NotNull(message = "Email should not be Empty")
    private String email;
    @Size(min = 8, message = "Password should be at least 8 Characters")
    private String password;
}
