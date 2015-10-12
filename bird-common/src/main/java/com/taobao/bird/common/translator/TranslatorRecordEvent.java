package com.taobao.bird.common.translator;

import java.util.ArrayList;
import java.util.List;

import com.taobao.bird.common.model.runtime.BirdRecord;

/**
 * @description
 * @author <a href="junyu@taobao.com">junyu</a>
 * @date 2013-7-12下午04:14:30
 */
public class TranslatorRecordEvent {

    private List<String>     shardKeys;
    private List<String>     primaryKeys;
    private String           logicTable;
    private List<BirdRecord> result;

    // 兼容旧的转换类
    public void setShardKey(String shardKey) {
        shardKeys = new ArrayList<String>();
        shardKeys.add(shardKey);
    }

    public String getLogicTable() {
        return logicTable;
    }

    public List<String> getShardKeys() {
        return shardKeys;
    }

    public void setShardKeys(List<String> shardKeys) {
        this.shardKeys = shardKeys;
    }

    public void setLogicTable(String logicTable) {
        this.logicTable = logicTable;
    }

    public List<BirdRecord> getResult() {
        return result;
    }

    public void setResult(List<BirdRecord> result) {
        this.result = result;
    }

    public List<String> getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(List<String> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }
}
