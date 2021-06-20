package bobs.Dao;

import bobs.Dto.RoomInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;

@Repository
public class JdbcRoomInfoDao implements RoomInfoDao {

	private final JdbcTemplate jdbcTemplate;
	private final String SQL_ROOMSEQ = "SELECT IFNULL(MAX(id) + 1, 1) FROM room_info";
	private final String SQL_ROOMINSERT = "INSERT INTO room_info (id, created_at, max_people, deadline, room_status, category_id, location_id) "
									+ "VALUES (?, NOW(), ?, date_format(?, '%Y-%m-%d %H:%i:%s'), ?, ?, ?)";
	private final String SQL_FINDVAILDROOM = "SELECT * FROM room_info WHERE deadline > date_format(?, '%Y-%m-%d %H:%i:%s') "
									+"AND deadline <= date_format(?, '%Y-%m-%d %H:%i:%s') AND room_status = 'active' "
									+"AND category_id = ? AND location_id = ? ORDER BY deadline ASC";
	private final String SQL_ROOMSTATUSUPDATE = "UPDATE room_info SET room_status = ? WHERE id = ?";
	private final String SQL_FINDROOM = "SELECT * FROM room_info where id = ?";

	@Autowired
	public JdbcRoomInfoDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// 코드 안전성을 위해 추후 PrepareStatement 작업 필요. 또한 SQL 에러시 예외처리도 필요 (DataAccessException 사용?)
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
				SQL_FINDVAILDROOM
				,(rs, rowNum) -> new RoomInfoDto (
						rs.getInt("id"),
						rs.getString("created_at"),
						rs.getInt("max_people"),
						rs.getString("deadline"),
						rs.getString("room_status"),
						rs.getInt("category_id"),
						rs.getInt("location_id"))
				, startTime, endTime, roomInfoDto.getCategory_id(), roomInfoDto.getLocation_id());
	}

	@Override
	public int statusUpdate(RoomInfoDto roomInfoDto){
		return jdbcTemplate.update(SQL_ROOMSTATUSUPDATE, roomInfoDto.getRoom_status(), roomInfoDto.getId());
	}

	@Override
	public RoomInfoDto roomSelect(RoomInfoDto roomInfoDto){
		return jdbcTemplate.queryForObject(
				SQL_FINDROOM,
				new Object[] {roomInfoDto.getId()},
				(rs, rowNum) -> new RoomInfoDto (
						rs.getInt("id"),
						rs.getString("created_at"),
						rs.getInt("max_people"),
						rs.getString("deadline"),
						rs.getString("room_status"),
						rs.getInt("category_id"),
						rs.getInt("location_id")
				));
	}
}
