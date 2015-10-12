package com.taobao.bird.extractor.binlog.event;

import com.taobao.bird.extractor.binlog.LogEvent;

/**
 * @desc
 * @author junyu 2015年10月2日下午4:06:43
 * @version
 **/
public final class UnknownLogEvent extends LogEvent {

    public UnknownLogEvent(LogHeader header){
        super(header);
    }
}
