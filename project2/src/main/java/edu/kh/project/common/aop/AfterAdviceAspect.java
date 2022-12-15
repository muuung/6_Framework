package edu.kh.project.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AfterAdviceAspect {
    private Logger logger = LoggerFactory.getLogger(AfterAdviceAspect.class);
    
    // @After : 메서드 종료 후
    // @AfterReturning : 메서드 종료 후 + 메서드 반환 값ㅇ르 다룰 수 있음
    // @AfterThrowing : 메서드 예외 발생 시 + 예외 객체를 다룰 수 있음

    @After("CommonPointcut.serviceImplPointcut()")
    public void afterServiceLog(JoinPoint jp) {
        logger.info("--------------------------------------------------------------------------------");
    }
}