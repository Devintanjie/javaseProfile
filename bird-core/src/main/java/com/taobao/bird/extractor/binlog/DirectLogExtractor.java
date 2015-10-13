package com.taobao.bird.extractor.binlog;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;
import com.lmax.disruptor.RingBuffer;
import com.taobao.bird.common.datasource.DataSourceConfig;
import com.taobao.bird.common.lifecycle.BirdLifeCycle;
import com.taobao.bird.common.log.dynamic.BirdLogFactory;
import com.taobao.bird.core.ringbuffer.BinlogsEvent;
import com.taobao.bird.extractor.Extractor;

/**
 * @desc
 * @author junyu
 * @version
 **/
public class DirectLogExtractor implements Extractor<LogEvent>, BirdLifeCycle {

    private AtomicBoolean            inited  = new AtomicBoolean(false);
    private AtomicBoolean            started = new AtomicBoolean(false);

    private DataSourceConfig         source;
    private LogPosition              pos;
    private String                   uniqueId;
    private DirectLogFetcher         fetcher;
    private LogDecoder               decoder = new LogDecoder();
    private LogContext               context = new LogContext();
    private RingBuffer<BinlogsEvent> ring;

    public DirectLogExtractor(DataSourceConfig source, LogPosition pos, String uniqueId, RingBuffer<BinlogsEvent> ring){
        this.source = source;
        this.pos = pos;
        this.uniqueId = uniqueId;
        this.ring = ring;
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
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                Connection conn = DriverManager.getConnection("jdbc:mysql://" + source.getIp() + ":" + source.getPort()
                                                              + "/" + source.getDbName(),
                    source.getUser(),
                    source.getPasswd());
                Statement statement = conn.createStatement();
                statement.executeQuery("set @master_binlog_checksum= '@@global.binlog_checksum'");
                statement.close();

                if (pos == null || StringUtils.isBlank(pos.getFileName())) {
                    Statement s = conn.createStatement();
                    ResultSet result = s.executeQuery("show master status");
                    if (result.next()) {
                        String file = result.getString("File");
                        long position = result.getLong("Position");
                        pos = new LogPosition(file, position);
                    } else {
                        throw new RuntimeException("'show master status' return empty record,please check,ip:"
                                                   + source.getIp() + ",port:" + source.getPort() + ",dbname:"
                                                   + source.getDbName());
                    }
                }

                long slaveId = generateUniqueServerId();
                BirdLogFactory.getTaskLog().info("[config] auto-generated server_id: " + slaveId + ",binlog_file:"
                                                 + pos.getFileName() + ",binlog_position:" + pos.getPosition());
                fetcher.open(conn, pos.getFileName(), pos.getPosition(), slaveId);
                context.setLogPosition(pos);
            } catch (Exception e) {
                throw new RuntimeException("exception when open source db for dump binlog.", e);
            } finally {
                if (fetcher != null) {
                    try {
                        fetcher.close();
                    } catch (IOException e) {
                        BirdLogFactory.getTaskLog().error("fetcher close error.but ignore it.", e);
                    }
                }
            }
        }
    }

    private final long generateUniqueServerId() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            byte[] addr = localHost.getAddress();
            int salt = (uniqueId != null) ? uniqueId.hashCode() : 0;
            return ((0x7f & salt) << 24) + ((0xff & (int) addr[1]) << 16) // NL
                   + ((0xff & (int) addr[2]) << 8) // NL
                   + (0xff & (int) addr[3]);
        } catch (UnknownHostException e) {
            throw new RuntimeException("Unknown host", e);
        }
    }

    @Override
    public void extract() {
        try {
            while (!Thread.currentThread().isInterrupted() && !this.isStop()) {
                fetcher.fetch();
                LogEvent e = decoder.decode(fetcher, context);
                long lastSeq = ring.next();
                BinlogsEvent br = ring.get(lastSeq);
                List<LogEvent> events = Lists.newArrayList();
                events.add(e);
                br.setEvents(events);
                ring.publish(lastSeq);
            }
        } catch (IOException e) {
            throw new RuntimeException("exception when dump binlog.", e);
        }
    }

    @Override
    public void start() {
        if (!inited.get()) {
            throw new IllegalArgumentException("please init first before start.");
        }

        if (started.compareAndSet(false, true)) {
            BirdLogFactory.getTaskLog().info("direct log extractor started.");
            this.extract();
        }
    }

    @Override
    public void stop() {
        if (started.compareAndSet(true, false)) {
            if (fetcher != null) {
                try {
                    fetcher.close();
                } catch (IOException e) {
                    BirdLogFactory.getTaskLog().error("fetcher close error.but ignore it.", e);
                }
            }

            BirdLogFactory.getTaskLog().info("direct log extractor stoped.");
        }
    }

    @Override
    public void abort(String why, Throwable e) {
        Thread.currentThread().interrupt();
        this.stop();
        BirdLogFactory.getTaskLog().info("direct log extractor abort.");
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
