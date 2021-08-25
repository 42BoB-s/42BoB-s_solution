package bobs.dao.classes;

import bobs.dao.BaseDao;
import bobs.dao.RoomMatchDao;
import bobs.dto.ActivityLogDto;
import bobs.dto.BaseDto;
import bobs.dto.RoomInfoDto;
import bobs.dto.RoomMatchDto;
import bobs.domain.CanceledRoom;
import bobs.domain.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcRoomMatchDao implements BaseDao<RoomMatchDto>, RoomMatchDao {

	private final JdbcTemplate jdbcTemplate;
	private final JdbcRoomInfoDao roomInfoDao;
	private final JdbcLocationDao locationDao;
	private final JdbcCategoryDao categoryDao;
	private final JdbcActivityLogDao activityLogDao;

	@Override
	public int create(RoomMatchDto roomMatchDto) {
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
	public List<RoomMatchDto> findById(int roomId) {
		return  jdbcTemplate.query(SQL_FINDUSER, rowMapper, roomId);
	}

	@Override
	public int roomUserCount(RoomMatchDto roomMatchDto) {
		return jdbcTemplate.queryForObject(SQL_ROOMUSERCNT, Integer.class, roomMatchDto.getRoom_id());
	}


	@Override
	public int roomDupleCount(RoomMatchDto roomMatchDto) {
		return jdbcTemplate.queryForObject(SQL_DUPLECOUNT, Integer.class, roomMatchDto.getRoom_id(), roomMatchDto.getUser_id());
	}

	@Override
	public Map<String, List<String>> getAlarmUserId(List<String> roomIdList) {
		Map<String, List<String>> userIdMap = new HashMap<>();

		for (String roomId : roomIdList) {
			List<String> userIdList = new ArrayList<>();
			for (RoomMatchDto x :findById(Integer.parseInt(roomId))) {
				userIdList.add(x.getUser_id());
			}
			userIdMap.put(roomId, userIdList);
		}
		return userIdMap;
	}

	@Override
	public List<Room> findByUserId(String id) {
		return jdbcTemplate.query(SQL_FINDVALIDROOM, roomRowMapper(), id);
	}

	@Override
	public List<String> findParticipants(int room_id) {
		List<String> participantsList = new ArrayList<>();
		for (BaseDto Dto : jdbcTemplate.query(SQL_GETUSERID, StrRowMapper("user_id"), room_id)) {
			participantsList.add(String.valueOf(Dto.getName()));
		}
		return participantsList;
	}

	@Override
	public List<String> deleteRoomMatch(CanceledRoom canceledRoom) {
		jdbcTemplate.update(SQL_DELROOM_MATCH, canceledRoom.getUser_id(), canceledRoom.getRoom_id());
		List<String> leftParticipants = findParticipants(canceledRoom.getRoom_id());
		if (leftParticipants.size() == 0)
			roomInfoDao.roomStatusUpdate(canceledRoom.getRoom_id(), "destroyed");
		activityLogDao.create(new ActivityLogDto().
				getRoomExitLog(canceledRoom, roomInfoDao.getRoomInfoDto(canceledRoom.getRoom_id())));
		return leftParticipants;
	}

	private RowMapper<BaseDto> StrRowMapper(String col) {
		return (rs, rowNum) -> {
			BaseDto Dto = new BaseDto();
			Dto.setName(rs.getString(col));
			return Dto;
		};
	}

	private RowMapper<Room> roomRowMapper() {
		return (rs, rowNum) -> {
			Room room = new Room();
			room.setRoom_id(rs.getInt("room_id"));
			room.setParticipants(findParticipants(rs.getInt("room_id")));

			RoomInfoDto tmp = roomInfoDao.getRoomInfoDto(rs.getInt("room_id"));
			room.setRoom_status(tmp.getRoom_status());
			room.setEnter_at(tmp.getDeadline());
			room.setLocation_name(locationDao.findById(tmp.getLocation_id()).stream().findAny().get().getName());
			room.setCategory_name(categoryDao.findById(tmp.getCategory_id()).stream().findAny().get().getName());
			return room;
		};
	}
}
