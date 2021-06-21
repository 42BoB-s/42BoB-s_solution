package com.example.alarmscheduler.dao;


import com.example.alarmscheduler.dto.RoomInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RoomInfoJDBCDao{

    private final JdbcTemplate jdbcTemplate;

    // dohlee님 코드랑 일치시킴
    private final String SQL_ROOMSTATUSUPDATE = "UPDATE room_info SET room_status = ? WHERE id = ?";

    // 추가해야하는 SQL문
    private final String SQL_FINDALARMROOM = "SELECT * FROM room_info " +
            "WHERE room_status = 'active' " +
            "AND deadline <  date_format(DATE_ADD(NOW(),INTERVAL 20 MINUTE), '%Y.%m.%d %H:%i:%s') " +
            "AND deadline >  date_format(NOW(), '%Y.%m.%d %H:%i:%s')";
    private final String SQL_TEST = "SELECT * FROM room_info " +
            "WHERE deadline <=  date_format('2021/06/12 14:30:00', '%Y.%m.%d %H:%i:%s') " +
            "AND deadline >= date_format('2021/06/12 14:00:00', '%Y.%m.%d %H:%i:%s')" +
            "AND room_status = 'active'";


    @Autowired
    public RoomInfoJDBCDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /*알람 대상 room_id를 추출 */
    public List<String> getAlarmRoomId() {

        List<String> roomIdList = new ArrayList<>();
        for (RoomInfoDto ri : jdbcTemplate.query(SQL_TEST,rowMapper)) {
            roomIdList.add(String.valueOf(ri.getId()));
        }
        return roomIdList;
    }

    /*알람 대상 room_status를 succeed로 업데이트 */
    public void roomStatusUpdate(List<String> roomIdList){
        for (String id : roomIdList) {
            jdbcTemplate.update(SQL_ROOMSTATUSUPDATE, "succeed", id);
        }
    }

    RowMapper<RoomInfoDto> rowMapper = (rs, rowNum) -> {
        RoomInfoDto roomInfo = new RoomInfoDto();
        roomInfo.setId(rs.getInt("id"));
        roomInfo.setCreated_at(rs.getString("created_at"));
        roomInfo.setMax_people(rs.getInt("Max_people"));
        roomInfo.setDeadline(rs.getString("deadline"));
        roomInfo.setRoom_status(rs.getString("room_status"));
        roomInfo.setCategory_id(rs.getInt("category_id"));
        roomInfo.setLocation_id(rs.getInt("location_id"));
        return roomInfo;
    };
}
