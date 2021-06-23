package bobs.Service.dao;

import bobs.Dao.JdbcRoomInfoDao;
import bobs.Dao.JdbcRoomMatchDao;
import bobs.Dto.RoomInfoDto;
import bobs.Dto.RoomMatchDto;
import org.assertj.core.api.Assertions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class RoomMatchJDBCDaoTest {

    JdbcRoomInfoDao RoomInfoDao = new JdbcRoomInfoDao(new JdbcTemplate(mysqlDataSource()));
    JdbcRoomMatchDao RoomMatchDao = new JdbcRoomMatchDao(new JdbcTemplate(mysqlDataSource()));

    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setUrl("jdbc:mariadb://leeworld9.ipdisk.co.kr:53306/bobs_db?serverTimezone=Asia/Seoul");
        dataSource.setUsername("bobs_admin");
        dataSource.setPassword("bobs$@");

        return dataSource;
    }

    private String addUserName(int targetRoomId) {
        RoomMatchDto roomMatch = new RoomMatchDto();
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("mm:ss.sss");
        String uniqueKey = format.format(now);
        roomMatch.setRoom_id(targetRoomId);
        for (int i = 1; i < 5 ; i++) {
            roomMatch.setUser_id("("+i+") " + uniqueKey);
            RoomMatchDao.matchInsert(roomMatch);
        }
        return uniqueKey;
    }


    @org.junit.Test
    public void userNameListCheck() {
        RoomInfoDto roomInfo = new RoomInfoDto();
        List<String> roomIdList = new ArrayList<>();

        // 생성된 roomId에 유저를 4명 추가했을때, 그 id와 일치하는 유저 이름을 추출하는가
        int targetId = RoomInfoDao.testRoomInsert(roomInfo);
        roomIdList.add(String.valueOf(targetId));
        String uniqueKey = addUserName(targetId);
        Map<String, List<String>> resultMap =  RoomMatchDao.getAlarmUserId(roomIdList);

        int i = 1;
        for(String userName : resultMap.get(String.valueOf(targetId)) ) {
            Assertions.assertThat(userName).isEqualTo("("+i+") " + uniqueKey);
            i++;
        }
    }
}