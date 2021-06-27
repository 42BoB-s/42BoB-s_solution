package bobs.Service;

import bobs.Dao.JdbcRoomInfoDao;
import bobs.Dao.JdbcRoomMatchDao;
import bobs.Dto.RoomInfoDto;
import bobs.Dto.RoomMatchDto;
import bobs.Slack.Slack;
import bobs.domain.CanceledRoom;
import bobs.domain.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class RoomServiceImpl implements RoomService {

	private JdbcRoomInfoDao jdbcRoomInfoDao;
	private JdbcRoomMatchDao jdbcRoomMatchDao;
	private JdbcRoomMatchDao RoomMatchDao;

	@Autowired
	public RoomServiceImpl(JdbcRoomInfoDao jdbcRoomInfoDao, JdbcRoomMatchDao jdbcRoomMatchDao, JdbcRoomMatchDao RoomMatchDao)
	{
		this.jdbcRoomInfoDao = jdbcRoomInfoDao;
		this.jdbcRoomMatchDao = jdbcRoomMatchDao;
		this. RoomMatchDao = RoomMatchDao;
	}


	@Override
	public int roomCreate(RoomInfoDto roomInfoDto) {
		int room_id = jdbcRoomInfoDao.roomInsert(roomInfoDto);
		if (room_id != 0)
			System.out.println("[[[ROOM INSERT OK]]]");
		else
			System.out.println("[[[ROOM INSERT FAIL]]]");
		return room_id;
	}


	@Override
	public boolean roomCountCheck(RoomMatchDto roomMatchDto) {
		int count = jdbcRoomMatchDao.roomUserCount(roomMatchDto);
		if (count < 4)
			return true;
		else
			return false;
	}

	@Override
	public boolean userDupleCheck(RoomMatchDto roomMatchDto) {
		int count = jdbcRoomMatchDao.roomDupleCount(roomMatchDto);

		if (count == 1)
			return true;
		else
			return false;
	}

	//roomMatchDto(임시)의 user_id는 나중에 세션에서 받아와서 처리해야함
	@Override
	public List<RoomInfoDto> findVaildRoom(RoomInfoDto roomInfoDto, RoomMatchDto roomMatchDto, String startTime, String endTime) {
		List<RoomInfoDto> result = new ArrayList<>();
		List<RoomInfoDto> tmpList = jdbcRoomInfoDao.vaildRoomSelect(roomInfoDto, startTime, endTime);
		for (RoomInfoDto dto : tmpList) {
			roomMatchDto.setRoom_id(dto.getId());
			if (roomCountCheck(roomMatchDto)) { // 4명 미만의 방인지 체크
				if (!userDupleCheck(roomMatchDto))  // 4명미만의 방이나, 이미 내가 참가한 곳이 있을수 있으니 체크
					result.add(dto);
			}
		}
		if (result.size() == 0) { // 방이 없는 경우
			System.out.println("[[[ROOM FIND FAIL]]]");
			// 방생성
			roomInfoDto.setDeadline(endTime);
			roomMatchDto.setRoom_id(roomCreate(roomInfoDto));
		} else { // 방이 있는 경우 ( 4명인 방 제외 )
			System.out.println("[[[ROOM FIND OK]]]");
			roomMatchDto.setRoom_id(result.get(0).getId());
		}
		//방참가
		roomEnter(roomMatchDto);
		return result;
	}

	@Override
	public int roomEnter(RoomMatchDto roomMatchDto) {
		int chk = 0;
		RoomInfoDto status = new RoomInfoDto();
		status.setId(roomMatchDto.getRoom_id());
		chk = jdbcRoomMatchDao.matchInsert(roomMatchDto);
		if (chk == 1)
			System.out.println("[[[ROOM ENTER OK]]]");
		else
			System.out.println("[[[ROOM ENTER FAIL]]]");
		return chk;
	}

	//화면에 보여줄 방 조회
	@Override
	public List<Room> findRooms(String id) {
		return RoomMatchDao.findValidAll(id);
	}

	//방 취소
	@Override
	public void cancelRoom(CanceledRoom canceledRoom) {
		Slack slack = new Slack();
		List<String> leftParticipants = RoomMatchDao.deleteRoomMatch(canceledRoom);
		slack.sendCancelMsg(leftParticipants);
	}
}
