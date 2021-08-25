package bobs.job;

import org.quartz.Trigger;

// Job의 Trigger 를 생성하여 반환하기 위한 인터페이스
public interface JobTriggerProducer {
    // AlarmJob의 Trigger를 생성하기 위한 메서드
    Trigger getAlarmTrigger();
}
