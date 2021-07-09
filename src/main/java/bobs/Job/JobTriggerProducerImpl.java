package bobs.Job;

import org.quartz.Trigger;
import org.springframework.stereotype.Component;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Component
public class JobTriggerProducerImpl implements JobTriggerProducer {

    @Override
    public Trigger getAlarmTrigger() {
        Trigger AlarmTrigger = newTrigger()
                .withIdentity("AlarmTrigger", "AlarmGroup")
<<<<<<< HEAD
                .withSchedule(cronSchedule("0 55 * * * ?"))
=======
                .withSchedule(cronSchedule("0 55 * * * ?")) // 매 시각 55분마다
>>>>>>> abbc7aa2479d66a75ed6e5c5bcfeef8913d02ebc
                .forJob("Alarm", "AlarmGroup")
                .build();
        return AlarmTrigger;
    }
}
