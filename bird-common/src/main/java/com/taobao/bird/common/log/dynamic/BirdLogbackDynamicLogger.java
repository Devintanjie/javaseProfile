package com.taobao.bird.common.log.dynamic;

import java.io.File;
import java.nio.charset.Charset;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;

import com.taobao.bird.common.model.config.SimpleNode;

/**
 * @desc
 * @author junyu
 * @version
 **/
public class BirdLogbackDynamicLogger extends BirdLogback918DynamicLogger {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
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

        PatternLayoutEncoder layout = new PatternLayoutEncoder();
        layout.setContext(loggerContext);
        layout.setPattern(pattern);
        layout.setCharset(Charset.forName(getEncoding()));
        layout.start();
        appender.setEncoder(layout);

        appender.setPrudent(true);
        appender.start();
        return appender;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
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

        PatternLayoutEncoder layout = new PatternLayoutEncoder();
        layout.setContext(loggerContext);
        layout.setPattern(pattern);
        layout.setCharset(Charset.forName(getEncoding()));
        layout.start();
        appender.setEncoder(layout);

        appender.setPrudent(true);
        appender.start();
        return appender;
    }
}
