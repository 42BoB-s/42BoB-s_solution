package bobs.repository;

import bobs.Dto.RoomInfoDto;
import bobs.domain.CanceledRoom;
import bobs.domain.Room;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcTemplateRoomRepository implements RoomRepository {

	private final JdbcTemplate jdbcTemplate;

	public JdbcTemplateRoomRepository(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Room> findValidAll(String id) {
		String sql = "SELECT * FROM room_match WHERE user_id = ?" +
				"AND enter_at < date_format(DATE_ADD(NOW(),INTERVAL 24 HOUR), '%Y.%m.%d %H:%i:%s')";/* +
				"AND enter_at > date_format(NOW(), '%Y.%m.%d %H:%i:%s')";*/
		return jdbcTemplate.query(sql, roomRowMapper(), id);
	}


	@Override
	public List<String> deleteRoomMatch(CanceledRoom canceledRoom) {
		String sql = "DELETE FROM room_match WHERE user_id = ? AND room_id = ?";
		jdbcTemplate.update(sql, canceledRoom.getUser_id(), canceledRoom.getRoom_id());
		List<String> leftParticipants = findParticipants(canceledRoom.getRoom_id());
		if (leftParticipants.size() == 0) {
			sql = "UPDATE room_info SET room_status = 'destroyed' WHERE id = ?";
			jdbcTemplate.update(sql, canceledRoom.getRoom_id());
		}
		RoomInfoDto tmp = jdbcTemplate.query("SELECT * FROM room_info WHERE id = " +
				canceledRoom.getRoom_id(), roomInfoRowMapper)
				.stream()
				.findAny()
				.get();
		leaveLog(canceledRoom.getUser_id(), "room_exit", tmp.getLocation_id());
		return leftParticipants;
	}

	public void leaveLog(String user_id, String activity_status, int location_id) {
		String sql = "INSERT INTO activity_log(activity_status,location_id, created_at, user_id) VALUES(?, ?, NOW() , ?)";
		jdbcTemplate.update(sql, activity_status, location_id, user_id);
	}

	public RowMapper<Room> roomRowMapper() {
		return (rs, rowNum) -> {
			Room room = new Room();
			room.setEnter_at(rs.getString("enter_at"));
			room.setRoom_id(rs.getInt("room_id"));

			room.setParticipants(findParticipants(rs.getInt("room_id")));

			RoomInfoDto tmp = jdbcTemplate.query("SELECT * FROM room_info WHERE id = " +
					rs.getInt("room_id"), roomInfoRowMapper)
					.stream()
					.findAny()
					.get();
			room.setLocation_name(findLocationById(tmp.getLocation_id()));
			room.setCategory_name(findCategoryById(tmp.getCategory_id()));
			return room;
		};
	}

	RowMapper<RoomInfoDto> roomInfoRowMapper = (rs, rowNum) -> {
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

	public List<String> findParticipants(int room_id) {
		List<String> participantsList = new ArrayList<>();
		String sql = "SELECT * FROM room_match WHERE room_id = ?";
		for (Member member : jdbcTemplate.query(sql, StrRowMapper("user_id"), room_id)) {
			participantsList.add(String.valueOf(member.getName()));
		}
		return participantsList;
	}

	private RowMapper<Member> StrRowMapper(String col) {
		return (rs, rowNum) -> {
			Member member = new Member();
			member.setName(rs.getString(col));
			return member;
		};
	}

	static class Member{

		private String name;

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

	}

	private RowMapper<Member> IntRowMapper(String col) {
		return (rs, rowNum) -> {
			Member member = new Member();
			member.setName(rs.getString(col));
			return member;
		};
	}

	private String findLocationById(int id) {
		String location;

		List<Member> result = jdbcTemplate.query("SELECT * FROM location WHERE id = " + id, StrRowMapper("name"));
		location = result.stream().findAny().get().getName();
		return location;
	}

	private String findCategoryById(int id) {
		String category;

		List<Member> result = jdbcTemplate.query("SELECT * FROM category WHERE id = " + id, StrRowMapper("name"));
		category = result.stream().findAny().get().getName();
		return category;
	}
}