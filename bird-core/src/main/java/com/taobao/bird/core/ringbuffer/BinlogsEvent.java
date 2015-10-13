package com.taobao.bird.core.ringbuffer;

import java.util.List;

import com.taobao.bird.extractor.binlog.LogEvent;
import com.taobao.bird.extractor.binlog.LogPosition;

/**
 * @desc
 * @author junyu 2015年10月13日下午12:27:27
 * @version
 **/
public class BinlogsEvent {

    private List<LogEvent> events;

    private LogPosition    position;

    public List<LogEvent> getEvents() {
        return events;
    }

    public void setEvents(List<LogEvent> events) {
        this.events = events;
    }

    public LogPosition getPosition() {
        return position;
    }

    public void setPosition(LogPosition position) {
        this.position = position;
    }
}
