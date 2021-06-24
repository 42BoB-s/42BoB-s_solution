package bobs;

import bobs.Job.JobDetailBuilder;
import bobs.Job.JobTriggerBuilder;
import org.quartz.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

@Configuration
@PropertySource("classpath:/quartz.properties")
public class JobConfiguration {

    private final Scheduler scheduler;
    private final JobDetailBuilder jobDetailBuilder;
    private final JobTriggerBuilder jobTriggerBuilder;

    @Autowired
    public JobConfiguration(Scheduler scheduler, JobDetailBuilder jobDetailBuilder,JobTriggerBuilder jobTriggerBuilder ) {
        this.scheduler = scheduler;
        this.jobDetailBuilder = jobDetailBuilder;
        this.jobTriggerBuilder = jobTriggerBuilder;
    }

    @PostConstruct
    public void start() {
        try {

            /* Job을 스케줄러에 등록*/
            JobDetail AlarmJob = jobDetailBuilder.makeAlarmJobDetail();
            Trigger AlarmTrigger =  jobTriggerBuilder.makeAlarmJobTrigger();
            scheduler.scheduleJob(AlarmJob, AlarmTrigger);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}