package com.company.handlers;

import com.company.models.User;
import com.company.repositories.SessionsRepository;
import com.company.services.Chatterbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class WebSocketHandlerImpl implements WebSocketHandler {
    @Autowired
    private Chatterbox chatterbox;

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        chatterbox.register(webSocketSession);
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        chatterbox.sendMessage(webSocketSession, webSocketMessage);
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        chatterbox.remove(webSocketSession);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
