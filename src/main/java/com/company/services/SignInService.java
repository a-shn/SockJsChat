package com.company.services;

import com.company.models.User;

import java.util.Optional;

public interface SignInService {
    Optional<User> signIn(String email, String password);
}
