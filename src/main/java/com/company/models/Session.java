package com.company.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class Session {
    private Long userId;
    private String session;
    private LocalDateTime since;
    private LocalDateTime to;
}
