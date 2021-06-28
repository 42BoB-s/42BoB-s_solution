package bobs.Service.dao;



import bobs.Dto.RoomInfoDto;
import org.assertj.core.api.Assertions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class RoomInfoDaoTest {

    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setUrl("");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }

    testDao DAO = new testDao(new JdbcTemplate(mysqlDataSource()));

    @org.junit.Test
    public void roomIdListCheck() {
        RoomInfoDto roomInfo = new RoomInfoDto();

        // 10분뒤에 deadline인 roominfo 생성
        int id = DAO.testRoomInsert(roomInfo);

        List<String> roomIdList = DAO.getAlarmRoomId();
        System.out.println("insert room id : " + id);
        System.out.print("finded room ids : ");
        Assertions.assertThat(id).isEqualTo(Integer.parseInt(roomIdList.get(roomIdList.size() - 1)));

        for (String x : roomIdList) {
            System.out.print(x + ",");
        }
        System.out.println();

    }

    @org.junit.Test
    public void roomStatusChangeCheck() {

        // room_info 에 row 하나 생성
        RoomInfoDto roomInfo = new RoomInfoDto();
        DAO.testRoomInsert(roomInfo);

        // room_info 에서 alarm후보 추출
        List<String> roomIdList = DAO.getAlarmRoomId();

        // status update 해주고
        DAO.roomStatusUpdate(roomIdList);

        List<String> roomStatusList = DAO.testFindRoom(roomIdList);
        for (String x : roomStatusList) {
            Assertions.assertThat(x).isEqualTo("succeed");
        }
    }

    class testDao {
        private final JdbcTemplate jdbcTemplate;
        public testDao(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        private final String SQL_ROOMSEQ = "SELECT IFNULL(MAX(id) + 1, 1) FROM room_info";
        private final String SQL_TESTcreate = "INSERT INTO room_info (id, created_at, max_people, deadline, room_status, category_id, location_id) "
                + "VALUES (?, NOW(), ?, date_format(DATE_ADD(NOW(),INTERVAL 10 MINUTE), '%Y.%m.%d %H:%i:%s'), ?, ?, ?)";
        private final String SQL_FINDALARMROOM = "SELECT * FROM room_info " +
                "WHERE room_status = 'active' " +
                "AND deadline <  date_format(DATE_ADD(NOW(),INTERVAL 20 MINUTE), '%Y.%m.%d %H:%i:%s') " +
                "AND deadline >  date_format(NOW(), '%Y.%m.%d %H:%i:%s')";
        private final String SQL_ROOMSTATUSUPDATE = "UPDATE room_info SET room_status = ? WHERE id = ?";
        private final String SQL_FINDROOM = "SELECT * FROM room_info where id = ?";
        public List<String> getAlarmRoomId() {

            List<String> roomIdList = new ArrayList<>();
            for ( RoomInfoDto ri: jdbcTemplate.query(SQL_FINDALARMROOM,rowMapper)) {
                roomIdList.add(String.valueOf(ri.getId()));
            }
            return roomIdList;
        }

        public void roomStatusUpdate(List<String> roomIdList){
            for (String id : roomIdList) {
                jdbcTemplate.update(SQL_ROOMSTATUSUPDATE, "succeed", id);
            }
        }

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

        public List<String> testFindRoom(List<String> roomIdList){

            List<String> roomStatusList = new ArrayList<>();
            for (String x : roomIdList) {
                for(RoomInfoDto ri : jdbcTemplate.query(SQL_FINDROOM,rowMapper, x)) {
                    roomStatusList.add(ri.getRoom_status());
                }
            }
            return roomStatusList;
        }

        private final RowMapper<RoomInfoDto> rowMapper = (rs, rowNum) -> {
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
}
