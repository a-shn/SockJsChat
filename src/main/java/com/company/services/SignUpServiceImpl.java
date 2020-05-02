package com.company.services;

import com.company.models.User;
import com.company.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SignUpServiceImpl implements SignUpService {
    private UsersRepository usersRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public SignUpServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public boolean signUp(String login, String email, String password) {
        if (usersRepository.findByEmail(email).isEmpty()) {
            User user = new User(null, login, email, bCryptPasswordEncoder.encode(password));
            usersRepository.save(user);
            return true;
        } else {
            return false;
        }
    }
}
