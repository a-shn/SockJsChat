package com.company.repositories;

import com.company.models.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.util.Optional;

public class UsersJdbcTemplateImpl implements UsersRepository {
    //language=SQL
    private static final String SQL_SELECT_BY_EMAIL = "SELECT * FROM users WHERE email = (?)";
    //language=SQL
    private static final String SQL_INSERT = "INSERT INTO users (nickname, email, password) VALUES (?,?,?)";
    //language=sql
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM users WHERE user_id=?";

    private JdbcTemplate jdbcTemplate;

    public UsersJdbcTemplateImpl(JdbcTemplate template) {
        this.jdbcTemplate = template;
    }

    private RowMapper<User> userRowMapper = (row, rowNumber) -> {
        long userId = row.getLong("user_id");
        String login = row.getString("nickname");
        String email = row.getString("email");
        String password = row.getString("password");

        return new User(userId, login, email, password);
    };


    @Override
    public void save(User user) {
        int updRows = jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection
                    .prepareStatement(SQL_INSERT);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            return statement;
        });

        if (updRows == 0) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            User user = jdbcTemplate.queryForObject(SQL_SELECT_BY_EMAIL, new Object[]{email}, userRowMapper);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findById(long id) {
        try {
            User user = jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, new Object[]{id}, userRowMapper);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
