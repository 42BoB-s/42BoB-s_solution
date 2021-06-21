package bobs.Service;

import bobs.Dto.RoomInfoDto;
import bobs.Dto.RoomMatchDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoomServiceImplTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private RoomService roomService;

	@Test
	public void findVaildRoom() {

		RoomInfoDto roomInfoDto = new RoomInfoDto();
		String startTime = "2021-06-21 18:00:00";
		String endTime = "2021-06-24 14:00:00";
		roomInfoDto.setCategory_id(3);
		roomInfoDto.setLocation_id(2);

		RoomMatchDto roomMatchDto = new RoomMatchDto();
		roomMatchDto.setUser_id("test2");

		List<RoomInfoDto> list = roomService.findVaildRoom(roomInfoDto, roomMatchDto, startTime, endTime);
		for (RoomInfoDto dto : list)
		{
			System.out.println("해당 방 내역 : " +
						"ID : " + dto.getId() + " " +
						"MAX_PEOPLE : " + 	dto.getMax_people() + " " +
						"DEADLINE : " + 	dto.getDeadline() + " " +
						"ROOMSTATUS : " + 	dto.getRoom_status() + " " +
						"CATEGORY_ID : " + 	dto.getCategory_id() + " " +
						"LOCATION_ID : " + 	dto.getLocation_id()
				);
		}
	}
}
