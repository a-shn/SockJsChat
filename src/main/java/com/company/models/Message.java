package com.company.models;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class Message {
    private String from;
    private String text;
    private long roomId;
    private LocalDateTime time;
}
