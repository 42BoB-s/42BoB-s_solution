package bobs.dao;

import bobs.dto.RoomMatchDto;
import bobs.domain.CanceledRoom;
import bobs.domain.Room;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Map;

public interface RoomMatchDao {
	int roomUserCount(RoomMatchDto roomMatchDto);
	int roomDupleCount(RoomMatchDto roomMatchDto);
	Map<String, List<String>> getAlarmUserId(List<String> roomIdList);

	/* 추가된 함수 */
	List<Room> findByUserId(String id);
	List<String> findParticipants(int room_id);
	List<String> deleteRoomMatch(CanceledRoom canceledRoom);

	String SQL_ROOMUSERCNT = "SELECT COUNT(*) FROM room_match where room_id = ?";
	String SQL_MATCHINSERT = "INSERT INTO room_match(room_id, user_id, enter_at) VALUES (?, ?, NOW())";
	String SQL_DUPLECOUNT = "SELECT COUNT(*) FROM room_match where room_id = ? AND user_id = ?";
	String SQL_FINDUSER = "SELECT * FROM room_match WHERE room_id = ?";
	String SQL_FINDVALIDROOM = "SELECT * FROM room_match WHERE user_id = ? AND enter_at < DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 24 HOUR),'%Y.%m.%d %H:%i:%s') AND enter_at > DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -24 HOUR), '%Y.%m.%d %H:%i:%s')";
	String SQL_GETUSERID = "SELECT * FROM room_match WHERE room_id = ?";
	String SQL_DELROOM_MATCH = "DELETE FROM room_match WHERE user_id = ? AND room_id = ?";

	RowMapper<RoomMatchDto> rowMapper = (rs, rowNum) -> {
		RoomMatchDto roomMatch = new RoomMatchDto();
		roomMatch.setRoom_id(rs.getInt("room_id"));
		roomMatch.setUser_id(rs.getString("user_id"));
		roomMatch.setEnter_at(rs.getString("enter_at"));
		return roomMatch;
	};

}
