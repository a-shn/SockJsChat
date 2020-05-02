package com.company.controllers;

import com.company.models.Room;
import com.company.repositories.RoomsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class ChatController {
    @Autowired
    private RoomsRepository roomsRepository;

    @GetMapping("/chat")
    public ModelAndView getChatPage() {
        List<Room> rooms = roomsRepository.getAllRooms();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("chats");
        modelAndView.addObject("rooms", rooms);
        return modelAndView;
    }

    @RequestMapping(value = "/chat", params = "room")
    public String joinChatRoom(@RequestParam String room, HttpServletResponse response) {
        response.addCookie(new Cookie("room", room));
        return "chat";
    }
}
