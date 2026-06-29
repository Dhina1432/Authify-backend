package com.example.authify.io;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {

    @NotBlank(message = "Password should not be empty")
    private String newPassword;
    @NotBlank(message = "OTP should not be empty")
    private String otp;
    @NotBlank(message = "Email should not be empty")
    private String email;
}
