package com.company.services;

import com.company.models.User;

import java.util.Optional;

public interface SessionsService {
    void registerSession(String session, User user);

    Optional<User> getUserForSession(String session);
}
