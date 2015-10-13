package com.taobao.bird.core.ringbuffer;

import java.util.List;

import com.taobao.bird.common.model.runtime.ColumnMeta;

/**
 * @desc
 * @author junyu 2015年10月13日下午1:06:54
 * @version
 **/
public class TableMeta {

    private List<String>     primaryKeys;
    private List<ColumnMeta> columns;
    private String           table;
    private List<String>     shardKeys;

    public List<String> getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(List<String> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public List<ColumnMeta> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnMeta> columns) {
        this.columns = columns;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<String> getShardKeys() {
        return shardKeys;
    }

    public void setShardKeys(List<String> shardKeys) {
        this.shardKeys = shardKeys;
    }

}
