package bobs.Dao;

import bobs.Dto.RoomInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcRoomInfoDao implements RoomInfoDao {

	private final JdbcTemplate jdbcTemplate;
	private final String SQL_ROOMSEQ = "SELECT IFNULL(MAX(id) + 1, 1) FROM room_info";
	private final String SQL_ROOMINSERT = "INSERT INTO room_info (id, created_at, max_people, deadline, room_status, category_id, location_id) "
									+ "VALUES (?, NOW(), ?, date_format(?, '%Y-%m-%d %H:%i:%s'), ?, ?, ?)";
	private final String SQL_FINDVAILDROOM = "SELECT * FROM room_info WHERE deadline > date_format(?, '%Y-%m-%d %H:%i:%s') "
									+"AND deadline <= date_format(?, '%Y-%m-%d %H:%i:%s') "
									+"AND room_status = 'active' AND category_id = ? AND location_id = ? ORDER BY deadline ASC";
	private final String SQL_ROOMSTATUSUPDATE = "UPDATE room_info SET room_status = ? WHERE id = ?";
	private final String SQL_FINDROOM = "SELECT * FROM room_info where id = ?";

	/* tjeong add */
	private final String SQL_FINDALARMROOM = "SELECT * FROM room_info " +
			"WHERE room_status = 'active' " +
			"AND deadline <  date_format(DATE_ADD(NOW(),INTERVAL 20 MINUTE), '%Y.%m.%d %H:%i:%s') " +
			"AND deadline >  date_format(NOW(), '%Y.%m.%d %H:%i:%s')";
	private final String SQL_TESTcreate = "INSERT INTO room_info (id, created_at, max_people, deadline, room_status, category_id, location_id) "
			+ "VALUES (?, NOW(), ?, date_format(DATE_ADD(NOW(),INTERVAL 10 MINUTE), '%Y.%m.%d %H:%i:%s'), ?, ?, ?)";


	@Autowired
	public JdbcRoomInfoDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// 코드 안전성을 위해 추후 PrepareStatement 작업 필요. 또한 SQL 에러시 예외처리도 필요 (DataAccessException)
	@Override
	public int roomInsert(RoomInfoDto roomInfoDto) {
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
	public List<RoomInfoDto> vaildRoomSelect(RoomInfoDto roomInfoDto, String startTime, String endTime){
		return jdbcTemplate.query(
				SQL_FINDVAILDROOM, rowMapper,
				startTime, endTime, roomInfoDto.getCategory_id(), roomInfoDto.getLocation_id());
	}

	/*
	 * 스케줄러 서비스로직
	 *알람 대상 room_id를 추출
	 * */
	@Override
	public List<String> getAlarmRoomId() {

		List<String> roomIdList = new ArrayList<>();
		for ( RoomInfoDto ri: jdbcTemplate.query(SQL_FINDALARMROOM,rowMapper)) {
			roomIdList.add(String.valueOf(ri.getId()));
		}
		return roomIdList;
	}

	/*
	 * 스케줄러 서비스로직
	 *알람 대상 room_status를 succeed로 업데이트
	 * */
	@Override
	public void roomStatusUpdate(List<String> roomIdList){
		for (String id : roomIdList) {
			jdbcTemplate.update(SQL_ROOMSTATUSUPDATE, "succeed", id);
		}
	}

	/* TestCase */
	@Override
	public int testRoomInsert(RoomInfoDto roomInfo) {
		int chk = 0;
		roomInfo.setId(jdbcTemplate.queryForObject(SQL_ROOMSEQ, Integer.class));
		roomInfo.setMax_people(4);
		roomInfo.setRoom_status("active");
		roomInfo.setCategory_id(1);
		roomInfo.setLocation_id(1);

		chk = jdbcTemplate.update(SQL_TESTcreate,
				roomInfo.getId(),
				roomInfo.getMax_people(),
				roomInfo.getRoom_status(),
				roomInfo.getCategory_id(),
				roomInfo.getLocation_id());
		return roomInfo.getId();
	}

	/* TestCase */
	@Override
	public List<String> testFindRoom(List<String> roomIdList){

		List<String> roomStatusList = new ArrayList<>();
		for (String x : roomIdList) {
			for(RoomInfoDto ri : jdbcTemplate.query(SQL_FINDROOM,rowMapper, x)) {
				roomStatusList.add(ri.getRoom_status());
			}
		}
		return roomStatusList;
	}

	private final RowMapper<RoomInfoDto> rowMapper = (rs, rowNum) -> {
		RoomInfoDto roomInfo = new RoomInfoDto();
		roomInfo.setId(rs.getInt("id"));
		roomInfo.setCreated_at(rs.getString("created_at"));
		roomInfo.setMax_people(rs.getInt("Max_people"));
		roomInfo.setDeadline(rs.getString("deadline"));
		roomInfo.setRoom_status(rs.getString("room_status"));
		roomInfo.setCategory_id(rs.getInt("category_id"));
		roomInfo.setLocation_id(rs.getInt("location_id"));
		return roomInfo;
	};
}
