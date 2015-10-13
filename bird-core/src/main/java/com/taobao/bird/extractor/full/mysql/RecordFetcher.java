package com.taobao.bird.extractor.full.mysql;

import com.taobao.bird.core.ringbuffer.RecordsEvent;

/**
 * @desc
 * @author junyu 2015年10月13日下午1:03:37
 * @version
 **/
public interface RecordFetcher {

    public void fetch(RecordsEvent event) throws Exception;
}
