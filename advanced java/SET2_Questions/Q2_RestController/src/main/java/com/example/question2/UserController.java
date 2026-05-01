package com.example.question2;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UserController {

    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String email) {
        return "User Registered: " + name + " | Email: " + email;
    }
}
