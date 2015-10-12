package com.taobao.bird.common.translator;

import java.util.List;

import com.taobao.bird.common.model.runtime.BirdRecord;


/**
 * @desc 
 * @author junyu 2015年2月17日下午12:28:42
 * @version 
 **/
public interface RecordTranslator {
    /**
     * 全量转换接口
     * @param records 一批记录，可以逐条做转换
     * @param db  这批记录所属的db
     * @param table 这批记录所属的table(真实表)
     * @param logicTable 逻辑表，如果表是分表的，那么真实表和逻辑表会不一致
     * @return
     */
    public List<TranslatorRecordEvent> fullChange(List<BirdRecord> records,String table,String logicTable);
}
