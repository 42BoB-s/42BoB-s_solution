package com.example.alarmscheduler.job;

import com.example.alarmscheduler.dao.RoomInfoJDBCDao;
import com.example.alarmscheduler.dao.RoomMatchJDBCDao;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
            Set keyset = alarmUserIdMap.keySet();
            for (Object key : keyset) {
                String tmpkey = String.valueOf(key);
                System.err.println("roomId : " + tmpkey + " userId List : " + alarmUserIdMap.get(tmpkey));
            }


            /* change room_status active -> success */
            roomInfoDao.roomStatusUpdate(roomIdList);
        }
        else {
            System.err.println("No valid Room Info");
        }
    }
}