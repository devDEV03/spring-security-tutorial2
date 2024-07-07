package com.springbuffer.spring_security_client.service;

import com.springbuffer.spring_security_client.entity.User;
import com.springbuffer.spring_security_client.entity.VerificationToken;
import com.springbuffer.spring_security_client.model.UserModel;

import java.util.Optional;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(User user, String token);

    String validateVerificationToken(String token);

    VerificationToken generateNewToken(String oldToken);

    User findByEmail(String email);

    void createPasswordTokenForUser(User user, String token);

    String validatePasswordToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    void changePassword(User user, String newPassword);

    boolean checkIfOldPasswordStillValid(String oldPassword,User user);
}
