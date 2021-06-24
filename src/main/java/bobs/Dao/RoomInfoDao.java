package bobs.Dao;

import bobs.Dto.RoomInfoDto;

import java.util.List;

public interface RoomInfoDao {
	int roomInsert(RoomInfoDto roomInfoDto);
	List<RoomInfoDto> vaildRoomSelect(RoomInfoDto roomInfoDto, String startTime, String endTime);
	List<String> getAlarmRoomId();
	void roomStatusUpdate(List<String> roomIdList);
	int testRoomInsert(RoomInfoDto roomInfo);
	List<String> testFindRoom(List<String> roomIdList);
}
