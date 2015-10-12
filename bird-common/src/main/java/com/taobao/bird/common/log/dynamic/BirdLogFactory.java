package com.taobao.bird.common.log.dynamic;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.StringUtils;

import com.taobao.bird.common.log.Logger;
import com.taobao.bird.common.log.LoggerFactory;
import com.taobao.bird.common.log.dynamic.BirdDynamicLogger.AppenderType;
import com.taobao.bird.common.model.config.SimpleNode;

/**
 * @desc
 * @author junyu 2015年10月6日上午11:03:33
 * @version
 **/
public class BirdLogFactory {

    protected static volatile Logger mainLog                    = LoggerFactory.getLogger("TASK_MAIN_LOG");
    protected static volatile Logger applyErrorLog              = LoggerFactory.getLogger("APPLY_ERROR_LOG");
    protected static volatile Logger increCommitLog             = LoggerFactory.getLogger("INCRE_COMMIT_LOG");
    protected static volatile Logger checkDiffLog               = LoggerFactory.getLogger("CHECK_DIFF_LOG");

    protected static Logger          defaultLog                 = LoggerFactory.getLogger("COMMON-LOG");
    protected static TpsProfile      tpsProfile                 = new TpsProfile();
    protected static TraceProfile    traceProfile               = new TraceProfile();

    public static final String       CHECK_FETCH_KEY            = "CHECK_FETCH";
    public static final String       CHECK_DETAIL_KEY           = "CHECK_DETAIL";

    public static final String       FULL_FETCH_KEY             = "FULL_FETCH";
    public static final String       FULL_APPLY_KEY             = "FULL_APPLY";

    public static final String       INCRE_FETCH_LOG_KEY        = "INCRE_FETCH_LOG";
    public static final String       INCRE_APPLY_LOG_KEY        = "INCRE_APPLY_LOG";

    public static final String       FULL_TRANSLATE_KEY         = "FULL_TRANSLATE";
    public static final String       INCRE_TRANSLATE_KEY        = "INCRE_TRANSLATE";
    public static final String       CHECK_TRANSLATE_KEY        = "CHECK_TRANSLATE";

    public static final String       TRACE_FULL_FETCH_KEY       = "TRACE_FULL_FETCH";
    public static final String       TRACE_FULL_TRANSLATE_KEY   = "TRACE_FULL_TRANSLATE";
    public static final String       TRACE_FULL_BATCH_APPLY_KEY = "TRACE_FULL_BATCH_APPLY";

    public static final String       TRACE_INCRE_FETCH_KEY      = "TRACE_INCRE_FETCH_LOG";
    public static final String       TRACE_INCRE_TRANSLATE_KEY  = "TRACE_INCRE_TRANSLATE";
    public static final String       TRACE_INCRE_APPLY_KEY      = "TRACE_INCRE_APPLY";

    public static final String       TRACE_CHECK_FETCH_KEY      = "TRACE_CHECK_FETCH_LOG";
    public static final String       TRACE_CHECK_TRANSLATE_KEY  = "TRACE_CHECK_TRANSLATE";
    public static final String       TRACE_CHECK_DETAIL_KEY     = "TRACE_CHECK_DETAIL";

    private static AtomicBoolean     inited                     = new AtomicBoolean(false);

    private static BirdDynamicLogger buildDynamic(Logger logger) {
        boolean canUseEncoder;
        try {
            // logback从0.9.19开始采用encoder
            // http://logback.qos.ch/manual/encoders.html
            Class.forName("ch.qos.logback.classic.encoder.PatternLayoutEncoder");
            canUseEncoder = true;
        } catch (ClassNotFoundException e) {
            canUseEncoder = false;
        }

        BirdDynamicLogger dynamic = null;
        String LOGBACK = "logback";
        String LOG4J = "log4j";

        // slf4j只是一个代理工程，需要判断一下具体的实现类
        if (checkLogger(logger, LOGBACK)) {
            if (canUseEncoder) {
                dynamic = new BirdLogbackDynamicLogger();
            } else {
                dynamic = new BirdLogback918DynamicLogger();
            }
        } else if (checkLogger(logger, LOG4J)) {
            dynamic = new BirdLog4jDynamicLogger();
        } else {
            logger.warn("logger is not a log4j/logback instance, dynamic logger is disabled");
        }

        return dynamic;
    }

    private static boolean checkLogger(Logger logger, String name) {
        return StringUtils.contains(logger.getDelegate().getClass().getName(), name);
    }

    public static void init(SimpleNode node) throws IOException {
        if (inited.compareAndSet(false, true)) {
            BirdDynamicLogger dynamic = buildDynamic(defaultLog);
            tpsProfile.init(dynamic.initLogger(node,
                LoggerFactory.getLogger("TPS_PROFILE"),
                AppenderType.DAILY_ROLLING_APPENDER,
                "tps_profile.log",
                "m%n%"));
            traceProfile.init(dynamic.initLogger(node,
                LoggerFactory.getLogger("TRACE_PROFILE"),
                AppenderType.DAILY_ROLLING_APPENDER,
                "trace_profile.log",
                "m%n%"));
            mainLog = dynamic.initLogger(node, mainLog, AppenderType.DAILY_ROLLING_APPENDER, "main.log", null);
            applyErrorLog = dynamic.initLogger(node,
                mainLog,
                AppenderType.SIZED_ROLLING_APPENDER,
                "apply_error.log",
                null);
            increCommitLog = dynamic.initLogger(node,
                mainLog,
                AppenderType.DAILY_ROLLING_APPENDER,
                "incre_commit.log",
                null);
            checkDiffLog = dynamic.initLogger(node,
                mainLog,
                AppenderType.SIZED_ROLLING_APPENDER,
                "check_diff.log",
                null);
        }
    }

    public static Logger getTaskLog() {
        if (mainLog != null) {
            return mainLog;
        } else {
            return defaultLog;
        }
    }

    public static Logger getApplyErrorLog() {
        if (applyErrorLog != null) {
            return applyErrorLog;
        } else {
            return defaultLog;
        }
    }

    public static Logger getIncreCommitLog() {
        if (increCommitLog != null) {
            return increCommitLog;
        } else {
            return defaultLog;
        }
    }

    public static Logger getCheckDiffLog() {
        if (checkDiffLog != null) {
            return checkDiffLog;
        } else {
            return defaultLog;
        }
    }

    public static TpsProfile getTpsProfile() {
        if (tpsProfile == null) {
            throw new RuntimeException("should init LogFactory before use it!");
        }

        return tpsProfile;
    }

    public static TraceProfile getTraceProfile() {
        if (traceProfile == null) {
            throw new RuntimeException("should init LogFactory before use it!");
        }

        return traceProfile;
    }
}
