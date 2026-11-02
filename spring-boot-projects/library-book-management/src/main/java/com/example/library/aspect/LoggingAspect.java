package com.example.library.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

// AOP: logs every method of the controller and service without touching their code.
@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.example.library.controller..*(..)) || execution(* com.example.library.service..*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.info("START  {}.{}() args={}", className, methodName, Arrays.toString(joinPoint.getArgs()));
        long begin = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            log.info("END    {}.{}() finished in {} ms", className, methodName,
                    System.currentTimeMillis() - begin);
            return result;
        } catch (Throwable ex) {
            log.error("FAILED {}.{}() -> {}: {}", className, methodName,
                    ex.getClass().getSimpleName(), ex.getMessage());
            throw ex; // re-throw so GlobalExceptionHandler can build the response
        }
    }
}
