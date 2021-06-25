package bobs.repository;

import bobs.Dto.RoomInfoDto;
import bobs.domain.CanceledRoom;
import bobs.domain.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.List;

class JdbcTemplateRoomRepositoryTest {

	private final JdbcTemplate jdbcTemplate = new JdbcTemplate(mysqlDataSource());

	public DataSource mysqlDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
		dataSource.setUrl("jdbc:mariadb://leeworld9.ipdisk.co.kr:53306/bobs_db?serverTimezone=Asia/Seoul");
		dataSource.setUsername("bobs_admin");
		dataSource.setPassword("bobs$@");

		return dataSource;
	}

	private JdbcTemplateRoomRepository jdbcTemplateRoomRepository = new JdbcTemplateRoomRepository(mysqlDataSource());

	@Test
	void deleteRoomMatch() {
		CanceledRoom canceledRoom = new CanceledRoom();
		canceledRoom.setRoom_id(9);
		canceledRoom.setUser_id("testtesttest");
		List<String> test = jdbcTemplateRoomRepository.deleteRoomMatch(canceledRoom);
		System.out.println("리턴할 친구들은" + test.size());
		//status 변경 완료
	}

	@Test
	void leaveLog() {
		CanceledRoom canceledRoom = new CanceledRoom();
		canceledRoom.setRoom_id(9);
		canceledRoom.setUser_id("testtesttest");
		RoomInfoDto tmp = jdbcTemplate.query("SELECT * FROM room_info WHERE id = ?" , jdbcTemplateRoomRepository.roomInfoRowMapper, canceledRoom.getRoom_id())
				.stream()
				.findAny()
				.get();
		jdbcTemplateRoomRepository.leaveLog(canceledRoom.getUser_id(), "room_exit", tmp.getLocation_id());
	}
}