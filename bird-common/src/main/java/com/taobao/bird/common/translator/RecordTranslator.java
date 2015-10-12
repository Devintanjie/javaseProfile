package com.taobao.bird.common.translator;

import java.util.List;

import com.taobao.bird.common.model.runtime.BirdRecord;


/**
 * @desc 
 * @author junyu 2015��2��17������12:28:42
 * @version 
 **/
public interface RecordTranslator {
    /**
     * ȫ��ת���ӿ�
     * @param records һ����¼������������ת��
     * @param db  ������¼������db
     * @param table ������¼������table(��ʵ��)
     * @param logicTable �߼���������Ƿֱ�ģ���ô��ʵ����߼���᲻һ��
     * @return
     */
    public List<TranslatorRecordEvent> fullChange(List<BirdRecord> records,String table,String logicTable);
}
