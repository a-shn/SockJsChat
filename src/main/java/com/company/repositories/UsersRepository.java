package com.company.repositories;

import com.company.models.User;

import java.util.Optional;

public interface UsersRepository {
    void save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findById(long id);
}
