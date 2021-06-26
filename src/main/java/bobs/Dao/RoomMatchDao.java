package bobs.Dao;

import bobs.Dto.RoomMatchDto;
import bobs.domain.CanceledRoom;
import bobs.domain.Room;

import java.util.List;
import java.util.Map;

public interface RoomMatchDao {
	int roomUserCount(RoomMatchDto roomMatchDto);
	int matchInsert(RoomMatchDto roomMatchDto);
	int roomDupleCount(RoomMatchDto roomMatchDto);
	Map<String, List<String>> getAlarmUserId(List<String> roomIdList);

	/* 추가된 함수 */
	List<Room> findValidAll(String id);
	List<String> findParticipants(int room_id);
	List<String> deleteRoomMatch(CanceledRoom canceledRoom);
}
