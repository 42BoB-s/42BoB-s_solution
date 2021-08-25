package bobs;

import bobs.job.JobDetailProducer;
import bobs.job.JobDetailProducerImpl;
import bobs.job.JobTriggerProducerImpl;
import bobs.job.JobTriggerProducer;
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
    private final JobTriggerProducer jobTriggerProducer;

    @Autowired
    public JobConfiguration(Scheduler scheduler, JobDetailProducerImpl jobDetailProducer, JobTriggerProducerImpl jobTriggerPorducerImpl) {
        this.scheduler = scheduler;
        this.jobDetailProducer = jobDetailProducer;
        this.jobTriggerProducer = jobTriggerPorducerImpl;
    }

    @PostConstruct
    public void start() {
        try {
            /* Job을 스케줄러에 등록*/
            scheduler.scheduleJob(jobDetailProducer.getAlarmDetail(), jobTriggerProducer.getAlarmTrigger());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}