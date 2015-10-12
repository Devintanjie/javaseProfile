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
 * @author junyu
 * @version
 **/
public class TpsProfile {

    private static Logger                                                                                   log;
    private static volatile ConcurrentHashMap<String/* customerKey */, AtomicLong/* count */>               countMap           = new ConcurrentHashMap<String, AtomicLong>();
    private static volatile ConcurrentHashMap<String/* customerKey */, ScheduledExecutorService/* timer */> timerMap           = new ConcurrentHashMap<String, ScheduledExecutorService>();

    private static ConcurrentHashMap<String/* customerKey */, AtomicLong>                                   transfered         = new ConcurrentHashMap<String, AtomicLong>();

    public static final String                                                                              DEFAULT_CUSTOM_KEY = "DEFAULT_KEY";
    private Object                                                                                          lock               = new Object();
    private Object                                                                                          lock2              = new Object();

    public void init(Logger logger) throws IOException {
        log = logger;
    }

    public void increment() {
        String customKey = DEFAULT_CUSTOM_KEY;
        increment(customKey);
    }

    public void increment(String customKey) {
        increment(customKey, 1);
    }

    public void increment(String customKey, long number) {
        if (customKey == null) {
            customKey = DEFAULT_CUSTOM_KEY;
        }

        AtomicLong count = countMap.get(customKey);
        if (null == count) {
            synchronized (lock) {
                if (null == countMap.get(customKey)) {
                    count = new AtomicLong(0);
                    countMap.putIfAbsent(customKey, count);
                    log.info("add a count,customKey:" + customKey);
                } else {
                    count = countMap.get(customKey);
                }
            }
        }

        if (null == timerMap.get(customKey)) {
            synchronized (lock2) {
                if (null == timerMap.get(customKey)) {
                    ScheduledExecutorService ses = new ScheduledThreadPoolExecutor(1);
                    ses.scheduleAtFixedRate(new ProfileExportor(customKey), 0, 10, TimeUnit.SECONDS);
                    timerMap.put(customKey, ses);
                    log.info("add a ScheduledExecutorService,customKey:" + customKey);
                }
            }
        }

        count.addAndGet(number);
    }

    public class ProfileExportor implements Runnable {

        private final String customKey;

        public ProfileExportor(String customKey){
            this.customKey = customKey;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public void run() {
            try {
                Calendar ca = Calendar.getInstance();
                StringBuilder sb = new StringBuilder();
                sb.append(format.format(ca.getTime()));
                sb.append(" ");
                sb.append((char) 1);
                sb.append(customKey);
                sb.append((char) 1);
                sb.append("(10s)");
                sb.append(" ");
                sb.append((char) 1);

                long lastCount = countMap.get(customKey).getAndSet(0L);

                sb.append(lastCount);

                log.info(sb.toString());

                if (transfered.get(customKey) == null) {
                    transfered.putIfAbsent(customKey, new AtomicLong(0));
                } else {
                    transfered.get(customKey).addAndGet(lastCount);
                }
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

    public long getIncrementByCustomKey(String customKey) {
        long increment = 0;
        if (transfered.get(customKey) != null) {
            increment = transfered.get(customKey).longValue();
            transfered.put(customKey, new AtomicLong(0));
        }

        return increment;
    }
}
