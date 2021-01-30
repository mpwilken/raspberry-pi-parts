package us.thirdbase.rspi.parts.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.Arrays;

@Configuration
@EnableAspectJAutoProxy
@Aspect
public class LoggingAspectConfiguration {
    // cannot use @Slf4j
    private final Logger aspectLogger = LoggerFactory.getLogger(LoggingAspectConfiguration.class.getName());

    @Pointcut("within(us.thirdbase.rspi.parts.service..*)")
    @SuppressWarnings("WeakerAccess")
    public void monitor() {
    }

    @Before("execution(* us.thirdbase.rspi.parts.service.PartService.find*(..))")
    public void loggingAdvice(JoinPoint joinPoint) {
        aspectLogger.debug("calling " + joinPoint.getSignature() + " with " + Arrays.toString(joinPoint.getArgs()));
    }

    @Bean
    public PerformanceMonitorInterceptor performanceMonitorInterceptor() {
        return new PerformanceMonitorInterceptor(false);
    }

    @Bean
    public Advisor performanceMonitorAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("us.thirdbase.rspi.parts.config.LoggingAspectConfiguration.monitor()");
        return new DefaultPointcutAdvisor(pointcut, performanceMonitorInterceptor());
    }
}

