package com.taobao.bird.extractor.full.mysql;

/**
 * @desc
 * @author junyu 2015年10月13日下午1:01:21
 * @version
 **/
public class RecordPosition {

    private String ip;

    private String port;

    private String dbName;

    private String primaryKey;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

}
