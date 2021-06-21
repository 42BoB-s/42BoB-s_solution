package bobs.Dao;

import bobs.Dto.RoomMatchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcRoomMatchDao implements RoomMatchDao {

	private final JdbcTemplate jdbcTemplate;
	private final String SQL_ROOMUSERCNT = "SELECT COUNT(*) FROM room_match where room_id = ?";
	private final String SQL_MATCHINSERT = "INSERT INTO room_match(room_id, user_id, enter_at) VALUES (?, ?, NOW())";
	private final String SQL_DUPLECOUNT = "SELECT COUNT(*) FROM room_match where room_id = ? AND user_id = ?";

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
}
