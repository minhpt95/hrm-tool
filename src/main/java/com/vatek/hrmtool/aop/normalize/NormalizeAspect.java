package com.vatek.hrmtool.aop.normalize;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
@Aspect
@Component
public class NormalizeAspect {
    @Around("@within(com.vatek.hrmtool.aop.normalize.annotation.Normalize) || @annotation(com.vatek.hrmtool.aop.normalize.annotation.Normalize)")
    public Object normalizeParameters(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg instanceof String) {
                args[i] = Normalizer.normalize((String) arg, Normalizer.Form.NFC);
            }
        }
        return joinPoint.proceed(args);
    }
}
