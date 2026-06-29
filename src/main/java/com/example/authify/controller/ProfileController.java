package com.example.authify.controller;


import com.example.authify.io.ProfileRequest;
import com.example.authify.io.ProfileResponse;
import com.example.authify.service.EmailService;
import com.example.authify.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    private final EmailService emailService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse response(@Valid @RequestBody ProfileRequest request) {
        ProfileResponse response=profileService.createProfile(request);
        try {
            emailService.sendWelcomeEmail(response.getEmail(), response.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

//    @GetMapping("/test")
//    public String test(){
//        return "Auth is Working";
//    }
    @GetMapping("/profile")
    public ProfileResponse getProfile(@CurrentSecurityContext(expression = "authentication?.name") String email){
        return profileService.getProfile(email);
    }

}
