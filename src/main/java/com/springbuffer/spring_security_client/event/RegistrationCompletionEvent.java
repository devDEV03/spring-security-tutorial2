package com.springbuffer.spring_security_client.event;

import com.springbuffer.spring_security_client.entity.User;
import jakarta.persistence.GeneratedValue;
import lombok.*;
import org.springframework.context.ApplicationEvent;


@Getter
@Setter
public class  RegistrationCompletionEvent extends ApplicationEvent {
    private User user;
    private String url;

    public RegistrationCompletionEvent(User user,String url) {
        super(user);
        this.url = url;
        this.user = user;
    }
}
