package com.example.alarmscheduler.dao;

import com.example.alarmscheduler.dto.RoomMatchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RoomMatchJDBCDao {

    private final JdbcTemplate jdbcTemplate;

    // 추가해야하는 SQL문
    private final String SQL_FINDUSER = "SELECT * FROM room_match WHERE room_id = ?";

    @Autowired
    public RoomMatchJDBCDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, List<String>> getAlarmUserId(List<String> roomIdList) {
        Map<String, List<String>> userIdMap = new HashMap<>();

        for (String roomId : roomIdList) {
            List<String> userIdList = new ArrayList<>();

            for (RoomMatchDto rm : jdbcTemplate.query(SQL_FINDUSER,rowMapper, roomId)) {
                userIdList.add(rm.getUser_id());
            }
            userIdMap.put(roomId, userIdList);
        }
        return userIdMap;
    }

    RowMapper<RoomMatchDto> rowMapper = (rs, rowNum) -> {
        RoomMatchDto roomMatch = new RoomMatchDto();
        roomMatch.setRoom_id(rs.getInt("room_id"));
        roomMatch.setUser_id(rs.getString("user_id"));
        roomMatch.setEnter_at(rs.getString("enter_at"));
        return roomMatch;
    };
}