package com.taobao.bird.extractor.binlog.event;

import com.taobao.bird.extractor.binlog.LogEvent;

/**
 * @desc
 * @author junyu 
 * @version
 **/
public final class UnknownLogEvent extends LogEvent {

    public UnknownLogEvent(LogHeader header){
        super(header);
    }
}
