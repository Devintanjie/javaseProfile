package com.taobao.bird.core.ringbuffer;

import javax.sql.DataSource;

/**
 * @desc
 * @author junyu 2015年10月13日下午2:26:57
 * @version
 **/
public class RuntimeConfig {

    private Object     lastId;
    private DataSource sourceDs;
    private TableMeta  meta;
    private int        batchTime;
    private boolean    isLastBatch;

    public Object getLastId() {
        return lastId;
    }

    public void setLastId(Object lastId) {
        this.lastId = lastId;
    }

    public DataSource getSourceDs() {
        return sourceDs;
    }

    public void setSourceDs(DataSource sourceDs) {
        this.sourceDs = sourceDs;
    }

    public TableMeta getMeta() {
        return meta;
    }

    public void setMeta(TableMeta meta) {
        this.meta = meta;
    }

    public int getBatchTime() {
        return batchTime;
    }

    public void setBatchTime(int batchTime) {
        this.batchTime = batchTime;
    }

    public void incrementBatchTime() {
        this.batchTime += 1;
    }

    public void resetBatchTime() {
        this.batchTime = 0;
    }

    public boolean isLastBatch() {
        return isLastBatch;
    }

    public void setLastBatch(boolean isLastBatch) {
        this.isLastBatch = isLastBatch;
    }
}
