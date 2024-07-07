package com.springbuffer.spring_security_client.controller;



import com.springbuffer.spring_security_client.entity.User;
import com.springbuffer.spring_security_client.entity.VerificationToken;
import com.springbuffer.spring_security_client.event.RegistrationCompletionEvent;
import com.springbuffer.spring_security_client.model.PasswordModel;
import com.springbuffer.spring_security_client.model.UserModel;
import com.springbuffer.spring_security_client.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;


@RestController
@Slf4j
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest httpServletRequest){
        User user = userService.registerUser(userModel);
        applicationEventPublisher.publishEvent(new RegistrationCompletionEvent(user,applicationUrl(httpServletRequest)));

        return "Success";
    }

    @GetMapping("/verifyRegistration")
    public String validateToken(@RequestParam("token") String token){
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")){
            return "User verified Successfully";
        }
        else{
            return "Bad user";
        }
    }

    @GetMapping("/verifyRegisterationToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken,HttpServletRequest request){
        VerificationToken verificationToken = userService.generateNewToken(oldToken);
        User user = verificationToken.getUser();
        resendVerificationEmail(user,applicationUrl(request),verificationToken);
        return "Verification Link Sent";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel,HttpServletRequest request){
    User user = userService.findByEmail(passwordModel.getEmail());
    String url = "";
    if(user != null){
        String token = UUID.randomUUID().toString();
        userService.createPasswordTokenForUser(user,token);
        url = passwordResetTokenMail(user,applicationUrl(request),token);
    }
    return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token,@RequestBody PasswordModel passwordModel){

        String valid = userService.validatePasswordToken(token);
        if(!valid.equalsIgnoreCase("valid")){
            return "Invalid Token";
        }

        Optional<User> user = userService.getUserByPasswordResetToken(token);
        if(user.isPresent()){
            userService.changePassword(user.get(),passwordModel.getNewPassword());
            return "Password Reset Successful";
        }
        else{
            return "Invalid Token";
        }
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordModel passwordModel){
        User user = userService.findByEmail(passwordModel.getEmail());
        if(!userService.checkIfOldPasswordStillValid(passwordModel.getOldPassword(),user)){
            return "Invalid Old Password";
        }

        userService.changePassword(user,passwordModel.getNewPassword());
        return "Password Changed Successfully";
    }

    private String passwordResetTokenMail(User user, String s, String token) {
        String url = s + "/savePassword?token=" + token;
        log.info("Click the link to reset your password : {} ",url);
        return url;
    }

    private void resendVerificationEmail(User user, String s, VerificationToken verificationToken) {
        String url = s + "/verifyRegistration?token=" + verificationToken.getToken();
        log.info("Click the link to verify your account : {} ",url);
    }

    private String applicationUrl(HttpServletRequest httpServletRequest) {
    return "http://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort() +httpServletRequest.getContextPath();
    }
}
