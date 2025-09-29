package com.jobtracker.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/admin/auth")
public class AdminLoginController {

    private static final String ADMIN_EMAIL = "admin@jobtracker.com";
    private static final String ADMIN_PASSWORD = "admin123";

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        if (ADMIN_EMAIL.equals(email) && ADMIN_PASSWORD.equals(password)) {
            return "Admin login successful";
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid admin credentials");
        }
    }
}
