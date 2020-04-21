package com.company.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class User {
    private Long userId;
    private String login;
    private String email;
    private String password;
}
