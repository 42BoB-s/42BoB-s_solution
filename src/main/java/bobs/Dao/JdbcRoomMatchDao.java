package bobs.Dao;

import bobs.Dto.RoomInfoDto;
import bobs.Dto.RoomMatchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

@Repository
public class JdbcRoomMatchDao implements RoomMatchDao {

	private final JdbcTemplate jdbcTemplate;
	private final String SQL_ROOMUSERCNT = "SELECT COUNT(*) FROM room_match where room_id = ?";
	private final String SQL_MATCHINSERT = "INSERT INTO room_match(room_id, user_id, enter_at) VALUES (?, ?, NOW())";

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
			//d.printStackTrace();
			System.out.println("같은 방에 접근 할 수 없습니다.");
		}
		return chk;
	}
}
