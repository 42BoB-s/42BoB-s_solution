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

    @Autowired
    public RoomInfoJDBCDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // dohlee님 코드랑 일치시킴
    private final String SQL_ROOMSEQ = "SELECT IFNULL(MAX(id) + 1, 1) FROM room_info";
    private final String SQL_ROOMSTATUSUPDATE = "UPDATE room_info SET room_status = ? WHERE id = ?";
    private final String SQL_FINDROOM = "SELECT * FROM room_info where id = ?";

    // 추가해야하는 SQL문
    private final String SQL_FINDALARMROOM = "SELECT * FROM room_info " +
            "WHERE room_status = 'active' " +
            "AND deadline <  date_format(DATE_ADD(NOW(),INTERVAL 20 MINUTE), '%Y.%m.%d %H:%i:%s') " +
            "AND deadline >  date_format(NOW(), '%Y.%m.%d %H:%i:%s')";

    /* for TestCase */
    private final String SQL_TESTcreate = "INSERT INTO room_info (id, created_at, max_people, deadline, room_status, category_id, location_id) "
            + "VALUES (?, NOW(), ?, date_format(DATE_ADD(NOW(),INTERVAL 10 MINUTE), '%Y.%m.%d %H:%i:%s'), ?, ?, ?)";
    private final String SQL_TESTgetstatus = "SELECT * FROM room_info WHERE id = ?";

    /*알람 대상 room_id를 추출 */
    public List<String> getAlarmRoomId() {

        List<String> roomIdList = new ArrayList<>();
        for ( RoomInfoDto ri: jdbcTemplate.query(SQL_FINDALARMROOM,rowMapper)) {
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

    /* for TestCase */
    public int testRoomInsert(RoomInfoDto roomInfo) {
        int chk = 0;
        roomInfo.setId(jdbcTemplate.queryForObject(SQL_ROOMSEQ, Integer.class));
        roomInfo.setMax_people(4);
        roomInfo.setRoom_status("active");
        roomInfo.setCategory_id(1);
        roomInfo.setLocation_id(1);

        chk = jdbcTemplate.update(SQL_TESTcreate,
                roomInfo.getId(),
                roomInfo.getMax_people(),
                roomInfo.getRoom_status(),
                roomInfo.getCategory_id(),
                roomInfo.getLocation_id());
        return roomInfo.getId();
    }

    /* for TestCase */
    public List<String> testFindRoom(List<String> roomIdList){

        List<String> roomStatusList = new ArrayList<>();
        for (String x : roomIdList) {
            for(RoomInfoDto ri : jdbcTemplate.query(SQL_FINDROOM,rowMapper, x)) {
                roomStatusList.add(ri.getRoom_status());
            }
        }
        return roomStatusList;
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
