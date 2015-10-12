package com.taobao.bird.extractor.binlog;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.StringUtils;

import com.taobao.bird.common.datasource.DataSourceConfig;
import com.taobao.bird.common.lifecycle.BirdLifeCycle;
import com.taobao.bird.extractor.Extractor;

/**
 * @desc
 * @author junyu 2015年10月4日下午10:52:53
 * @version
 **/
public class DirectLogExtractor implements Extractor<LogEvent>, BirdLifeCycle {

    private AtomicBoolean    inited  = new AtomicBoolean(false);
    private AtomicBoolean    started = new AtomicBoolean(false);

    private DataSourceConfig source;
    private LogPosition      pos;
    private DirectLogFetcher fetcher;
    private LogDecoder       decoder = new LogDecoder();
    private LogContext       context = new LogContext();

    public DirectLogExtractor(DataSourceConfig source, LogPosition pos){
        this.source = source;
        this.pos = pos;
    }

    @Override
    public void init() {
        if (inited.compareAndSet(false, true)) {
            if (source == null || StringUtils.isBlank(source.getIp()) || StringUtils.isBlank(source.getPort())
                || StringUtils.isBlank(source.getUser()) || StringUtils.isBlank(source.getDbName())
                || StringUtils.isBlank(source.getPasswd())) {
                throw new IllegalArgumentException("source db config some prop is empty(ip or port or user or password or dbname),please check,please check.");
            }

            try {
                fetcher = new DirectLogFetcher();
                // TODO:加入主备切换
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/binlog_test",
                    "bucket",
                    "74123963");
                Statement statement = conn.createStatement();
                statement.executeQuery("set @master_binlog_checksum= '@@global.binlog_checksum'");

                if (pos == null || StringUtils.isBlank(pos.getFileName())) {
                    // TODO:show master status and get the pos;
                }

                // TODO: get master id
                int masterId = 7;
                fetcher.open(conn, pos.getFileName(), pos.getPosition(), masterId);
                context.setLogPosition(pos);
            } catch (Exception e) {
                throw new RuntimeException("Exception when open source db for dump binlog.", e);
            } finally {
                if (fetcher != null) {
                    try {
                        fetcher.close();
                    } catch (IOException e) {
                        // TODO: log it
                        // ignore it;
                    }
                }
            }
        }
    }

    @Override
    public LogEvent extract() {
        try {
            // TODO:加入容灾
            fetcher.fetch();
            LogEvent e = decoder.decode(fetcher, context);
            return e;
        } catch (IOException e) {
            throw new RuntimeException("exception when dump binlog.", e);
        }
    }

    @Override
    public void start() {
        if (started.compareAndSet(false, true)) {
            // TODO: just log it ,do nothing
        }
    }

    @Override
    public void stop() {
        if (started.compareAndSet(true, false)) {
            if (fetcher != null) {
                try {
                    fetcher.close();
                } catch (IOException e) {
                    // TODO:log it
                    // ignore it;
                }
            }
        }
    }

    @Override
    public void abort(String why, Throwable e) {
        // TODO: log it;
        Thread.currentThread().interrupt();
        this.stop();
    }

    @Override
    public boolean isStart() {
        return started.get();
    }

    @Override
    public boolean isStop() {
        return !started.get();
    }
}
