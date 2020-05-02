package com.company.services;

import com.company.models.Session;
import com.company.models.User;
import com.company.repositories.SessionsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

public class SessionsServiceImpl implements SessionsService {
    private final int SESSION_LIFETIME = 6;
    @Autowired
    private SessionsRepository sessionsRepository;

    @Override
    public void registerSession(String session, User user) {
        LocalDateTime since = LocalDateTime.now();
        LocalDateTime to = since.plusDays(SESSION_LIFETIME);
        sessionsRepository.save(new Session(user.getUserId(), session, since, to));
    }

    @Override
    public Optional<User> getUserForSession(String session) {
        return sessionsRepository.findUserBySession(session);
    }
}
