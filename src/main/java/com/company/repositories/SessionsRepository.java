package com.company.repositories;

import com.company.models.Session;
import com.company.models.User;

import java.util.Optional;

public interface SessionsRepository {
    void save(Session session);

    Optional<User> findUserBySession(String sessionId);
}
