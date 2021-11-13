package com.baraka.config.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.baraka.service.*.*(..))")
    public Object aroundAdviceService(ProceedingJoinPoint joinPoint) throws Throwable {

        Object output;
        long start = logEntry(joinPoint);
        try {
            output = joinPoint.proceed();
        } finally {
            logExit(joinPoint, start);
        }
        return output;
    }

    @Around("execution(* com.baraka.controller.*.*(..))")
    public Object aroundAdviceController(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = logEntry(joinPoint);
        Object output;
        ObjectMapper mapper = new ObjectMapper();
        String inputParams = this.getInputArgs(joinPoint, mapper);
        logRequest(inputParams);
        try {
            output = joinPoint.proceed();
            logResponse(output);
        } finally {
            logExit(joinPoint, start);
        }
        return output;
    }

    private long logEntry(ProceedingJoinPoint joinPoint) {
        long start = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().toString();
        log.info("{} :: {}() - Entry", className, methodName);
        return start;
    }

    private void logExit(ProceedingJoinPoint joinPoint, long start) {
        long elapsedTime = System.currentTimeMillis() - start;
        log.info("{} -> Method execution time: {} milliseconds.", joinPoint.getSignature().getName(),
                elapsedTime);
        log.info("{}.{}() - Exit", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
    }

    private void logRequest(String inputParams) {
        log.info(
                "---------------------------------------------------------------- REQUEST START ----------------------------------------------------------------");
        log.info(inputParams);
        log.info(
                "---------------------------------------------------------------- REQUEST END    ----------------------------------------------------------------");

    }

    private void logResponse(Object output) {
        if (Objects.nonNull(output)) {
            log.info(
                    "---------------------------------------------------------------- RESPONSE START ----------------------------------------------------------------");
            log.info("Response Status : {} ", ((ResponseEntity<?>) output).getStatusCode());
            log.info("Response Object : {} ", Objects.requireNonNull(((ResponseEntity<?>) output).getBody()).toString());
            log.info(
                    "---------------------------------------------------------------- RESPONSE END    ----------------------------------------------------------------");

        }
    }

    private String getInputArgs(ProceedingJoinPoint pjt, ObjectMapper mapper) {
        Object[] array = pjt.getArgs();
        CodeSignature signature = (CodeSignature) pjt.getSignature();

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        int i = 0;
        String[] parameterNames = signature.getParameterNames();
        int maxArgs = parameterNames.length;
        for (String name : signature.getParameterNames()) {
            sb.append("[").append(name).append(":");
            try {
                sb.append(mapper.writeValueAsString(array[i])).append("]");
                if (i != maxArgs - 1) {
                    sb.append(",");
                }
            } catch (Exception e) {
                sb.append("],");
            }
            i++;
        }
        return sb.append("}").toString();
    }
}
