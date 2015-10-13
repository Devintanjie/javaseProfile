package com.taobao.bird.core.processor;

import java.util.concurrent.atomic.AtomicBoolean;

import com.taobao.bird.common.datasource.DataSourceConfig;
import com.taobao.bird.common.lifecycle.BirdLifeCycle;
import com.taobao.bird.common.log.dynamic.BirdLogFactory;
import com.taobao.bird.common.model.runtime.BirdRecord;

/**
 * @desc
 * @author junyu 2015年10月13日上午9:17:33
 * @version
 **/
public class MySQLRecordProcessor implements Processor<BirdRecord>, BirdLifeCycle {

    private AtomicBoolean          inited  = new AtomicBoolean(false);
    private AtomicBoolean          started = new AtomicBoolean(false);
    private final DataSourceConfig source;

    public MySQLRecordProcessor(DataSourceConfig source){
        this.source = source;
    }

    @Override
    public void init() {
        if (inited.compareAndSet(false, true)) {
            // TODO
        }
    }

    @Override
    public void work() {
        // TODO Auto-generated method stub
    }

    @Override
    public void start() {
        if (!inited.get()) {
            throw new IllegalArgumentException("please init first before start.");
        }

        if (started.compareAndSet(false, true)) {
            BirdLogFactory.getTaskLog().info("mysql record processor started.");
        }
    }

    @Override
    public void stop() {
        if (started.compareAndSet(true, false)) {
            BirdLogFactory.getTaskLog().info("mysql record processor stoped.");
        }
    }

    @Override
    public void abort(String why, Throwable e) {
        Thread.currentThread().interrupt();
        this.stop();
        BirdLogFactory.getTaskLog().info("mysql record processor abort.");
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
