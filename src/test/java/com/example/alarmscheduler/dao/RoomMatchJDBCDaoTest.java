package com.example.alarmscheduler.dao;

import com.example.alarmscheduler.dto.RoomInfoDto;
import com.example.alarmscheduler.dto.RoomMatchDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


class RoomMatchJDBCDaoTest {

    RoomInfoJDBCDao RoomInfoDao = new RoomInfoJDBCDao(new JdbcTemplate(mysqlDataSource()));
    RoomMatchJDBCDao RoomMatchDao = new RoomMatchJDBCDao(new JdbcTemplate(mysqlDataSource()));

    private DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("...");
        dataSource.setUsername("...");
        dataSource.setPassword("...");

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


    @Test
    @DisplayName("roomId의 userName을 제대로 추출하는지 확인")
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