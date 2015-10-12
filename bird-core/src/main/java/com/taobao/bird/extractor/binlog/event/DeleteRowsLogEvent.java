package com.taobao.bird.extractor.binlog.event;

import com.taobao.bird.extractor.binlog.LogBuffer;

/**
 * @desc
 * @author junyu 
 * @version
 **/
public final class DeleteRowsLogEvent extends RowsLogEvent {

    public DeleteRowsLogEvent(LogHeader header, LogBuffer buffer, FormatDescriptionLogEvent descriptionEvent){
        super(header, buffer, descriptionEvent);
    }
}
