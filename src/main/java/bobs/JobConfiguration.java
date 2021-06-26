package bobs;


import bobs.Job.JobDetailProducer;
import bobs.Job.JobTriggerPorducer;
import org.quartz.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

@Configuration
@PropertySource("classpath:/quartz.properties")
public class JobConfiguration {

    private final Scheduler scheduler;
    private final JobDetailProducer jobDetailProducer;
    private final JobTriggerPorducer jobTriggerPorducer;

    @Autowired
    public JobConfiguration(Scheduler scheduler,  JobDetailProducer jobDetailProducer, JobTriggerPorducer jobTriggerPorducer) {
        this.scheduler = scheduler;
        this.jobDetailProducer = jobDetailProducer;
        this.jobTriggerPorducer = jobTriggerPorducer;

    }

    @PostConstruct
    public void start() {
        try {

            /* Job을 스케줄러에 등록*/
            scheduler.scheduleJob(jobDetailProducer.getAlarmDetail(), jobTriggerPorducer.getAlarmTrigger());

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}