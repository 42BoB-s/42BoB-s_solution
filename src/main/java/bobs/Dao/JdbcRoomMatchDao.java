package bobs.Dao;

import bobs.Dto.ActivityLogDto;
import bobs.Dto.RoomInfoDto;
import bobs.Dto.RoomMatchDto;
import bobs.domain.CanceledRoom;
import bobs.domain.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.lang.ref.PhantomReference;
import java.security.PrivilegedAction;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcRoomMatchDao implements RoomMatchDao {

	private final JdbcTemplate jdbcTemplate;
	private final JdbcActivityLogDao activityLogDao;
	private final String SQL_ROOMUSERCNT = "SELECT COUNT(*) FROM room_match where room_id = ?";
	private final String SQL_MATCHINSERT = "INSERT INTO room_match(room_id, user_id, enter_at) VALUES (?, ?, NOW())";
	private final String SQL_DUPLECOUNT = "SELECT COUNT(*) FROM room_match where room_id = ? AND user_id = ?";

	// tjeong add
	private final String SQL_FINDUSER = "SELECT * FROM room_match WHERE room_id = ?";

	//mtak add
	private final String SQL_FINDVALIDROOM = "SELECT * FROM room_match WHERE user_id = ?" +
			"AND enter_at < date_format(DATE_ADD(NOW(),INTERVAL 24 HOUR), '%Y.%m.%d %H:%i:%s') ORDER BY enter_at desc";/* +
				"AND enter_at > date_format(NOW(), '%Y.%m.%d %H:%i:%s')";*/
	private final String SQL_GETLOCATION = "SELECT * FROM location WHERE id = ?";
	private final String SQL_GETCATEGORY = "SELECT * FROM category WHERE id = ?";
	private final String SQL_GETROOMINFO = "SELECT * FROM room_info WHERE id = ?";
	private final String SQL_GETUSERID = "SELECT * FROM room_match WHERE room_id = ?";
	private final String SQL_DELROOM_MATCH = "DELETE FROM room_match WHERE user_id = ? AND room_id = ?";
	private final String SQL_UPDATE_ROOM_STATUS_DESTROYED = "UPDATE room_info SET room_status = 'destroyed' WHERE id = ?";
	
	@Autowired
	public JdbcRoomMatchDao(JdbcTemplate jdbcTemplate, JdbcActivityLogDao activityLogDao){
		this.jdbcTemplate = jdbcTemplate;
		this.activityLogDao = activityLogDao;
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

	/* *
	 * Mtak - Running Code
	 * */

	@Override
	public List<Room> findValidAll(String id) {
		return jdbcTemplate.query(SQL_FINDVALIDROOM, roomRowMapper(), id);
	}

	@Override
	public List<String> findParticipants(int room_id) {
		List<String> participantsList = new ArrayList<>();
		for (Member member : jdbcTemplate.query(SQL_GETUSERID, StrRowMapper("user_id"), room_id)) {
			participantsList.add(String.valueOf(member.getName()));
		}
		return participantsList;
	}

	@Override
	public List<String> deleteRoomMatch(CanceledRoom canceledRoom) {
		jdbcTemplate.update(SQL_DELROOM_MATCH, canceledRoom.getUser_id(), canceledRoom.getRoom_id());
		List<String> leftParticipants = findParticipants(canceledRoom.getRoom_id());
		if (leftParticipants.size() == 0)
			jdbcTemplate.update(SQL_UPDATE_ROOM_STATUS_DESTROYED, canceledRoom.getRoom_id());
		RoomInfoDto tmp = getRoomInfoDto(canceledRoom.getRoom_id());
		activityLogDao.leaveLog(updateActivityLogDto(canceledRoom, tmp, "room_exit"));
		return leftParticipants;
	}
	
	public ActivityLogDto updateActivityLogDto(CanceledRoom canceledRoom, RoomInfoDto tmp, String status) {
		ActivityLogDto LogDto = new ActivityLogDto();
		LogDto.setUser_id(canceledRoom.getUser_id());
		LogDto.setActivity_status(status);
		LogDto.setLocation_id(tmp.getLocation_id());
		return LogDto;
	}
	
	/* *
	 * Mtak - Util Code
	 * */

	static class Member{

		private String name;

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

	}

	private RowMapper<Member> StrRowMapper(String col) {
		return (rs, rowNum) -> {
			Member member = new Member();
			member.setName(rs.getString(col));
			return member;
		};
	}

	private RowMapper<Room> roomRowMapper() {
		return (rs, rowNum) -> {
			Room room = new Room();
			room.setEnter_at(rs.getString("enter_at"));
			room.setParticipants(findParticipants(rs.getInt("room_id")));
			RoomInfoDto tmp = getRoomInfoDto(rs.getInt("room_id"));
			room.setRoom_id(Integer.parseInt(tmp.getDeadline()));
			room.setLocation_name(findLocationById(tmp.getLocation_id()));
			room.setCategory_name(findCategoryById(tmp.getCategory_id()));
			return room;
		};
	}
	
	private RoomInfoDto getRoomInfoDto(int room_id) {
		return jdbcTemplate.query(SQL_GETROOMINFO, roomInfoRowMapper, room_id)
				.stream()
				.findAny()
				.get();
	}
	
	private String findLocationById(int id) {
		String location;

		List<Member> result = jdbcTemplate.query(SQL_GETLOCATION, StrRowMapper("name"), id);
		location = result.stream().findAny().get().getName();
		return location;
	}

	private String findCategoryById(int id) {
		String category;

		List<Member> result = jdbcTemplate.query(SQL_GETCATEGORY, StrRowMapper("name"), id);
		category = result.stream().findAny().get().getName();
		return category;
	}

	/* *
	 * 이 코드는 RoomInfoDao와 완전히 겹침.
	 * RoomInfoDao에서 이 부분 private라서 가져오지 못함.
	 * 테스트에서 사용되서 여기서는 public으로 명시함.
	* */

	public  RowMapper<RoomInfoDto> roomInfoRowMapper = (rs, rowNum) -> {
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