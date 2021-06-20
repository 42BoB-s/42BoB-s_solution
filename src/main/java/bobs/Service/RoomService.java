package bobs.Service;

import bobs.Dto.RoomInfoDto;
import bobs.Dto.RoomMatchDto;

import java.util.List;

public interface RoomService {
	int roomCreate(RoomInfoDto roomInfoDTO);
	List<RoomInfoDto> findVaildRoom(RoomInfoDto roomInfoDto, RoomMatchDto roomMatchDto, String startTime, String endTime);
	int roomUpdate(RoomInfoDto roomInfoDto);
	int roomEnter(RoomMatchDto roomMatchDto);
}
