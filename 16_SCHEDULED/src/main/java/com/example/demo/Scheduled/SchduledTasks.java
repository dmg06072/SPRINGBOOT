package com.example.demo.Scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Month;

@Component
@Slf4j
public class SchduledTasks {

//    @Scheduled(fixedRate = 3000)
//    public void task1(){
//        log.info("task1.."+System.currentTimeMillis());
//    }
//    @Scheduled(fixedDelay = 1500)
//    public void task2(){
//        log.info("task2.."+System.currentTimeMillis());
//    }
    @Scheduled(cron="* * * * * *") // 초 분 시 일 월 요일
    public void task3(){
        log.info("task3.."+System.currentTimeMillis());
    }

    //
//[ Cron 표현식 위치별 값 정리 ]
//
//            | 위치             | 범위/값                                 | 설명     | 예시                  |
//            |------------------|-----------------------------------------|----------|-----------------------|
//            | 초 (Seconds)     | 0–59                                    | 초 단위  | 0, 15, 30, */10       |
//            | 분 (Minutes)     | 0–59                                    | 분 단위  | 0, 30, */5            |
//            | 시 (Hours)       | 0–23                                    | 시 단위  | 0, 12, 9-18, */6      |
//            | 일 (Day of Month)| 1–31, ?, L, W                           | 날짜     | 1, 15, L, 15W         |
//            | 월 (Month)       | 1–12, JAN–DEC                           | 월       | 1, 6, 12, 3-5         |
//            | 요일 (Day of Week)| 0–7 (0/7=일요일), SUN–SAT, ?, L, #     | 요일     | MON-FRI, 0,6, FRI, 2#1|

//[ 자주 쓰는 Cron 패턴 예시 ]
//
//"0 0 * * * *"       → 매 시 정각 실행
//"0 */5 * * * *"     → 5분마다 실행
//"0 0 9 * * MON-FRI" → 평일 오전 9시마다 실행
//"0 0 0 * * *"       → 매일 자정 실행
//"0 0 0 1 * *"       → 매달 1일 자정 실행
//"0 0 23 L * *"      → 매달 마지막 날 23시 실행
//"0 0 10 ? * 2#1"    → 매달 첫 번째 월요일 오전 10시 실행
}
