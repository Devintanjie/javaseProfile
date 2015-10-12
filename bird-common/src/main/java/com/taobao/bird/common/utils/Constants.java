package com.taobao.bird.common.utils;

/**
 * @desc
 * @author junyu 2015年2月16日下午1:03:15
 * @version
 **/
public class Constants {

    public static final String LOGGER_ALERT        = "LOGGER_ALERT";

    public static final String LOGGER_MYSQL_CONFIG = "LOGGER_MYSQL_CONFIG";

    public static final String BIRD_DATE_FORMAT    = "yyyy-MM-dd HH:mm:ss";

    public static final String DEFAULT_LOG_LAYOUT  = "%c %d %-5p - %m%n%n";

    public static final String ERROR_LOG_LAYOUT    = "%d - %m%n";

    public static final String PROFILE_LOG_LAYOUT  = "%m%n";

    public static final String CONFIG_LOG_LAYOUT   = "%m%n";

    public static final String TASK_LOG_FILE_PATH  = ".." + System.getProperty("file.separator") + "logs"
                                                     + System.getProperty("file.separator") + "task-logs";
}
