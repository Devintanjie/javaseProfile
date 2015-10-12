package com.taobao.bird.common.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * @desc
 * @author junyu 2015年8月3日上午11:05:55
 * @version
 **/
public class DisruptorMultiProducerTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testMultiProducer() throws InterruptedException {
        Executor executor = Executors.newFixedThreadPool(16);
        DataEventFactory f = new DataEventFactory();
        int bufferSize = 512;
        Disruptor<DataEvent> d = new Disruptor<>(f,
            bufferSize,
            executor,
            ProducerType.SINGLE,
            new SleepingWaitStrategy());
        d.handleEventsWith(new DataEventHandler());
        d.start();
        RingBuffer<DataEvent> ringBuffer = d.getRingBuffer();
        DataEventProducer producer = new DataEventProducer(ringBuffer);

        long c = System.currentTimeMillis();
        for (long l = 0; l < 511; l++) {
            producer.onData(l);
        }

        long e = System.currentTimeMillis();

        Thread.sleep(30000);
        System.out.println(d.getRingBuffer().getMinimumGatingSequence());
        System.out.println("total time:" + (e - c) + " ms");
    }
}
