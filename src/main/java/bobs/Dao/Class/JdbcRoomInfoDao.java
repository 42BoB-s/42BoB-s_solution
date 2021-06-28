package bobs.Dao.Class;

import bobs.Dao.Dao;
import bobs.Dao.RoomInfoDao;
import bobs.Dto.RoomInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcRoomInfoDao implements Dao<RoomInfoDto>,RoomInfoDao {

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
	public List<RoomInfoDto> vaildRoomSelect(RoomInfoDto roomInfoDto, String startTime, String endTime){
		return jdbcTemplate.query(
				SQL_FINDVAILDROOM, rowMapper,
				startTime, endTime, roomInfoDto.getCategory_id(), roomInfoDto.getLocation_id());
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