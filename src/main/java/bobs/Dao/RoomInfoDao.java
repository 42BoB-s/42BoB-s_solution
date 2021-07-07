package bobs.Dao;

import bobs.Dto.RoomInfoDto;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public interface RoomInfoDao {

	List<RoomInfoDto> vaildRoomSelect(RoomInfoDto roomInfoDto, String endTime);
	List<String> getAlarmRoomId();
	void roomStatusUpdate(int roomId, String status);
	RoomInfoDto getRoomInfoDto(int room_id);
	List<RoomInfoDto> findSameTimeRoomSelect(RoomInfoDto roomInfoDto, String endTime);


	String SQL_ROOMSEQ = "SELECT IFNULL(MAX(id) + 1, 1) FROM room_info";
	String SQL_ROOMINSERT = "INSERT INTO room_info (id, created_at, max_people, deadline, room_status, category_id, location_id) "
			+ "VALUES (?, NOW(), ?, date_format(?, '%Y-%m-%d %H:%i:%s'), ?, ?, ?)";
	String SQL_FINDVAILDROOM = "SELECT * FROM room_info"
			+" WHERE deadline > DATE_FORMAT(DATE_SUB(?, INTERVAL 1 HOUR), '%Y-%m-%d %H:%i:%s')"
			+" AND deadline <= DATE_FORMAT(?, '%Y-%m-%d %H:%i:%s')"
			+" AND room_status = 'active' AND location_id = ?";
	String WHERE_CATEGORY = " AND category_id = ?";
	String WHERE_ROOMINFO_ORDER = " ORDER BY created_at ASC";
	String SQL_FINDSAMETIMEROOM = "SELECT * FROM room_info"
			+" WHERE deadline >= DATE_FORMAT(DATE_SUB(?, INTERVAL 1 HOUR), '%Y-%m-%d %H:%i:%s')"
			+" AND deadline <= DATE_FORMAT(?, '%Y-%m-%d %H:%i:%s')"
			+" AND (room_status = 'active' OR room_status = 'succeed')";
	String SQL_ROOMSTATUSUPDATE = "UPDATE room_info SET room_status = ? WHERE id = ?";
	String SQL_FINDROOM = "SELECT * FROM room_info where id = ?";
	String SQL_FINDALARMROOM = "SELECT * FROM room_info " +
			"WHERE room_status = 'active' " +
			"AND deadline <  date_format(DATE_ADD(NOW(),INTERVAL 20 MINUTE), '%Y.%m.%d %H:%i:%s') " +
			"AND deadline >  date_format(NOW(), '%Y.%m.%d %H:%i:%s')";

	RowMapper<RoomInfoDto> rowMapper = (rs, rowNum) -> {
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
