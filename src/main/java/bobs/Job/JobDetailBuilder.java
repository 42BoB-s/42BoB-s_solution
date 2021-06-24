package bobs.Job;

import org.quartz.*;
import org.springframework.stereotype.Component;

import static org.quartz.JobBuilder.*;

@Component
public class JobDetailBuilder {

    public JobDetail makeAlarmJobDetail() {
        JobDetail AlarmJobDetail =  newJob(AlarmJob.class)
                .withIdentity("Alarm", "AlarmGroup")
                .usingJobData("ret_count", 0)
                .build();
        return AlarmJobDetail;
    }
}
