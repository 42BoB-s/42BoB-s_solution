package bobs.Dao;

import bobs.Dto.RoomInfoDto;
import bobs.Dto.RoomMatchDto;

import java.sql.SQLException;

public interface RoomMatchDao {
	int roomUserCount(RoomMatchDto roomMatchDto);
	int matchInsert(RoomMatchDto roomMatchDto);
}
