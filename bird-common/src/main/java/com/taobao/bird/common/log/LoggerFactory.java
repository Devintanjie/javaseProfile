package com.taobao.bird.common.log;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @desc
 * @author junyu 
 * @version
 **/
public class LoggerFactory {

    private LoggerFactory(){
    }

    private static volatile LoggerAdapter                          LOGGER_ADAPTER;
    private static final ConcurrentHashMap<String, FailsafeLogger> LOGGERS = new ConcurrentHashMap<String, FailsafeLogger>();

    static {
        String logger = System.getProperty("bird.logger");
        if ("slf4j".equals(logger)) {
            setLoggerAdapter(new Slf4jLoggerAdapter());
            // } else if ("jcl".equals(logger)) {
            // } else if ("log4j".equals(logger)) {
        } else if ("jdk".equals(logger)) {
            setLoggerAdapter(new JdkLoggerAdapter());
        } else {
            try {
                setLoggerAdapter(new Slf4jLoggerAdapter());
            } catch (Throwable e1) {
                setLoggerAdapter(new JdkLoggerAdapter());
            }
        }
    }

    public static void setLoggerAdapter(LoggerAdapter adapter) {
        if (adapter != null) {
            Logger logger = adapter.getLogger(LoggerFactory.class.getName());
            logger.info("using logger:" + adapter.getClass().getName());
            for (Map.Entry<String, FailsafeLogger> entry : LOGGERS.entrySet()) {
                entry.getValue().setLogger(logger);
            }
        }
    }

    public static LoggerAdapter getLoggerAdapter() {
        return LOGGER_ADAPTER;
    }

    public static Logger getLogger(Class<?> cls) {
        FailsafeLogger l = LOGGERS.get(cls.getName());
        if (l == null) {
            LOGGERS.putIfAbsent(cls.getName(), new FailsafeLogger(LOGGER_ADAPTER.getLogger(cls)));
            l = LOGGERS.get(cls.getName());
        }
        return l;
    }

    public static Logger getLogger(String key) {
        FailsafeLogger logger = LOGGERS.get(key);
        if (logger == null) {
            LOGGERS.putIfAbsent(key, new FailsafeLogger(LOGGER_ADAPTER.getLogger(key)));
            logger = LOGGERS.get(key);
        }
        return logger;
    }

    public static void setLevel(Level level) {
        LOGGER_ADAPTER.setLevel(level);
    }

    public static Level getLevel() {
        return LOGGER_ADAPTER.getLevel();
    }

    public static File getFile() {
        return LOGGER_ADAPTER.getFile();
    }
}
