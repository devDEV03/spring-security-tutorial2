package com.springbuffer.spring_security_client.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/api/hello")
    public String hello(){
        return "Hello,welcome to the API created by dev";
    }

}
