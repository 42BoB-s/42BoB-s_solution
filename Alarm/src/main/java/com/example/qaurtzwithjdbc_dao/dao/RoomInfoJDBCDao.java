package com.example.qaurtzwithjdbc_dao.dao;

import com.example.qaurtzwithjdbc_dao.dto.RoomInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoomInfoJDBCDao{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RoomInfoJDBCDao(JdbcTemplate jdbcTemplate) {
        System.out.println("RoomInfoJDBCDao Constructed");
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> getAlarmRoomId() {
        /* Test Code */
        String sql = "SELECT * FROM room_info " +
                "WHERE deadline <  date_format('2021/06/18 14:10:00', '%Y.%m.%d %H:%i:%s') " +
                "AND deadline >  date_format('2021/06/18 13:50:00', '%Y.%m.%d %H:%i:%s')" +
                "AND room_status = 'active'";
        return getRoomIdList(sql);


          /* Service Code
         String sql = "SELECT * FROM room_info " +
                "WHERE deadline <  date_format(DATE_ADD(NOW(),INTERVAL 20 MINUTE), '%Y.%m.%d %H:%i:%s') " +
                "AND deadline >  date_format(NOW(), '%Y.%m.%d %H:%i:%s')" +
                "AND room_status = 'active'";
        return getRoomIdList(sql);
        */
    }

    public void roomStatusUpdate(List<String> roomIdList) {
        for (String id : roomIdList) {
            String sql = "update room_info set room_status = 'success' WHERE id = " + id;
            int updateCnt = jdbcTemplate.update(sql);
            System.err.println(updateCnt + " row updated(active -> success)");
        }
    }

    public List<RoomInfo> getAlarmRoomInfo() {
        /* Test Code */
        String sql = "SELECT * FROM room_info " +
                "WHERE deadline <  date_format('2021/06/18 14:10:00', '%Y.%m.%d %H:%i:%s') " +
                "AND deadline >  date_format('2021/06/18 13:50:00', '%Y.%m.%d %H:%i:%s')" +
                "AND room_status = 'active'";
        return jdbcTemplate.query(sql,rowMapper);

        /* Service Code
         String sql = "SELECT * FROM room_info " +
                "WHERE deadline <  date_format(DATE_ADD(NOW(),INTERVAL 20 MINUTE), '%Y.%m.%d %H:%i:%s') " +
                "AND deadline >  date_format(NOW(), '%Y.%m.%d %H:%i:%s')" +
                "AND room_status = 'active'";
        return jdbcTemplate.query(sql,rowMapper);
         */
    }

    RowMapper<RoomInfo> rowMapper = (rs,rowNum) -> {
        RoomInfo roomInfo = new RoomInfo();
        roomInfo.setId(rs.getInt("id"));
        roomInfo.setCreated_at(rs.getString("created_at"));
        roomInfo.setMax_people(rs.getInt("Max_people"));
        roomInfo.setDeadline(rs.getString("deadline"));
        roomInfo.setRoomStatus(rs.getString("room_status"));
        roomInfo.setCategory_id(rs.getString("category_id"));
        roomInfo.setLocation_id(rs.getInt("location_id"));
        return roomInfo;
    };

    private List<String> getRoomIdList(String sql) {
        List<String> roomIdList = new ArrayList<>();

        for (RoomInfo ri : jdbcTemplate.query(sql,rowMapper)) {
            roomIdList.add(String.valueOf(ri.getId()));
        }
        return roomIdList;
    }

}
