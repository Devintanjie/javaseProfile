package com.taobao.bird.common.log.dynamic;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import com.taobao.bird.common.log.Logger;
import com.taobao.bird.common.model.config.SimpleNode;

/**
 * @desc
 * @author junyu
 * @version
 **/
public class BirdLog4jDynamicLogger extends BirdDynamicLogger {

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

        org.apache.log4j.Logger delegate = (org.apache.log4j.Logger) logger.getDelegate();
        delegate.setAdditivity(false);
        delegate.removeAllAppenders();
        delegate.addAppender(appender);
        delegate.setLevel(Level.INFO);
        return logger;
    }

    private Appender buildAppender(SimpleNode node, String fileName, String pattern, int maxBackup, String maxSize) {
        RollingFileAppender appender = new RollingFileAppender();
        appender.setMaxBackupIndex(maxBackup);
        appender.setMaxFileSize(maxSize);
        appender.setName(node.getNodeId());
        appender.setAppend(true);
        appender.setEncoding(getEncoding());
        appender.setLayout(new PatternLayout(pattern));
        appender.setFile(new File(getLogPath(node), fileName).getAbsolutePath());
        appender.activateOptions();
        return appender;
    }

    private Appender buildDailyMaxRollingAppender(SimpleNode node, String fileName, String pattern, int maxBackupIndex) {
        DailyMaxRollingFileAppender appender = new DailyMaxRollingFileAppender();
        appender.setName(node.getNodeId());
        appender.setAppend(true);
        appender.setEncoding(getEncoding());
        appender.setLayout(new PatternLayout(pattern));
        appender.setDatePattern("'.'yyyy-MM-dd-HH");
        appender.setMaxBackupIndex(maxBackupIndex);
        appender.setFile(new File(getLogPath(node), fileName).getAbsolutePath());
        appender.activateOptions();
        return appender;
    }
}
