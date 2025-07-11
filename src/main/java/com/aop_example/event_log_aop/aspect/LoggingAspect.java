package com.aop_example.event_log_aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Aspect class to demonstrate AOP logging around the Event.play() method
 */
@Aspect
@Component
public class LoggingAspect {

    /**
     * Before advice - executes before the Event.play() method
     */
    @Before("execution(* com.aop_example.event_log_aop.model.Event.play(..))")
    public void beforePlay(JoinPoint joinPoint) {
        System.out.println("🎯 [BEFORE] About to start playing...");
        System.out.println("   Method: " + joinPoint.getSignature().getName());
    }

    /**
     * After advice - executes after the Event.play() method (regardless of outcome)
     */
    @After("execution(* com.aop_example.event_log_aop.model.Event.play(..))")
    public void afterPlay(JoinPoint joinPoint) {
        System.out.println("🏁 [AFTER] Finished playing!");
        System.out.println("   Method: " + joinPoint.getSignature().getName());
    }

    /**
     * Around advice - surrounds the Event.play() method execution
     * This is the most powerful advice type
     */
    @Around("execution(* com.aop_example.event_log_aop.model.Event.play(..))")
    public Object aroundPlay(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("⏱️  [AROUND] Starting execution timer...");
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Proceed with the actual method execution
            Object result = joinPoint.proceed();
            
            long endTime = System.currentTimeMillis();
            System.out.println("⏱️  [AROUND] Method executed successfully in " + (endTime - startTime) + "ms");
            
            return result;
        } catch (Exception e) {
            System.out.println("❌ [AROUND] Exception occurred: " + e.getMessage());
            throw e;
        }
    }
}
