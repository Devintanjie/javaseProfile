package com.taobao.bird.extractor.binlog.event;

import com.taobao.bird.extractor.binlog.LogBuffer;
import com.taobao.bird.extractor.binlog.LogEvent;

/**
 * @desc
 * @author junyu 2015��10��2������4:05:51
 * @version
 **/
public final class StopLogEvent extends LogEvent {

    public StopLogEvent(LogHeader header, LogBuffer buffer, FormatDescriptionLogEvent description_event){
        super(header);
    }
}
