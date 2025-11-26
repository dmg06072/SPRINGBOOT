package com.example.demo.Aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

//    @Before("execution(* com.example.demo.Model.Service.MemoService.addMemo(..))")
//    public void loggingBefore(JoinPoint joinPoint)
//    {
//        log.info("[AOP] BEFORE..." + joinPoint);
//    }
//    @After("execution(* com.example.demo.Model.Service.MemoService.addMemo(..))")
//    public void loggingAfter(JoinPoint joinPoint)
//    {
//        log.info("[AOP] AFTER..." + joinPoint);
//    }

    //execution(* com.example.demo.Model.Service.MemoService.addMemo(..))
    //target : MemoService.addMemo 함수
    //execution(* com.example.demo.Model.Service.MemoService.*(..))
    //MemoService안의 모든 Method
    //execution(* com.example.demo.Model.Service.*.*(..))
    //Service패키지안의 모든 Service안의 모든  Method
    @Around("execution(* com.example.demo.Model.Service.*.*(..))")
    public Object loggingAround(ProceedingJoinPoint pjp) throws Throwable {
        //BEFORE 처리코드
        long start_time = System.currentTimeMillis();
        log.info("[AOP] AROUND BEFORE");

        //타겟 함수 실행
        Object returnValue = pjp.proceed();
        log.info("타겟 함수 리턴값 : " + returnValue);

        //AFTER 처리코드
        log.info("[AOP] AROUND AFTER");
        long end_time = System.currentTimeMillis();
        log.info("[AOP] 소요시간 : " +(end_time-start_time)+" ms");
        return returnValue;
    }
}
