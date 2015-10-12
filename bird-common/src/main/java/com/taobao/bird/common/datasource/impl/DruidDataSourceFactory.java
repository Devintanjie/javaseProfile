package com.taobao.bird.common.datasource.impl;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.alibaba.druid.pool.DruidDataSource;
import com.taobao.bird.common.datasource.DataSourceConfig;
import com.taobao.bird.common.datasource.DataSourceFactory;

/**
 * @desc
 * @author junyu
 * @version
 **/
public class DruidDataSourceFactory implements DataSourceFactory {

    public static final String DEFAULT_CONNECTION_PROPTY = "autoReconnect=true;rewriteBatchedStatements=true;socketTimeout=30000;connectTimeout=10000";

    @Override
    public DataSource createBasicDataSource(String ip, String port, String dbName, String user, String passwd)
                                                                                                              throws SQLException {
        DataSourceConfig config = new DataSourceConfig();
        config.setIp(ip);
        config.setPort(port);
        config.setDbName(dbName);
        config.setUser(user);
        config.setPasswd(passwd);
        return this.createDetailDataSource(config);
    }

    private DruidDataSource dds;

    // for test;
    protected void setDds(DruidDataSource dds) {
        this.dds = dds;
    }

    private DruidDataSource getDruidDataSource() {
        if (dds == null) {
            return new DruidDataSource();
        } else {
            return dds;
        }
    }

    @Override
    public DataSource createDetailDataSource(DataSourceConfig config) throws SQLException {
        DruidDataSource ds = getDruidDataSource();
        ds.setUrl("jdbc:mysql://" + config.getIp() + ":" + config.getPort() + "/" + config.getDbName());
        ds.setUsername(config.getUser());
        ds.setPassword(config.getPasswd());
        if (config.getMaxActive() > 0) {
            ds.setMaxActive(config.getMaxActive());
        } else {
            ds.setMaxActive(2);
        }

        if (config.getMinIdle() > 0) {
            ds.setMinIdle(config.getMinIdle());
        } else {
            ds.setMinIdle(1);
        }

        if (config.getInitialSize() > 0) {
            ds.setInitialSize(config.getInitialSize());
        } else {
            ds.setInitialSize(1);
        }

        if (config.getMaxWaitMill() > 0) {
            ds.setMaxWait(config.getMaxWaitMill());
        } else {
            ds.setMaxWait(10000);
        }

        if (config.getTestIdleIntervalMill() > 0) {
            ds.setTimeBetweenEvictionRunsMillis(config.getTestIdleIntervalMill());
        } else {
            ds.setTimeBetweenEvictionRunsMillis(60000);
        }

        if (config.getIdleTimeoutMill() > 0) {
            ds.setMinEvictableIdleTimeMillis(config.getIdleTimeoutMill());
        } else {
            ds.setMinEvictableIdleTimeMillis(50000);
        }

        ds.setTestWhileIdle(config.isTestWhileIdle());
        ds.setTestOnBorrow(config.isTestOnBorrow());
        ds.setTestOnReturn(config.isTestOnReturn());
        if (StringUtils.isNotBlank(config.getValidateQuery())) {
            ds.setValidationQuery(config.getValidateQuery());
        } else {
            ds.setValidationQuery("SELECT 1");
        }

        if (StringUtils.isNotBlank(config.getConnectionProperties())) {
            ds.setConnectionProperties(config.getConnectionProperties());
        } else {
            ds.setConnectionProperties(DEFAULT_CONNECTION_PROPTY);
        }

        ds.init();

        return ds;
    }
}
