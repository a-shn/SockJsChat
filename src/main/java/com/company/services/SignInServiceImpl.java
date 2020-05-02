package com.company.services;

import com.company.models.User;
import com.company.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class SignInServiceImpl implements SignInService {
    private UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    public SignInServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public Optional<User> signIn(String email, String password) {
        Optional<User> userOptional = usersRepository.findByEmail(email);
        if (userOptional.isPresent() && bCryptPasswordEncoder.matches(password, userOptional.get().getPassword())) {
            return userOptional;
        } else {
            return Optional.empty();
        }
    }
}
