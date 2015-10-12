package com.taobao.bird.common.translator;

import java.util.List;

import com.taobao.bird.common.model.runtime.BirdRecord;


/**
 * @desc 
 * @author junyu 
 * @version 
 **/
public interface RecordTranslator {
    public List<TranslatorRecordEvent> fullChange(List<BirdRecord> records,String table,String logicTable);
}
