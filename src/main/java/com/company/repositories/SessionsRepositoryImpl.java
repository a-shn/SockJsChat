package com.company.repositories;

import com.company.models.Session;
import com.company.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public class SessionsRepositoryImpl implements SessionsRepository {
    @Autowired
    private UsersRepository usersRepository;
    private JdbcTemplate jdbcTemplate;
    //language=sql
    private final String SQL_SAVE = "INSERT INTO sessions (user_id, session, since, \"to\") VALUES (?,?,?,?)";
    //language=sql
    private final String SQL_FIND_BY_SESSION = "SELECT * FROM sessions WHERE session=? and \"to\">?";

    private RowMapper<Session> sessionsRowMapper = (row, rowNumber) -> {
        Long userId = row.getLong("user_id");
        String session = row.getString("session");
        LocalDateTime since = row.getTimestamp("since").toLocalDateTime();
        LocalDateTime to = row.getTimestamp("to").toLocalDateTime();

        return new Session(userId, session, since, to);
    };

    public SessionsRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Session session) {
        int updRows = jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection
                    .prepareStatement(SQL_SAVE);
            statement.setLong(1, session.getUserId());
            statement.setString(2, session.getSession());
            statement.setTimestamp(3, Timestamp.valueOf(session.getSince()));
            statement.setTimestamp(4, Timestamp.valueOf(session.getTo()));
            return statement;
        });

        if (updRows == 0) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Optional<User> findUserBySession(String session) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        try {
            Session sessionModel = jdbcTemplate.queryForObject(SQL_FIND_BY_SESSION, new Object[]{session, now}, sessionsRowMapper);
            return usersRepository.findById(sessionModel.getUserId());
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
