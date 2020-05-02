package com.company.controllers;

import com.company.services.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignUpController {
    @Autowired
    private SignUpService signUpService;

    @GetMapping("/signup")
    public String getSignUpPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(@RequestParam("login") String login, @RequestParam("email") String email,
                         @RequestParam("password") String password) {
        boolean isSuccessful = signUpService.signUp(login, email, password);
        if (isSuccessful) {
            return "redirect: /signin";
        }
        return "redirect: /signup?error";
    }
}
