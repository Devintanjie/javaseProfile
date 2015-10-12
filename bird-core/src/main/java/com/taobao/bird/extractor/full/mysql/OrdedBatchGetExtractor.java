package com.taobao.bird.extractor.full.mysql;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.jute.Record;

import com.taobao.bird.common.lifecycle.BirdLifeCycle;
import com.taobao.bird.extractor.Extractor;

/**
 * @desc
 * @author junyu
 * @version
 **/
public class OrdedBatchGetExtractor implements Extractor<ArrayList<Record>>, BirdLifeCycle {

    private AtomicBoolean inited  = new AtomicBoolean(false);
    private AtomicBoolean started = new AtomicBoolean(false);

    @Override
    public void init() {
        if(inited.compareAndSet(false, true)){
            
        }
    }

    @Override
    public ArrayList<Record> extract() {
        return null;
    }

    @Override
    public void start() {
        if(started.compareAndSet(false, true)){
            
        }
    }

    @Override
    public void stop() {
        if(started.compareAndSet(true, false)){
            
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
