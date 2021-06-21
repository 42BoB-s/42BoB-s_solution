package com.example.alarmscheduler;

import com.example.alarmscheduler.job.AlarmJob;
import org.quartz.*;
import static org.quartz.JobBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.TriggerBuilder.newTrigger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

@Configuration
@PropertySource("classpath:/quartz.properties")
public class JobConfiguration {

    private final Scheduler scheduler;

    @Autowired
    public JobConfiguration(Scheduler scheduler) {
        System.out.println("JobConfiguration Constructed");
        this.scheduler = scheduler;
    }

    @PostConstruct
    public void start() {
        try {

            /* Job을 스케줄러에 등록*/
            JobDetail AlarmJob = newJob(AlarmJob.class)
                    .withIdentity("Alarm", "AlarmGroup")
                    .usingJobData("ret_count", 0)
                    .build();

            Trigger AlarmTrigger = newTrigger()
                    .withIdentity("AlarmTrigger", "AlarmGroup")
                    .withSchedule(cronSchedule("1/10 * * * * ?"))
                    .forJob("Alarm", "AlarmGroup")
                    .build();

            scheduler.scheduleJob(AlarmJob, AlarmTrigger);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}