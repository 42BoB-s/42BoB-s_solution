package com.example.alarmscheduler.dao;


import com.example.alarmscheduler.dto.RoomInfoDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Transactional
public class RoomInfoDaoTest {

    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("...");
        dataSource.setUsername("...");
        dataSource.setPassword("...");

        return dataSource;
    }

    RoomInfoJDBCDao DAO = new RoomInfoJDBCDao(new JdbcTemplate(mysqlDataSource()));

    @Test
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

    @Test
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
}
