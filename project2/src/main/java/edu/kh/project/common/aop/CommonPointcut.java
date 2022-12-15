package edu.kh.project.common.aop;

import org.aspectj.lang.annotation.Pointcut;

// JoinPoint : 공통 코드가 삽입될 수 있는 후보
// Pointcut : 실제로 코드가 삽입될 부분

// Pointcut을 모아둘 클래스
public class CommonPointcut {
    // edu.kh.project.board.model.service.
    // edu.kh.project.board.model.service.
    // edu.kh.project.board.model.service.
    // edu.kh.project.board.model.service.
    
    // execution([접근제한자] 반환형 패키지명.클래스명.메서드명(파라미터))
    // *  : 모든
    // .  : 바로 밑 하위 경로
    // .. : 모든 후손, 0개 이상
    @Pointcut("execution(* edu.kh.project..*ServiceImpl.*(..))")
    public void serviceImplPointcut() {}
    
}