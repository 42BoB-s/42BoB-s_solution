package bobs.Job;
import bobs.Dao.JdbcRoomInfoDao;
import bobs.Dao.JdbcRoomMatchDao;
import bobs.Slack.Slack;
import lombok.SneakyThrows;
import org.quartz.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@PersistJobDataAfterExecution
public class AlarmJob implements Job {

    private JdbcRoomInfoDao roomInfoDao;
    private JdbcRoomMatchDao roomMatchDao;
    private Slack Slack = new Slack();

    public AlarmJob(JdbcRoomInfoDao roomInfoDao, JdbcRoomMatchDao roomMatchDao) {
        this.roomInfoDao = roomInfoDao;
        this.roomMatchDao = roomMatchDao;
    }

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        int ret_count = dataMap.getIntValue("ret_count");


        if(ret_count > 4){ // Retry is allowed up to 5 times.
            JobExecutionException e = new JobExecutionException("Retries exceeded");
            //make sure it doesn't run again
            e.setUnscheduleAllTriggers(true);
            throw e;
        }

        try {
            List<String> roomIdList = roomInfoDao.getAlarmRoomId();
            if (roomIdList.isEmpty() == false) {
                Map<String, List<String>> alarmUserIdMap = roomMatchDao.getAlarmUserId(roomIdList);
                Set keyset = alarmUserIdMap.keySet();
                for (Object key : keyset) {
                    Slack.sendSuccessMsg(alarmUserIdMap.get(String.valueOf(key)));
                }
                roomInfoDao.roomStatusUpdate(roomIdList);
            }
            // reset ret_count to 0.
            dataMap.putAsString("ret_count", 0);
        } catch (Exception e) {
            ret_count++;
            dataMap.putAsString("ret_count", ret_count);
            Thread.sleep(60000);
            JobExecutionException e2 = new JobExecutionException(e);
            e2.refireImmediately();
            throw e2;
        }
    }
}