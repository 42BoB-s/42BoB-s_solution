package bobs.repository;



import bobs.domain.CanceledRoom;
import bobs.domain.Room;

import java.util.List;

public interface RoomRepository {
	List<Room> findValidAll(String id);

	List<String> deleteRoomMatch(CanceledRoom canceledRoom);
}
