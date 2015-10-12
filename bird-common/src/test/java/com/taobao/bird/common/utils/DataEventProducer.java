package com.taobao.bird.common.utils;

import com.lmax.disruptor.RingBuffer;

/**
 * @desc
 * @author junyu 2015年8月3日上午11:17:59
 * @version
 **/
public class DataEventProducer {

    private final RingBuffer<DataEvent> ringBuffer;

    public DataEventProducer(RingBuffer<DataEvent> ringBuffer){
        this.ringBuffer = ringBuffer;
    }

    public void onData(Long data) throws InterruptedException {
        // 业务处理需要时间
        Thread.sleep(10);
        long seq = ringBuffer.next();
        try {
            DataEvent e = ringBuffer.get(seq);
            e.setData(data);
        } finally {
            ringBuffer.publish(seq);
        }
    }
}
