package us.thirdbase.rspi.parts.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoggingAspectConfigurationTest {

    @Mock
    private JoinPoint joinPoint;
    @Mock
    private Appender<ILoggingEvent> appender;
    @Captor
    private ArgumentCaptor<ILoggingEvent> logCaptor;
    private LoggingAspectConfiguration subject;
    @Mock
    private Signature signature;

    @BeforeEach
    void setUp() {
        subject = new LoggingAspectConfiguration();
        Logger mainLogger = (Logger) LoggerFactory.getLogger(LoggingAspectConfiguration.class.getName());
        mainLogger.addAppender(appender);
        mainLogger.setLevel(Level.DEBUG);
    }

    @Test
    void loggingAdvice() {
        given(joinPoint.getSignature()).willReturn(signature);
        given(joinPoint.getArgs()).willReturn(new Object[]{"Hello"});
        given(signature.toString()).willReturn("someMethod");

        subject.loggingAdvice(joinPoint);

        verify(appender, after(1000).atLeast(1)).doAppend(logCaptor.capture());
        assertThat(logCaptor.getValue().getMessage()).isEqualTo("calling someMethod with [Hello]");
    }

    @Test
    void monitor() {
        subject.monitor();
    }

    @Test
    void performanceMonitorInterceptor() {
        PerformanceMonitorInterceptor actual = subject.performanceMonitorInterceptor();

        assertThat(actual).isNotNull();
    }

    @Test
    void performanceMonitorAdvisor() {
        Advisor actual = subject.performanceMonitorAdvisor();

        assertThat(actual).isInstanceOf(DefaultPointcutAdvisor.class);
        DefaultPointcutAdvisor advisor = (DefaultPointcutAdvisor) actual;
        assertThat(advisor.getPointcut()).isInstanceOf(AspectJExpressionPointcut.class);
        AspectJExpressionPointcut pointcut = (AspectJExpressionPointcut) advisor.getPointcut();
        assertThat(pointcut.getPointcutExpression().getPointcutExpression()).isEqualTo(
            "us.thirdbase.rspi.parts.config.LoggingAspectConfiguration.monitor()"
        );
    }
}
