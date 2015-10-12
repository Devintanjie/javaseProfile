package com.taobao.bird.checker.mysql;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.jute.Record;

import com.taobao.bird.applier.Applier;
import com.taobao.bird.common.lifecycle.BirdLifeCycle;

/**
 * @desc
 * @author junyu 2015年10月4日下午11:09:43
 * @version
 **/
public class BatchRecordChecker implements Applier<List<Record>>, BirdLifeCycle {

    private AtomicBoolean inited  = new AtomicBoolean(false);
    private AtomicBoolean started = new AtomicBoolean(false);   

    @Override
    public void apply(List<Record> data) {
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
