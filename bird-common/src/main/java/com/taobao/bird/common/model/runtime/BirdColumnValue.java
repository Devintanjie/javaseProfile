package com.taobao.bird.common.model.runtime;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @desc
 * @author junyu 2015年2月16日下午1:42:14
 * @version
 **/
public class BirdColumnValue {

    private ColumnMeta column;
    private Object     value;
    private boolean    check = true; // 是否需要做数据对比

    public BirdColumnValue(){
    }

    public BirdColumnValue(ColumnMeta column, Object value){
        this(column, value, true);
    }

    public BirdColumnValue(ColumnMeta column, Object value, boolean check){
        this.value = value;
        this.column = column;
        this.check = check;
    }

    public ColumnMeta getColumn() {
        return column;
    }

    public void setColumn(ColumnMeta column) {
        this.column = column;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (check ? 1231 : 1237);
        result = prime * result + ((column == null) ? 0 : column.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        BirdColumnValue other = (BirdColumnValue) obj;
        if (check != other.check) return false;
        if (column == null) {
            if (other.column != null) return false;
        } else if (!column.equals(other.column)) return false;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }

}
