package com.taobao.bird.extractor.binlog.event;

import com.taobao.bird.extractor.binlog.LogBuffer;

/**
 * @desc
 * @author junyu
 * @version
 **/
public final class WriteRowsLogEvent extends RowsLogEvent {

    public WriteRowsLogEvent(LogHeader header, LogBuffer buffer, FormatDescriptionLogEvent descriptionEvent){
        super(header, buffer, descriptionEvent);
    }
}
