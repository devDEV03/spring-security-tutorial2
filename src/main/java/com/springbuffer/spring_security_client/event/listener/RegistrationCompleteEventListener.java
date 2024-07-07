package com.springbuffer.spring_security_client.event.listener;

import com.springbuffer.spring_security_client.entity.User;
import com.springbuffer.spring_security_client.event.RegistrationCompletionEvent;
import com.springbuffer.spring_security_client.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompletionEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompletionEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(user,token);


        String url = event.getUrl() + "/verifyRegistration?token=" + token;

        log.info("Click the link to verify your account : {} ",url);


    }
}
