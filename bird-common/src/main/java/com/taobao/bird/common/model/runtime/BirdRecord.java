package com.taobao.bird.common.model.runtime;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.common.collect.Lists;

/**
 * @desc
 * @author junyu 
 * @version
 **/
public class BirdRecord {

    private String                schemaName;
    private String                tableName;
    private List<BirdColumnValue> primaryKeys = Lists.newArrayList();
    private List<BirdColumnValue> columns     = Lists.newArrayList();

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<BirdColumnValue> getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(List<BirdColumnValue> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public List<BirdColumnValue> getColumns() {
        return columns;
    }

    public void setColumns(List<BirdColumnValue> columns) {
        this.columns = columns;
    }

    public BirdColumnValue getColumnByName(String columnName, boolean returnNullNotExist) {
        for (BirdColumnValue column : columns) {
            if (column.getColumn().getName().equalsIgnoreCase(columnName)) {
                return column;
            }
        }

        for (BirdColumnValue pk : primaryKeys) {
            if (pk.getColumn().getName().equalsIgnoreCase(columnName)) {
                return pk;
            }
        }

        if (returnNullNotExist) {
            return null;
        } else {
            throw new RuntimeException("not found column[" + columnName + "]");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((columns == null) ? 0 : columns.hashCode());
        result = prime * result + ((primaryKeys == null) ? 0 : primaryKeys.hashCode());
        result = prime * result + ((schemaName == null) ? 0 : schemaName.hashCode());
        result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        BirdRecord other = (BirdRecord) obj;
        if (columns == null) {
            if (other.columns != null) return false;
        } else if (!columns.equals(other.columns)) return false;
        if (primaryKeys == null) {
            if (other.primaryKeys != null) return false;
        } else if (!primaryKeys.equals(other.primaryKeys)) return false;
        if (schemaName == null) {
            if (other.schemaName != null) return false;
        } else if (!schemaName.equals(other.schemaName)) return false;
        if (tableName == null) {
            if (other.tableName != null) return false;
        } else if (!tableName.equals(other.tableName)) return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }

}
