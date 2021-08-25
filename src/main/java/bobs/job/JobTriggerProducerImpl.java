package bobs.job;

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
                .withSchedule(cronSchedule("0 55 * * * ?")) // 매 시각 55분마다
                .forJob("Alarm", "AlarmGroup")
                .build();
        return AlarmTrigger;
    }
}
