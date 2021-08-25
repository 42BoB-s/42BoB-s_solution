package bobs.job;

import org.quartz.JobDetail;
import org.springframework.stereotype.Component;

import static org.quartz.JobBuilder.newJob;

@Component
public class JobDetailProducerImpl implements JobDetailProducer {

    @Override
    public JobDetail getAlarmDetail() {
        JobDetail AlarmJobDetail = newJob(AlarmJob.class)
                .withIdentity("Alarm", "AlarmGroup")
                .usingJobData("ret_count", 0)
                .build();
        return AlarmJobDetail;
    }
}
