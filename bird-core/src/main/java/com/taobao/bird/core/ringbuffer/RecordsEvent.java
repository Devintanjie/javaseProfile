package com.taobao.bird.core.ringbuffer;

import java.util.List;

import com.taobao.bird.common.model.runtime.BirdRecord;

/**
 * @desc
 * @author junyu 2015年10月13日下午12:27:12
 * @version
 **/
public class RecordsEvent {

    private List<BirdRecord> result;

    private RuntimeConfig    rc;

    private Object           id;

    public List<BirdRecord> getResult() {
        return result;
    }

    public void setResult(List<BirdRecord> result) {
        this.result = result;
    }

    public RuntimeConfig getRc() {
        return rc;
    }

    public void setRc(RuntimeConfig rc) {
        this.rc = rc;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

}
