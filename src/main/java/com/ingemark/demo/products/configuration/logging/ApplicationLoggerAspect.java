package com.ingemark.demo.products.configuration.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ApplicationLoggerAspect {

    private static final Logger log = LoggerFactory.getLogger(ApplicationLoggerAspect.class);

    @Pointcut("within(com.ingemark.demo.products.controller..*)")
    public void defineControllersPackagePointcuts() {
    }

    @Before("defineControllersPackagePointcuts()")
    public void logAroundControllers(final JoinPoint joinPoint) {
        log.info(
                "Invoked {}.{}() with arguments[s] = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs())
        );
    }
}
