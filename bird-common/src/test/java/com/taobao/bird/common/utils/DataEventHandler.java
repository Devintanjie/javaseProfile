package com.taobao.bird.common.utils;

import com.lmax.disruptor.EventHandler;

/**
 * @desc
 * @author junyu 2015年8月3日上午11:16:31
 * @version
 **/
public class DataEventHandler implements EventHandler<DataEvent> {
    @Override
    public void onEvent(DataEvent event, long sequence, boolean endOfBatch) throws Exception {
        //业务处理需要100ms
        Thread.sleep(100);
    }
}
