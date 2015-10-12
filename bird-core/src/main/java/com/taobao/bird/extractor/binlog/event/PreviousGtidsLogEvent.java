package com.taobao.bird.extractor.binlog.event;

import com.taobao.bird.extractor.binlog.LogBuffer;
import com.taobao.bird.extractor.binlog.LogEvent;

/**
 * @desc
 * @author junyu
 * @version
 **/
public class PreviousGtidsLogEvent extends LogEvent {

    public PreviousGtidsLogEvent(LogHeader header, LogBuffer buffer, FormatDescriptionLogEvent descriptionEvent){
        super(header);
        // do nothing , just for mysql gtid search function
    }
}
