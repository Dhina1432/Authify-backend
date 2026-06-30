package com.example.authify.service;

import com.example.authify.entity.UserEntity;
import com.example.authify.io.ProfileRequest;
import com.example.authify.io.ProfileResponse;
import com.example.authify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public ProfileResponse createProfile(ProfileRequest request) {
        UserEntity newProfile=convertToUserEntity(request);
        if (!userRepository.existsByEmail(request.getEmail())) {
            newProfile=userRepository.save(newProfile);
            return convertToProfileResponse(newProfile);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already exists");
    }

    @Override
    public ProfileResponse getProfile(String email) {
        UserEntity existingUser= userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not Found: "+email));
        return convertToProfileResponse(existingUser);
    }

    @Override
    public void sendResetOtp(String email) {
        UserEntity existEntity= userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found: "+ email));
        // generate 6 digit OTP
        String otp=String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));

        //expiry time(current time+ 10 min in millisecond)
        long expiryTime=System.currentTimeMillis() +(15 *60*1000);

        //update the user or profile

        existEntity.setResetOtp(otp);
        existEntity.setResetOtpExpireAt(expiryTime);

        //save in db
        userRepository.save(existEntity);

        try {
            System.out.println("Before sending email");
            emailService.sendResetOtp(existEntity.getEmail(), otp);
            System.out.println("After sending email");
        } catch (Exception e) {
            throw new RuntimeException("Unable to send Email");
        }

    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        UserEntity existingUser=userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found: "+email));

        if(existingUser.getResetOtp()==null || !existingUser.getResetOtp().equals(otp)){
            throw new RuntimeException("Invalid OTP");
        }

        if (existingUser.getResetOtpExpireAt()<System.currentTimeMillis()){
            throw new RuntimeException("OTP Expired");
        }

        existingUser.setPassword(passwordEncoder.encode(newPassword));
        existingUser.setResetOtpExpireAt(0L);
        existingUser.setResetOtp(null);

        userRepository.save(existingUser);
    }

    @Override
    public void sendOtp(String email) {
        UserEntity existingUser=userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found: "+email));
        if (existingUser.getIsAccountVerified() != null && existingUser.getIsAccountVerified()) {
            return;
        }

        //generate 6 digit otp
        String otp=String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));

        //expiry time(current time+ 10 min in millisecond)
        long expiryTime=System.currentTimeMillis() +(24*60*60*1000);

            //update userEntity
        existingUser.setVerifyOtp(otp);
        existingUser.setVerifyOtpExpireAt(expiryTime);

        //save in db
        userRepository.save(existingUser);

        try {
             emailService.sendOtpEmail(existingUser.getEmail(),otp);
        } catch (Exception e) {
            throw new RuntimeException("Unable to send Email");
        }

    }

    @Override
    public void verifyOtp(String email, String otp) {
        UserEntity existingUser= userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found: "+email));

        if (existingUser.getVerifyOtp()==null || !existingUser.getVerifyOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        if (existingUser.getVerifyOtpExpireAt() <System.currentTimeMillis()){
            throw new RuntimeException("OTP Expired");
        }

        existingUser.setVerifyOtp(null);
        existingUser.setVerifyOtpExpireAt(0L);
        existingUser.setIsAccountVerified(true);

        userRepository.save(existingUser);

    }


    private ProfileResponse convertToProfileResponse(UserEntity newProfile) {
        return ProfileResponse.builder()
                .name(newProfile.getName())
                .email(newProfile.getEmail())
                .userId(newProfile.getUserId())
                .isAccountVerified(newProfile.getIsAccountVerified())
                .build();
    }

    private UserEntity convertToUserEntity(ProfileRequest request) {
        return UserEntity.builder()
                .email(request.getEmail())
                .userId(UUID.randomUUID().toString())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .isAccountVerified(false)
                .resetOtpExpireAt(0L)
                .resetOtp(null)
                .verifyOtp(null)
                .verifyOtpExpireAt(0L)
                .build();

    }
}
