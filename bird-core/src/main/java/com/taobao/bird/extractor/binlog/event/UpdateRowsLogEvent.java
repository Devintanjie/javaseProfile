package com.taobao.bird.extractor.binlog.event;

import com.taobao.bird.extractor.binlog.LogBuffer;

/**
 * @desc
 * @author junyu 2015��10��2������4:07:33
 * @version
 **/
public final class UpdateRowsLogEvent extends RowsLogEvent {

    public UpdateRowsLogEvent(LogHeader header, LogBuffer buffer, FormatDescriptionLogEvent descriptionEvent){
        super(header, buffer, descriptionEvent);
    }
}
