package bobs.Dao;

import bobs.Dto.ActivityLogDto;

public interface ActivityLogDao {
    public void leaveLog(ActivityLogDto activityLogDto);
}
