package bobs.dao.classes;

import bobs.dao.BaseDao;
import bobs.dao.RoomInfoDao;
import bobs.dto.RoomInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcRoomInfoDao implements BaseDao<RoomInfoDto>,RoomInfoDao {

	private final JdbcTemplate jdbcTemplate;

	// 코드 안전성을 위해 추후 PrepareStatement 작업 필요. 또한 SQL 에러시 예외처리도 필요 (DataAccessException)
	@Override
	public int create(RoomInfoDto roomInfoDto) {
		int chk = 0;
		roomInfoDto.setId(jdbcTemplate.queryForObject(SQL_ROOMSEQ, Integer.class));
		roomInfoDto.setMax_people(4);
		roomInfoDto.setRoom_status("active");

		chk = jdbcTemplate.update(SQL_ROOMINSERT,
				roomInfoDto.getId(),
				roomInfoDto.getMax_people(),
				roomInfoDto.getDeadline(),
				roomInfoDto.getRoom_status(),
				roomInfoDto.getCategory_id(),
				roomInfoDto.getLocation_id());
		if (chk != 0)
			return roomInfoDto.getId();
		else
			return chk;
	}

	@Override
	public List<RoomInfoDto> findById(int roomId) {
		return jdbcTemplate.query(SQL_FINDROOM, rowMapper, roomId);
	}

	@Override
	public RoomInfoDto getRoomInfoDto(int roomId) {
		return findById(roomId).stream().findAny().get();
	}

	@Override
	public List<RoomInfoDto> findSameTimeRoomSelect(RoomInfoDto roomInfoDto, String endTime) {
		return jdbcTemplate.query(
				SQL_FINDSAMETIMEROOM, rowMapper,
				endTime, endTime);
	}

	@Override
	public List<RoomInfoDto> vaildRoomSelect(RoomInfoDto roomInfoDto, String endTime){
		String NEW_SQL_FINDVAILDROOM = SQL_FINDVAILDROOM;
		//0 은 '모두' 카테고리
		if(roomInfoDto.getCategory_id() == 0) {
			NEW_SQL_FINDVAILDROOM += WHERE_ROOMINFO_ORDER;
			System.out.println(NEW_SQL_FINDVAILDROOM);
			return jdbcTemplate.query(
					NEW_SQL_FINDVAILDROOM, rowMapper,
					endTime, endTime, roomInfoDto.getLocation_id());
		}
		else {
			NEW_SQL_FINDVAILDROOM += (WHERE_CATEGORY + WHERE_ROOMINFO_ORDER);
			System.out.println(NEW_SQL_FINDVAILDROOM);
			return jdbcTemplate.query(
					NEW_SQL_FINDVAILDROOM, rowMapper,
					endTime, endTime, roomInfoDto.getLocation_id(), roomInfoDto.getCategory_id());
		}
	}

	@Override
	public List<String> getAlarmRoomId() {
		List<String> roomIdList = new ArrayList<>();

		for ( RoomInfoDto ri: jdbcTemplate.query(SQL_FINDALARMROOM,rowMapper)) {
			roomIdList.add(String.valueOf(ri.getId()));
		}
		return roomIdList;
	}

	@Override
	public void roomStatusUpdate(int id, String status){
			jdbcTemplate.update(SQL_ROOMSTATUSUPDATE, status, id);
	}
}