package com.taobao.bird.common.model.runtime;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.common.collect.Lists;
import com.taobao.bird.common.utils.BirdToStringStyle;

/**
 * @desc
 * @author junyu 2015年2月16日下午1:19:59
 * @version
 **/
public class BirdTable {

    public String            tableType;
    public String            tableCat;
    public String            tableSchema;
    public String            tableName;
    private List<ColumnMeta> primaryKeys = Lists.newArrayList();
    private List<ColumnMeta> columns     = Lists.newArrayList();
    private List<ColumnMeta> shardKeys   = Lists.newArrayList();

    public BirdTable(String tableType, String tableCat, String tableSchema, String tableName){
        this.tableType = tableType;
        this.tableSchema = tableSchema;
        this.tableCat = tableCat;
        this.tableName = tableName;
    }

    public BirdTable(String tableType, String tableCat, String tableSchema, String tableName,
                     List<ColumnMeta> primaryKeys, List<ColumnMeta> columns){
        this.tableType = tableType;
        this.tableSchema = tableSchema;
        this.tableCat = tableCat;
        this.tableName = tableName;
        this.primaryKeys = primaryKeys;
        this.columns = columns;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(String tableCat) {
        this.tableCat = tableCat;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<ColumnMeta> getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(List<ColumnMeta> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public List<ColumnMeta> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnMeta> columns) {
        this.columns = columns;
    }

    public List<ColumnMeta> getShardKeys() {
        return shardKeys;
    }

    public void setShardKeys(List<ColumnMeta> shardKeys) {
        this.shardKeys = shardKeys;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tableCat == null) ? 0 : tableCat.hashCode());
        result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
        result = prime * result + ((tableSchema == null) ? 0 : tableSchema.hashCode());
        result = prime * result + ((tableType == null) ? 0 : tableType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        BirdTable other = (BirdTable) obj;
        if (tableCat == null) {
            if (other.tableCat != null) return false;
        } else if (!tableCat.equals(other.tableCat)) return false;
        if (tableName == null) {
            if (other.tableName != null) return false;
        } else if (!tableName.equals(other.tableName)) return false;
        if (tableSchema == null) {
            if (other.tableSchema != null) return false;
        } else if (!tableSchema.equals(other.tableSchema)) return false;
        if (tableType == null) {
            if (other.tableType != null) return false;
        } else if (!tableType.equals(other.tableType)) return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, BirdToStringStyle.DEFAULT_STYLE);
    }
}
