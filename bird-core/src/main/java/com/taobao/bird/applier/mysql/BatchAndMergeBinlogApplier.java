package com.taobao.bird.applier.mysql;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import com.taobao.bird.applier.Applier;
import com.taobao.bird.common.lifecycle.BirdLifeCycle;
import com.taobao.bird.extractor.binlog.LogEvent;

/**
 * @desc
 * @author junyu 
 * @version
 **/
public class BatchAndMergeBinlogApplier implements Applier<ArrayList<LogEvent>>, BirdLifeCycle {

    private AtomicBoolean inited  = new AtomicBoolean(false);
    private AtomicBoolean started = new AtomicBoolean(false);

    @Override
    public void apply(ArrayList<LogEvent> data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void init() {
        if (inited.compareAndSet(false, true)) {

        }
    }

    @Override
    public void start() {
        if (started.compareAndSet(false, true)) {

        }
    }

    @Override
    public void stop() {
        if (started.compareAndSet(true, false)) {

        }
    }

    @Override
    public void abort(String why, Throwable e) {
        // TODO Auto-generated method stub

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
