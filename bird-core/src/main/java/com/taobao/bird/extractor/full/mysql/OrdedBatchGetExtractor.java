package com.taobao.bird.extractor.full.mysql;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.jute.Record;

import com.google.common.collect.Lists;
import com.lmax.disruptor.RingBuffer;
import com.taobao.bird.common.datasource.DataSourceConfig;
import com.taobao.bird.common.lifecycle.BirdLifeCycle;
import com.taobao.bird.common.log.dynamic.BirdLogFactory;
import com.taobao.bird.common.model.config.MySQLTransferNode;
import com.taobao.bird.core.ringbuffer.RecordsEvent;
import com.taobao.bird.core.ringbuffer.RuntimeConfig;
import com.taobao.bird.core.ringbuffer.TableMeta;
import com.taobao.bird.extractor.Extractor;

/**
 * @desc
 * @author junyu
 * @version
 **/
public class OrdedBatchGetExtractor implements Extractor<ArrayList<Record>>, BirdLifeCycle {

    private AtomicBoolean            inited  = new AtomicBoolean(false);
    private AtomicBoolean            started = new AtomicBoolean(false);

    private RecordPosition           pos;
    private MySQLRecordFetcher       fetcher;
    private RingBuffer<RecordsEvent> ring;
    private MySQLTransferNode        node;
    private int                      batchCount;

    public OrdedBatchGetExtractor(MySQLTransferNode node, RecordPosition pos, RingBuffer<RecordsEvent> ring){
        this.node = node;
        this.pos = pos;
        this.ring = ring;
    }

    @Override
    public void init() {
        if (inited.compareAndSet(false, true)) {
            // TODO encode/decode
            if (node == null || StringUtils.isBlank(node.getSourceIp()) || StringUtils.isBlank(node.getSourcePort())
                || StringUtils.isBlank(node.getSourceUserName()) || StringUtils.isBlank(node.getSourceDbName())
                || StringUtils.isBlank(node.getSourcePasswd())) {
                throw new IllegalArgumentException("source db config some prop is empty(ip or port or user or password or dbname),please check,please check.");
            }

            fetcher = new MySQLRecordFetcher(node.getSourceEncoding(), node.getTargetEncoding(), batchCount);
        }
    }

    @Override
    public void extract() {
        try {
            RuntimeConfig rc = new RuntimeConfig();
            List<DataSourceConfig> dsc = Lists.newArrayList();
            Object lastId = null;// TODO
            for (DataSourceConfig c : dsc) {
                DataSource ds = null;// TODO
                List<TableMeta> metas = new ArrayList<TableMeta>();// TODO
                for (TableMeta m : metas) {
                    rc.setLastId(lastId);
                    rc.setSourceDs(ds);
                    rc.setMeta(m);
                    rc.setLastBatch(false);
                    while (!Thread.currentThread().isInterrupted() && !this.isStop()) {
                        long lastSeq = ring.next();
                        RecordsEvent e = ring.get(lastSeq);
                        fetcher.fetch(e);
                        ring.publish(lastSeq);
                        // TODO record the position
                        if (e.getRc().isLastBatch()) {
                            break;
                        }
                    }
                }
            }

            // TODO wait to complete
        } catch (Exception e) {
            // TODO
        }
    }

    @Override
    public void start() {
        if (!inited.get()) {
            throw new IllegalArgumentException("please init first before start.");
        }

        if (started.compareAndSet(false, true)) {
            BirdLogFactory.getTaskLog().info("full record extractor started.");
            this.extract();
        }
    }

    @Override
    public void stop() {
        if (started.compareAndSet(true, false)) {
            BirdLogFactory.getTaskLog().info("full record extractor stoped.");
        }
    }

    @Override
    public void abort(String why, Throwable e) {
        Thread.currentThread().interrupt();
        this.stop();
        BirdLogFactory.getTaskLog().info("full record extractor abort.");
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
