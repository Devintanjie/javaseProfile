package com.taobao.bird.common.log.dynamic;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.taobao.bird.common.log.Logger;

/**
 * @desc
 * @author junyu 2015年10月6日上午11:08:33
 * @version
 **/
public class TraceProfile {

    protected static Logger                                                                         logger;
    private static volatile ConcurrentHashMap<String/* key */, AtomicLong/* count */>               countMap           = new ConcurrentHashMap<String, AtomicLong>();
    private static volatile ConcurrentHashMap<String/* key */, ScheduledExecutorService/* timer */> timerMap           = new ConcurrentHashMap<String, ScheduledExecutorService>();
    public static final String                                                                      DEFAULT_CUSTOM_KEY = "DEFAULT_KEY";
    private Object                                                                                  lock               = new Object();
    private Object                                                                                  lock2              = new Object();

    public void init(Logger log) throws IOException {
        logger = log;
    }

    public void add(String key, long ms) {
        if (key == null) {
            throw new IllegalArgumentException("customKey is null!");
        }

        AtomicLong count = countMap.get(key);
        if (null == count) {
            synchronized (lock) {
                if (null == countMap.get(key)) {
                    count = new AtomicLong(0);
                    countMap.putIfAbsent(key, count);
                    logger.info("add a count,customKey:" + key);
                } else {
                    count = countMap.get(key);
                }
            }
        }

        if (null == timerMap.get(key)) {
            synchronized (lock2) {
                if (null == timerMap.get(key)) {
                    ScheduledExecutorService ses = new ScheduledThreadPoolExecutor(1);
                    ses.scheduleAtFixedRate(new ProfileExportor(key), 0, 10, TimeUnit.SECONDS);
                    timerMap.put(key, ses);
                    logger.info("add a section profile,key:" + key);
                }
            }
        }

        count.addAndGet(ms);
    }

    public class ProfileExportor implements Runnable {

        private final String customKey;

        public ProfileExportor(String customKey){
            this.customKey = customKey;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public void run() {
            Calendar ca = Calendar.getInstance();
            StringBuilder sb = new StringBuilder();
            sb.append(format.format(ca.getTime()));
            sb.append((char) 1);
            sb.append(customKey);
            sb.append((char) 1);

            long lastCount = countMap.get(customKey).getAndSet(0);
            sb.append(lastCount);

            logger.info(sb.toString());
        }
    }
}
