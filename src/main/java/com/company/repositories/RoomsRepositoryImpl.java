package com.company.repositories;

import com.company.models.Message;
import com.company.models.Room;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDateTime;
import java.util.List;

public class RoomsRepositoryImpl implements RoomsRepository {
    //language=SQL
    private static final String SQL_SELECT_ALL = "SELECT * FROM rooms";

    private JdbcTemplate jdbcTemplate;

    public RoomsRepositoryImpl(JdbcTemplate template) {
        this.jdbcTemplate = template;
    }

    private RowMapper<Room> roomsRowMapper = (row, rowNumber) -> {
        long roomId = row.getLong("room_id");
        String name = row.getString("name");
        return new Room(roomId, name);
    };

    @Override
    public List<Room> getAllRooms() {
        return jdbcTemplate.query(SQL_SELECT_ALL, roomsRowMapper);
    }
}
