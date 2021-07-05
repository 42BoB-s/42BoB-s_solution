package bobs.Service;

import bobs.Dto.RoomInfoDto;
import bobs.Dto.RoomMatchDto;
import bobs.domain.CanceledRoom;
import bobs.domain.Room;

import java.util.List;

public interface RoomService {
	// 새로운 방 생성하는 서비스
	int roomCreate(RoomInfoDto roomInfoDTO);
	// 해당 되는 방을 찾아서 새로운 방을 만들어 입장시키거나, 해당되는 방에 입장 시키는 서비스
	// roomCreate, roomCountCheck, userDupleCheck, roomEnter 메소드 사용
	boolean findVaildRoom(RoomInfoDto roomInfoDto, RoomMatchDto roomMatchDto, String endTime);
	// 방에 유저수기 4명 미만인지 체크하는 서비스
	boolean roomCountCheck(RoomMatchDto roomMatchDto);
	// 방의 중복 유저 체크
	boolean userDupleCheck(RoomMatchDto roomMatchDto);
	// 방에 입장하는 서비스
	int roomEnter(RoomMatchDto roomMatchDto);
	List<Room> findRooms(String id);
	void cancelRoom(CanceledRoom canceledRoom);
}
