package bobs.Service.dao;

import bobs.Dao.Class.*;
import bobs.Dto.RoomInfoDto;
import bobs.Dto.RoomMatchDto;
import org.assertj.core.api.Assertions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class RoomMatchJDBCBaseDaoTest {

    testDao RoomInfoDao = new testDao(new JdbcTemplate(mysqlDataSource()));
    JdbcRoomInfoDao roomInfoDao = new JdbcRoomInfoDao(new JdbcTemplate(mysqlDataSource()));
    JdbcActivityLogDao activityLogDao = new JdbcActivityLogDao(new JdbcTemplate(mysqlDataSource()));
    JdbcCategoryDao categoryDao = new JdbcCategoryDao(new JdbcTemplate(mysqlDataSource()));
    JdbcLocationDao locationDao = new JdbcLocationDao(new JdbcTemplate(mysqlDataSource()));
    JdbcRoomMatchDao RoomMatchDao = new JdbcRoomMatchDao(new JdbcTemplate(mysqlDataSource()), roomInfoDao, locationDao, categoryDao, activityLogDao);


    testDao Dao = new testDao(new JdbcTemplate(mysqlDataSource()));
    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setUrl("");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }

    @org.junit.Test
    public void validUserName() {
        RoomInfoDto roomInfo = new RoomInfoDto();
        List<String> roomIdList = new ArrayList<>();
        int targetId = RoomInfoDao.testRoomInsert(roomInfo);
        roomIdList.add(String.valueOf(targetId));
        addValideUserName(targetId);
        Map<String, List<String>> resultMap =  RoomMatchDao.getAlarmUserId(roomIdList);
    }

    @org.junit.Test
    public void randomUserName() {

        RoomInfoDto roomInfo = new RoomInfoDto();
        List<String> roomIdList = new ArrayList<>();

        // 생성된 roomId에 유저를 4명 추가했을때, 그 id와 일치하는 유저 이름을 추출하는가
        int targetId = RoomInfoDao.testRoomInsert(roomInfo);
        roomIdList.add(String.valueOf(targetId));

        String uniqueKey = addRandomUserName(targetId);

        Map<String, List<String>> resultMap =  RoomMatchDao.getAlarmUserId(roomIdList);

        int i = 1;
        for(String userName : resultMap.get(String.valueOf(targetId)) ) {
            Assertions.assertThat(userName).isEqualTo("("+i+") " + uniqueKey);
            i++;
        }
    }

    private void addValideUserName(int targetRoomId) {
        List<String> slackName = new ArrayList<>();
        slackName.add("tjeong");

        RoomMatchDto roomMatch = new RoomMatchDto();
        roomMatch.setRoom_id(targetRoomId);
        for (String x : slackName) {
            roomMatch.setUser_id(x);
            RoomMatchDao.create(roomMatch);
        }
    }

    private String addRandomUserName(int targetRoomId) {
        RoomMatchDto roomMatch = new RoomMatchDto();
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("mm:ss.sss");
        String uniqueKey = format.format(now);
        roomMatch.setRoom_id(targetRoomId);
        for (int i = 1; i < 5 ; i++) {
            roomMatch.setUser_id("("+i+") " + uniqueKey);
            RoomMatchDao.create(roomMatch);
        }
        return uniqueKey;
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
