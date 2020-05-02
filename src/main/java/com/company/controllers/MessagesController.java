package com.company.controllers;

import com.company.models.Message;
import com.company.repositories.ChatRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class MessagesController {
    @Autowired
    private ChatRepository chatRepository;

    @GetMapping("/getMessages")
    public ResponseEntity<List<Message>> getMessages(HttpServletRequest request) {
        long room = -1;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("room")) {
                room = Integer.parseInt(cookie.getValue());
            }
        }
        return ResponseEntity.ok(chatRepository.getLastMessages(room, 10));
    }
}

