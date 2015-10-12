package com.taobao.bird.extractor.binlog.event;

import com.taobao.bird.extractor.binlog.LogBuffer;
import com.taobao.bird.extractor.binlog.LogEvent;

/**
 * @desc
 * @author junyu
 * @version
 **/
public class HeartbeatLogEvent extends LogEvent {

    public static final int FN_REFLEN = 512; /* Max length of full path-name */
    private int             identLen;
    private String          logIdent;

    public HeartbeatLogEvent(LogHeader header, LogBuffer buffer, FormatDescriptionLogEvent descriptionEvent){
        super(header);

        final int commonHeaderLen = descriptionEvent.commonHeaderLen;
        identLen = buffer.limit() - commonHeaderLen;
        if (identLen > FN_REFLEN - 1) {
            identLen = FN_REFLEN - 1;
        }

        logIdent = buffer.getFullString(commonHeaderLen, identLen, LogBuffer.ISO_8859_1);
    }

    public int getIdentLen() {
        return identLen;
    }

    public String getLogIdent() {
        return logIdent;
    }

}
