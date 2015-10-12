package com.taobao.bird.common.log.dynamic;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import com.taobao.bird.common.log.Logger;
import com.taobao.bird.common.model.config.SimpleNode;

/**
 * @desc
 * @author junyu 
 * @version
 **/
public abstract class BirdDynamicLogger {

    private String encode = "UTF-8";

    public String getLogPath(SimpleNode node) {
        String rootLog = System.getProperty("BIRD.LOG.PATH");
        if (StringUtils.isEmpty(rootLog)) {
            rootLog = System.getProperty("user.home");
        }

        if (!rootLog.endsWith(File.separator)) {
            rootLog += File.separator;
        }

        StringBuilder path = new StringBuilder(rootLog);
        if (!StringUtils.containsIgnoreCase(rootLog, "logs")) {
            path.append("logs").append(File.separator);
        }

        path.append("yugong").append(File.separator);
        path.append("task-logs").append(File.separator);
        path.append(node.getNodeId()).append(File.separator);
        String file = path.toString();
        File dir = new File(file);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return file;
    }

    protected String getEncoding() {
        return encode != null ? encode : System.getProperty("file.encoding", "UTF-8");
    }

    public abstract Logger initLogger(SimpleNode simpleNode, Logger logger, AppenderType type, String fileName,
                                    String pattern);

    public enum AppenderType {
        SIZED_ROLLING_APPENDER, DAILY_ROLLING_APPENDER
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }
}
