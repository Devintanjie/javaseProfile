package com.taobao.bird.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @desc
 * @author junyu 2015��2��16������1:31:32
 * @version
 **/
public class BirdToStringStyle extends ToStringStyle {

    private static final long         serialVersionUID = -171763461752684801L;
    private static final String       DEFAULT_TIME     = "yyyy-MM-dd HH:mm:ss";
    private static final String       DEFAULT_DAY      = "yyyy-MM-dd";

    /**
     * <pre>
     * �����ʽ��
     * Person[name=John Doe,age=33,smoker=false ,time=2010-04-01 00:00:00]
     * </pre>
     */
    public static final ToStringStyle TIME_STYLE       = new OtterDateStyle(DEFAULT_TIME);

    /**
     * <pre>
     * �����ʽ��
     * Person[name=John Doe,age=33,smoker=false ,day=2010-04-01]
     * </pre>
     */
    public static final ToStringStyle DAY_STYLE        = new OtterDateStyle(DEFAULT_DAY);

    /**
     * <pre>
     * �����ʽ��
     * Person[name=John Doe,age=33,smoker=false ,time=2010-04-01 00:00:00]
     * </pre>
     */
    public static final ToStringStyle DEFAULT_STYLE    = BirdToStringStyle.TIME_STYLE;

    // =========================== �Զ���style =============================

    /**
     * ֧�����ڸ�ʽ����ToStringStyle
     * 
     * @author li.jinl
     */
    private static class OtterDateStyle extends ToStringStyle {

        private static final long serialVersionUID = 5208917932254652886L;

        // ����format��ʽ
        private String            pattern;

        public OtterDateStyle(String pattern){
            super();
            this.setUseShortClassName(true);
            this.setUseIdentityHashCode(false);
            // ��������format��ʽ
            this.pattern = pattern;
        }

        protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
            // �����Զ����date������
            if (value instanceof Date) {
                value = new SimpleDateFormat(pattern).format(value);
            }
            // �����������������Զ��������
            buffer.append(value);
        }
    }
}
