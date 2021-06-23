package bobs.Dao;

import bobs.Dto.RoomMatchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcRoomMatchDao implements RoomMatchDao {

	private final JdbcTemplate jdbcTemplate;
	private final String SQL_ROOMUSERCNT = "SELECT COUNT(*) FROM room_match where room_id = ?";
	private final String SQL_MATCHINSERT = "INSERT INTO room_match(room_id, user_id, enter_at) VALUES (?, ?, NOW())";
	private final String SQL_DUPLECOUNT = "SELECT COUNT(*) FROM room_match where room_id = ? AND user_id = ?";

	// tjeong add
	private final String SQL_FINDUSER = "SELECT * FROM room_match WHERE room_id = ?";

	@Autowired
	public JdbcRoomMatchDao(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public int roomUserCount(RoomMatchDto roomMatchDto){
		return jdbcTemplate.queryForObject(SQL_ROOMUSERCNT, Integer.class, roomMatchDto.getRoom_id());
	}

	@Override
	public int matchInsert(RoomMatchDto roomMatchDto) {
		int chk = 0;
		try {
			chk = jdbcTemplate.update(SQL_MATCHINSERT,
					roomMatchDto.getRoom_id(),
					roomMatchDto.getUser_id());
		} catch (DuplicateKeyException d) {
			d.printStackTrace();
		}
		return chk;
	}

	@Override
	public int roomDupleCount(RoomMatchDto roomMatchDto) {
		return jdbcTemplate.queryForObject(SQL_DUPLECOUNT, Integer.class, roomMatchDto.getRoom_id(), roomMatchDto.getUser_id());
	}

	/* 스케줄러 서비스로직
	 *  roomIdList에 일치하는 userId 추출하여 Map(roomId, List<userName>) 형식으로 추출
	 * */

	@Override
	public Map<String, List<String>> getAlarmUserId(List<String> roomIdList) {
		Map<String, List<String>> userIdMap = new HashMap<>();

		for (String roomId : roomIdList) {
			List<String> userIdList = new ArrayList<>();

			for (RoomMatchDto rm : jdbcTemplate.query(SQL_FINDUSER,rowMapper, roomId)) {
				userIdList.add(rm.getUser_id());
			}
			userIdMap.put(roomId, userIdList);
		}
		return userIdMap;
	}

	private final RowMapper<RoomMatchDto> rowMapper = (rs, rowNum) -> {
		RoomMatchDto roomMatch = new RoomMatchDto();
		roomMatch.setRoom_id(rs.getInt("room_id"));
		roomMatch.setUser_id(rs.getString("user_id"));
		roomMatch.setEnter_at(rs.getString("enter_at"));
		return roomMatch;
	};
}
