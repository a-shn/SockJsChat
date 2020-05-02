package com.company.repositories;

import com.company.models.Message;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class ChatJdbcTemplateImpl implements ChatRepository {
    //language=SQL
    private static final String SQL_SELECT_LAST = "SELECT * FROM chat ORDER BY time DESC LIMIT (?)";
    //language=SQL
    private static final String SQL_INSERT = "INSERT INTO chat (\"from\", text, time) VALUES (?,?,?)";

    private JdbcTemplate jdbcTemplate;

    public ChatJdbcTemplateImpl(JdbcTemplate template) {
        this.jdbcTemplate = template;
    }

    private RowMapper<Message> messageRowMapper = (row, rowNumber) -> {
        String from = row.getString("from");
        String text = row.getString("text");
        LocalDateTime time = row.getTimestamp("time").toLocalDateTime();
        return new Message(from, text, time);
    };

    @Override
    public List<Message> getLastMessages(int limit) {
        return jdbcTemplate.query(SQL_SELECT_LAST, new Object[]{limit}, messageRowMapper);
    }

    @Override
    public void save(Message message) {
        int updRows = jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection
                    .prepareStatement(SQL_INSERT);
            statement.setString(1, message.getFrom());
            statement.setString(2, message.getText());
            statement.setTimestamp(3, Timestamp.valueOf(message.getTime()));
            return statement;
        });

        if (updRows == 0) {
            throw new IllegalArgumentException();
        }
    }
}
