package com.taobao.bird.common.utils;

import com.lmax.disruptor.EventFactory;

/**
 * @desc
 * @author junyu 2015年8月3日上午11:15:08
 * @version
 **/
public class DataEventFactory implements EventFactory<DataEvent> {

    @Override
    public DataEvent newInstance() {
        return new DataEvent();
    }
}
