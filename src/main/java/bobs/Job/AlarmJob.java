package bobs.Job;
import bobs.Dao.JdbcRoomInfoDao;
import bobs.Dao.JdbcRoomMatchDao;
import bobs.Slack.Slack;
import lombok.SneakyThrows;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Map;
import java.util.Set;


@Component
@PersistJobDataAfterExecution
public class AlarmJob implements Job {

    private final JdbcRoomInfoDao roomInfoDao;
    private final JdbcRoomMatchDao roomMatchDao;
    private final Scheduler scheduler;
    private final JobDetailBuilder jobDetailBuilder;
    private final JobTriggerBuilder jobTriggerBuilder;
    private Slack Slack = new Slack();

    @Autowired
    public AlarmJob(JdbcRoomInfoDao roomInfoDao, JdbcRoomMatchDao roomMatchDao, Scheduler scheduler, JobDetailBuilder jobDetailBuilder, JobTriggerBuilder jobTriggerBuilder) {
        this.roomInfoDao = roomInfoDao;
        this.roomMatchDao = roomMatchDao;
        this.scheduler = scheduler;
        this.jobDetailBuilder = jobDetailBuilder;
        this.jobTriggerBuilder = jobTriggerBuilder;
    }

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        int ret_count = dataMap.getIntValue("ret_count");

        System.err.println("ret_count = " + ret_count);

        // 5회까지 재시도 하도록 함.
        if(ret_count > 4){
            JobExecutionException e = new JobExecutionException("Retries exceeded");
            // 5회까지 했는데도 Job에 Error가 계속 된다면
            e.setUnscheduleAllTriggers(true);
            // 다시 AlarmJob 등록
            dataMap.putAsString("ret_count", 0);
            scheduler.scheduleJob(jobDetailBuilder.makeAlarmJobDetail(), jobTriggerBuilder.makeAlarmJobTrigger());
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
            e.printStackTrace();
            ret_count++;
            dataMap.putAsString("ret_count", ret_count);
            Thread.sleep(5000);
            JobExecutionException e2 = new JobExecutionException(e);
            e2.refireImmediately();
            throw e2;
        }
    }
}