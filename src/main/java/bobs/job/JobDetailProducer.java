package bobs.job;

import org.quartz.JobDetail;

// JobDetail을 생성하여 반환하기 위한 인터페이스
public interface JobDetailProducer {

    // AlarmJob의 JobDetail을 생성하기 위한 메서드
    JobDetail getAlarmDetail();
}
