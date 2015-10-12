package com.taobao.bird.common.log.dynamic;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.LogbackException;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;

import com.taobao.bird.common.log.Logger;
import com.taobao.bird.common.model.config.SimpleNode;

/**
 * @desc
 * @author junyu 
 * @version
 **/
public class BirdLogback918DynamicLogger extends BirdDynamicLogger {

    protected static LoggerContext loggerContext;

    public BirdLogback918DynamicLogger(){
        buildLoggerContext(null);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Logger initLogger(SimpleNode node, Logger logger, AppenderType type, String fileName, String pattern) {
        Appender appender = null;
        pattern = StringUtils.isBlank(pattern) ? "%d{yyyy-MM-dd HH:mm:ss.SSS} %p %m%n" : pattern;
        if (type == AppenderType.SIZED_ROLLING_APPENDER) {
            appender = buildAppender(node, fileName, pattern, 20, "100M");
        } else if (type == AppenderType.DAILY_ROLLING_APPENDER) {
            appender = buildDailyMaxRollingAppender(node, fileName, pattern, 6);
        } else {
            throw new IllegalArgumentException("unknow appender type:" + type);
        }

        ch.qos.logback.classic.Logger delegate = (ch.qos.logback.classic.Logger) logger.getDelegate();
        delegate.setAdditive(false);
        delegate.detachAndStopAllAppenders();
        delegate.addAppender(appender);
        delegate.setLevel(Level.INFO);
        return logger;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Appender buildAppender(SimpleNode node, String fileName, String pattern, int maxBackup, String maxSize) {
        RollingFileAppender appender = new RollingFileAppender();
        appender.setContext(loggerContext);
        appender.setName(node.getNodeId());
        appender.setAppend(true);
        appender.setFile(new File(getLogPath(node), fileName).getAbsolutePath());

        SizeBasedTriggeringPolicy trigger = new SizeBasedTriggeringPolicy();
        trigger.setMaxFileSize(maxSize);
        appender.setTriggeringPolicy(trigger);

        FixedWindowRollingPolicy rolling = new FixedWindowRollingPolicy();
        rolling.setMaxIndex(maxBackup);
        rolling.setParent(appender);
        rolling.setContext(loggerContext);
        rolling.start();
        appender.setRollingPolicy(rolling);

        PatternLayout layout = new PatternLayout();
        layout.setPattern(pattern);
        layout.setContext(loggerContext);
        layout.start();
        appender.setLayout(layout);
        appender.setPrudent(true);
        appender.start();
        return appender;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Appender buildDailyMaxRollingAppender(SimpleNode node, String fileName, String pattern, int maxBackupIndex) {
        RollingFileAppender appender = new RollingFileAppender();
        appender.setContext(loggerContext);
        appender.setName(node.getNodeId());
        appender.setAppend(true);
        appender.setFile(new File(getLogPath(node), fileName).getAbsolutePath());

        TimeBasedRollingPolicy rolling = new TimeBasedRollingPolicy();
        rolling.setContext(loggerContext);
        rolling.setFileNamePattern(new File(getLogPath(node), fileName).getAbsolutePath() + ".%d{yyyy-MM-dd-HH}");
        rolling.setMaxHistory(maxBackupIndex);
        rolling.setParent(appender);
        rolling.start();
        appender.setRollingPolicy(rolling);

        PatternLayout layout = new PatternLayout();
        layout.setContext(loggerContext);
        layout.setPattern(pattern);
        layout.start();
        appender.setLayout(layout);
        appender.setPrudent(true);
        appender.start();
        return appender;
    }

    public static LoggerContext buildLoggerContext(Map<String, String> props) {
        if (loggerContext == null) {
            ILoggerFactory lcObject = LoggerFactory.getILoggerFactory();

            if (!(lcObject instanceof LoggerContext)) {
                throw new LogbackException("Expected LOGBACK binding with SLF4J, but another log system has taken the place: "
                                           + lcObject.getClass().getSimpleName());
            }

            loggerContext = (LoggerContext) lcObject;
            if (props != null) {
                for (Map.Entry<String, String> entry : props.entrySet()) {
                    loggerContext.putProperty(entry.getKey(), entry.getValue());
                }
            }
        }

        return loggerContext;
    }
}
