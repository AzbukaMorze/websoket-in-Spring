package com.golovkin.websocket.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class ControllerLogAspect {

    @Pointcut("execution(public * com.golovkin.websocket.controller..*(..))")
    public void callController(){ }

    @Before("callController()")
    public void beforeCallController(JoinPoint joinPoint){

        List<String> args = Arrays.stream(joinPoint.getArgs())
                .map(arg -> arg != null ? arg.toString() : "null")
                .toList();

        log.info("Call {}#{} with args: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                args);
    }

    @AfterReturning(pointcut = "callController()", returning = "result")
    public void afterReturningController(JoinPoint joinPoint, Object result) {
        String returnValue = result != null ? result.toString() : "void";
        log.info("Successfully executed {}#{} with return value: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                returnValue);
    }
}
