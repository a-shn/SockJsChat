package com.company.repositories;

import com.company.models.Message;

import java.util.List;

public interface ChatRepository {
    List<Message> getLastMessages(long roomId, int limit);

    void save(Message message);
}
