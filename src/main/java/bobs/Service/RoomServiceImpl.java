package bobs.Service;

import bobs.Dao.JdbcRoomInfoDao;
import bobs.Dao.JdbcRoomMatchDao;
import bobs.Dto.RoomInfoDto;
import bobs.Dto.RoomMatchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

	private JdbcRoomInfoDao jdbcRoomInfoDao;
	private JdbcRoomMatchDao jdbcRoomMatchDao;

	@Autowired
	public RoomServiceImpl(JdbcRoomInfoDao jdbcRoomInfoDao, JdbcRoomMatchDao jdbcRoomMatchDao)
	{
		this.jdbcRoomInfoDao = jdbcRoomInfoDao;
		this.jdbcRoomMatchDao = jdbcRoomMatchDao;
	}


	@Override
	public int roomCreate(RoomInfoDto roomInfoDto) {
		int chk = jdbcRoomInfoDao.roomInsert(roomInfoDto);
		if (chk == 1)
			System.out.println("[[[ROOM INSERT OK]]]");
		else
			System.out.println("[[[ROOM INSERT FAIL]]]");
		return chk;
	}

	//roomMatchDto(임시)의 user_id는 나중에 세션에서 받아와서 처리해야함
	@Override
	public List<RoomInfoDto> findVaildRoom(RoomInfoDto roomInfoDto, RoomMatchDto roomMatchDto, String startTime, String endTime) {
		List<RoomInfoDto> result = jdbcRoomInfoDao.vaildRoomSelect(roomInfoDto, startTime, endTime);
		if (result.size() == 0) { // 방이 없는 경우
			System.out.println("[[[ROOM FIND FAIL]]]");
			// 방생성 및 참가
			roomInfoDto.setDeadline(endTime);
			roomMatchDto.setRoom_id(roomCreate(roomInfoDto));
			roomEnter(roomMatchDto);
		}
		else { // 방이 있는 경우
			System.out.println("[[[ROOM FIND OK]]]");
			//방참가
			roomMatchDto.setRoom_id(result.get(0).getId());
			roomEnter(roomMatchDto);
		}
		return result;
	}

	@Override
	public int roomUpdate(RoomInfoDto roomInfoDto) {
		int chk = jdbcRoomInfoDao.statusUpdate(roomInfoDto);
		if (chk == 1)
			System.out.println("[[[ROOM STATUS UPDATE OK]]]");
		else
			System.out.println("[[[ROOM INSERT UPDATE FAIL]]]");
		return chk;
	}

	@Override
	public int roomEnter(RoomMatchDto roomMatchDto) {
		int chk = 0;
		RoomInfoDto status = new RoomInfoDto();
		status.setId(roomMatchDto.getRoom_id());
		if (jdbcRoomMatchDao.roomUserCount(roomMatchDto) <= 3)
			chk = jdbcRoomMatchDao.matchInsert(roomMatchDto);
		if (jdbcRoomMatchDao.roomUserCount(roomMatchDto) == 4)
		{
			status.setRoom_status("succeed");
			status.setId(roomMatchDto.getRoom_id());
			jdbcRoomInfoDao.statusUpdate(status);
		}
		if (chk == 1)
			System.out.println("[[[ROOM ENTER OK]]]");
		else
			System.out.println("[[[ROOM ENTER FAIL]]]");
		return chk;
	}


}
