package com.company.services;

import com.company.models.Message;
import com.company.models.Room;
import com.company.models.User;
import com.company.repositories.ChatRepository;
import com.company.repositories.SessionsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class Chatterbox {
    @Autowired
    private SessionsRepository sessionsRepository;
    @Autowired
    private ChatRepository chatRepository;
    private ObjectMapper objectMapper;
    private Map<String, User> users;
    private Map<String, WebSocketSession> sessions;
    private Map<Long, List<String>> rooms;

    public Chatterbox() {
        sessions = new HashMap<>();
        users = new HashMap<>();
        rooms = new HashMap<>();
        objectMapper = new ObjectMapper();
    }

    public void register(WebSocketSession webSocketSession) {
        String sessionId = getSessionId(webSocketSession);
        long roomId = getRoomId(webSocketSession);
        Optional<User> userOptional = sessionsRepository.findUserBySession(sessionId);
        if (userOptional.isPresent()) {
            users.put(sessionId, userOptional.get());
            sessions.put(sessionId, webSocketSession);
            if (rooms.get(roomId) == null) {
                rooms.put(roomId, new ArrayList<>());
            }
            if (!rooms.get(roomId).contains(sessionId)) {
                rooms.get(roomId).add(sessionId);
            }
        }
    }

    @SneakyThrows
    public void sendMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) {
        String sessionId = getSessionId(webSocketSession);
        long roomId = getRoomId(webSocketSession);
        User user = users.get(sessionId);
        String text = (String) webSocketMessage.getPayload();
        LocalDateTime now = LocalDateTime.now();
        Message message = new Message(user.getEmail(), text, roomId, now);
        String json = objectMapper.writeValueAsString(message);
        WebSocketMessage<String> socketMessage = new TextMessage(json);
        for (String session: rooms.get(roomId)) {
            sessions.get(session).sendMessage(socketMessage);
        }
        chatRepository.save(message);
    }

    public void remove(WebSocketSession webSocketSession) {
        String sessionId = getSessionId(webSocketSession);
        long roomId = getRoomId(webSocketSession);
        sessions.remove(sessionId);
        users.remove(sessionId);
        rooms.get(roomId).remove(sessionId);
    }

    private int getRoomId(WebSocketSession webSocketSession) {
        String[] cookies = webSocketSession.getHandshakeHeaders().get("cookie").get(0).split("; ");
        String roomId = null;
        for (String cookie : cookies) {
            String key = cookie.split("=")[0];
            String value = cookie.split("=")[1];
            if (key.equals("room")) {
                roomId = value;
            }
        }
        return Integer.parseInt(roomId);
    }

    private String getSessionId(WebSocketSession webSocketSession) {
        String[] cookies = webSocketSession.getHandshakeHeaders().get("cookie").get(0).split(";");
        String sessionId = null;
        for (String cookie : cookies) {
            String key = cookie.split("=")[0];
            String value = cookie.split("=")[1];
            if (key.equals("sessionId")) {
                sessionId = value;
            }
        }
        return sessionId;
    }
}
