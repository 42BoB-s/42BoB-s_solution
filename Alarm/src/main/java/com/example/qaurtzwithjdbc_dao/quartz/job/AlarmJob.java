package com.example.qaurtzwithjdbc_dao.quartz.job;

import com.example.qaurtzwithjdbc_dao.dao.RoomInfoJDBCDao;
import com.example.qaurtzwithjdbc_dao.dao.RoomMatchJDBCDao;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;
import java.util.Map;

public class AlarmJob implements Job {

    private RoomInfoJDBCDao roomInfoDao;
    private RoomMatchJDBCDao roomMatchDao;

    public AlarmJob(RoomInfoJDBCDao roomInfoDao, RoomMatchJDBCDao roomMatchDao) {
        this.roomInfoDao = roomInfoDao;
        this.roomMatchDao = roomMatchDao;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        List<String> roomIdList = roomInfoDao.getAlarmRoomId();
        if (roomIdList.isEmpty() == false) {
            Map<String, List<String>> alarmUserIdMap = roomMatchDao.getAlarmUserId(roomIdList);
            System.err.println("roomId set : " + alarmUserIdMap.keySet() + " userId List : " + alarmUserIdMap.get("1"));
            /* change room_status active -> success */
            // roomInfoDao.roomStatusUpdate(roomIdList);
        }
        else {
            System.err.println("No valid Room Info");
        }
    }
}
