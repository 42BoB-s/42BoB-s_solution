package bobs.Dao;

import bobs.Dto.RoomInfoDto;
import bobs.Dto.RoomMatchDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface RoomMatchDao {
	int roomUserCount(RoomMatchDto roomMatchDto);
	int matchInsert(RoomMatchDto roomMatchDto);
	int roomDupleCount(RoomMatchDto roomMatchDto);
	Map<String, List<String>> getAlarmUserId(List<String> roomIdList);
}
