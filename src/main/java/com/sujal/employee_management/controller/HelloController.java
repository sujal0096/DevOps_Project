package com.sujal.employee_management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "Welcome DevOps Champion!";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, DevOps!";
    }
}