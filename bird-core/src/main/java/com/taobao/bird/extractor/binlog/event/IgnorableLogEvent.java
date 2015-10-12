package com.taobao.bird.extractor.binlog.event;

import com.taobao.bird.extractor.binlog.LogBuffer;
import com.taobao.bird.extractor.binlog.LogEvent;

/**
 * @desc
 * @author junyu
 * @version
 **/
public class IgnorableLogEvent extends LogEvent {

    public IgnorableLogEvent(LogHeader header, LogBuffer buffer, FormatDescriptionLogEvent descriptionEvent){
        super(header);

        // do nothing , just ignore log event
    }
}
