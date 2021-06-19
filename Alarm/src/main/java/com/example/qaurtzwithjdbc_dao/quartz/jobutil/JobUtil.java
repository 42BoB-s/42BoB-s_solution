package com.example.qaurtzwithjdbc_dao.quartz.jobutil;

import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JobUtil {


    public Trigger buildCronJobTrigger(String scheduleExp) {
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp))
                .build();
    }

    // 매개변수로 입력받은 시간단위로 실행
    public Trigger buildSimpleJobTrigger(Integer hour) {
        return TriggerBuilder.newTrigger()
                .withSchedule(SimpleScheduleBuilder
                        .simpleSchedule()
                        .repeatForever()
                        .withIntervalInHours(hour))
                .build();
    }

    public JobDetail buildJobDetail(Class job, String name, String group, Map params) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(params);
        return JobBuilder
                .newJob(job)
                .withIdentity(name, group)
                .usingJobData(jobDataMap)
                .build();
    }
}
