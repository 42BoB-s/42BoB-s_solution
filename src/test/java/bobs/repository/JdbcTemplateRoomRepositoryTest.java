package bobs.repository;

import bobs.Dao.Class.*;
import bobs.Dto.ActivityLogDto;
import bobs.Dto.RoomInfoDto;
import bobs.domain.CanceledRoom;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.List;

class JdbcTemplateRoomRepositoryTest {

	JdbcTemplate jdbcTemplate = new JdbcTemplate(mysqlDataSource());
	JdbcRoomInfoDao roomInfoDao = new JdbcRoomInfoDao(new JdbcTemplate(mysqlDataSource()));
	JdbcActivityLogDao activityLogDao = new JdbcActivityLogDao(new JdbcTemplate(mysqlDataSource()));
	JdbcCategoryDao categoryDao = new JdbcCategoryDao(new JdbcTemplate(mysqlDataSource()));
	JdbcLocationDao locationDao = new JdbcLocationDao(new JdbcTemplate(mysqlDataSource()));
	JdbcRoomMatchDao RoomMatchDao = new JdbcRoomMatchDao(new JdbcTemplate(mysqlDataSource()), roomInfoDao, locationDao, categoryDao, activityLogDao);

	public DataSource mysqlDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
		dataSource.setUrl("");
		dataSource.setUsername("");
		dataSource.setPassword("");

		return dataSource;
	}

	@Test
	void deleteRoomMatch() {
		CanceledRoom canceledRoom = new CanceledRoom();
		canceledRoom.setRoom_id(9);
		canceledRoom.setUser_id("testtesttest");
		List<String> test = RoomMatchDao.deleteRoomMatch(canceledRoom);
		System.out.println("리턴할 친구들은" + test.size());
		//status 변경 완료
	}

	@Test
	void leaveLog() {
		CanceledRoom canceledRoom = new CanceledRoom();
		canceledRoom.setRoom_id(9);
		canceledRoom.setUser_id("testtesttest");
		RoomInfoDto tmp = jdbcTemplate.query("SELECT * FROM room_info WHERE id = ?" , roomInfoDao.rowMapper, canceledRoom.getRoom_id())
				.stream()
				.findAny()
				.get();
		activityLogDao.create(updateActivityLogDto(canceledRoom, tmp, "room_exit"));
	}


	public ActivityLogDto updateActivityLogDto(CanceledRoom canceledRoom, RoomInfoDto tmp, String status) {
		ActivityLogDto LogDto = new ActivityLogDto();
		LogDto.setUser_id(canceledRoom.getUser_id());
		LogDto.setActivity_status(status);
		LogDto.setLocation_id(tmp.getLocation_id());
		return LogDto;
	}

}