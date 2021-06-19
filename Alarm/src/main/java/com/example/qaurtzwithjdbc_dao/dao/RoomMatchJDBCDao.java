package com.example.qaurtzwithjdbc_dao.dao;

import com.example.qaurtzwithjdbc_dao.dto.RoomMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RoomMatchJDBCDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RoomMatchJDBCDao(JdbcTemplate jdbcTemplate) {
        System.out.println("RoomMatchJDBCDao Constructed");
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<RoomMatch> rowMapper = (rs, rowNum) -> {
        RoomMatch roomMatch = new RoomMatch();
        roomMatch.setId(rs.getInt("room_id"));
        roomMatch.setUser_id(rs.getString("user_id"));
        roomMatch.setEnter_at(rs.getString("enter_at"));
        return roomMatch;
    };

    public Map<String, List<String>> getAlarmUserId(List<String> roomIdList) {
        Map<String, List<String>> userIdMap = new HashMap<>();
        for (String roomId : roomIdList) {
            String targetId = String.valueOf(roomId);
            String sql = "SELECT * FROM room_match WHERE room_id = " + targetId;
            userIdMap.put(targetId, getUserIdList(sql));
        }
        return userIdMap;
    }

    public Map<String, List<RoomMatch>> getAlarmRoomMatch(List<Integer> roomIdList) {
        /* Test Code */
        Map<String, List<RoomMatch>> matchMap = new HashMap<>();
        for (int roomId : roomIdList) {
            String targetId = String.valueOf(roomId);
            String sql = "SELECT * FROM room_match WHERE room_id = " + targetId;
            matchMap.put(targetId, jdbcTemplate.query(sql,rowMapper));
        }
        return matchMap;
    }

    private List<String> getUserIdList(String sql) {
        List<String> userIdList = new ArrayList<>();
        for (RoomMatch rm : jdbcTemplate.query(sql,rowMapper)) {
            userIdList.add(rm.getUser_id());
        }
        return userIdList;
    }
}
