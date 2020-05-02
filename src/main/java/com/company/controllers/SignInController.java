package com.company.controllers;

import com.company.models.User;
import com.company.services.SessionsService;
import com.company.services.SignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@Controller
public class SignInController {
    @Autowired
    private SignInService signInService;
    @Autowired
    private SessionsService sessionsService;

    @GetMapping("/signin")
    public String getSignInPage() {
        return "signin";
    }

    @PostMapping("/signin")
    public String signIn(HttpServletResponse response, @RequestParam("email") String email, @RequestParam("password") String password) {
        String sessionId = UUID.randomUUID().toString();
        Optional<User> userOptional = signInService.signIn(email, password);
        if (userOptional.isPresent()) {
            response.addCookie(new Cookie("sessionId", sessionId));
            sessionsService.registerSession(sessionId, userOptional.get());
            return "redirect: /chat";
        }
        return "redirect: /signin?error";
    }
}
