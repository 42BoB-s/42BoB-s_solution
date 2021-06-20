package bobs.Dao;

import bobs.Dto.RoomInfoDto;

import java.util.List;

public interface RoomInfoDao {
	int roomInsert(RoomInfoDto roomInfoDto);
	List<RoomInfoDto> vaildRoomSelect(RoomInfoDto roomInfoDto, String startTime, String endTime);
	int statusUpdate(RoomInfoDto roomInfoDto);
	RoomInfoDto roomSelect(RoomInfoDto roomInfoDto);
}
