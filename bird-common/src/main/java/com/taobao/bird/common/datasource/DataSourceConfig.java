package com.taobao.bird.common.datasource;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.taobao.bird.common.utils.BirdToStringStyle;

/**
 * @desc
 * @author junyu 2015年2月18日下午1:48:52
 * @version
 **/
public class DataSourceConfig {

    private String  ip;
    private String  port;
    private String  dbName;
    private String  user;
    private String  passwd;
    private int     maxActive;
    private int     minIdle;
    private int     initialSize;
    private int     maxWaitMill;
    private int     testIdleIntervalMill;
    private int     idleTimeoutMill;
    private String  validateQuery;
    private boolean testWhileIdle;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private String  connectionProperties;

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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMaxWaitMill() {
        return maxWaitMill;
    }

    public void setMaxWaitMill(int maxWaitMill) {
        this.maxWaitMill = maxWaitMill;
    }

    public int getTestIdleIntervalMill() {
        return testIdleIntervalMill;
    }

    public void setTestIdleIntervalMill(int testIdleIntervalMill) {
        this.testIdleIntervalMill = testIdleIntervalMill;
    }

    public int getIdleTimeoutMill() {
        return idleTimeoutMill;
    }

    public void setIdleTimeoutMill(int idleTimeoutMill) {
        this.idleTimeoutMill = idleTimeoutMill;
    }

    public String getValidateQuery() {
        return validateQuery;
    }

    public void setValidateQuery(String validateQuery) {
        this.validateQuery = validateQuery;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public String getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(String connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((connectionProperties == null) ? 0 : connectionProperties.hashCode());
        result = prime * result + ((dbName == null) ? 0 : dbName.hashCode());
        result = prime * result + idleTimeoutMill;
        result = prime * result + initialSize;
        result = prime * result + ((ip == null) ? 0 : ip.hashCode());
        result = prime * result + maxActive;
        result = prime * result + maxWaitMill;
        result = prime * result + minIdle;
        result = prime * result + ((passwd == null) ? 0 : passwd.hashCode());
        result = prime * result + ((port == null) ? 0 : port.hashCode());
        result = prime * result + testIdleIntervalMill;
        result = prime * result + (testOnBorrow ? 1231 : 1237);
        result = prime * result + (testOnReturn ? 1231 : 1237);
        result = prime * result + (testWhileIdle ? 1231 : 1237);
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((validateQuery == null) ? 0 : validateQuery.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        DataSourceConfig other = (DataSourceConfig) obj;
        if (connectionProperties == null) {
            if (other.connectionProperties != null) return false;
        } else if (!connectionProperties.equals(other.connectionProperties)) return false;
        if (dbName == null) {
            if (other.dbName != null) return false;
        } else if (!dbName.equals(other.dbName)) return false;
        if (idleTimeoutMill != other.idleTimeoutMill) return false;
        if (initialSize != other.initialSize) return false;
        if (ip == null) {
            if (other.ip != null) return false;
        } else if (!ip.equals(other.ip)) return false;
        if (maxActive != other.maxActive) return false;
        if (maxWaitMill != other.maxWaitMill) return false;
        if (minIdle != other.minIdle) return false;
        if (passwd == null) {
            if (other.passwd != null) return false;
        } else if (!passwd.equals(other.passwd)) return false;
        if (port == null) {
            if (other.port != null) return false;
        } else if (!port.equals(other.port)) return false;
        if (testIdleIntervalMill != other.testIdleIntervalMill) return false;
        if (testOnBorrow != other.testOnBorrow) return false;
        if (testOnReturn != other.testOnReturn) return false;
        if (testWhileIdle != other.testWhileIdle) return false;
        if (user == null) {
            if (other.user != null) return false;
        } else if (!user.equals(other.user)) return false;
        if (validateQuery == null) {
            if (other.validateQuery != null) return false;
        } else if (!validateQuery.equals(other.validateQuery)) return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, BirdToStringStyle.DEFAULT_STYLE);
    }
}
