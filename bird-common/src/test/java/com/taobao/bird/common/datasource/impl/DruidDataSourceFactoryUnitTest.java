package com.taobao.bird.common.datasource.impl;

import java.sql.SQLException;

import javax.sql.DataSource;

import junit.framework.Assert;

import org.junit.Test;

import com.taobao.bird.common.datasource.DataSourceConfig;

/**
 * @desc
 * @author junyu 2015年2月18日下午2:24:40
 * @version
 **/
public class DruidDataSourceFactoryUnitTest {

    @Test
    public void testCreateBasicDataSource() {
        DruidDataSourceFactory f = new DruidDataSourceFactory();
        DruidDataSourceMock m = new DruidDataSourceMock();
        f.setDds(m);
        try {
            DataSource x = f.createBasicDataSource("10.0.0.1", "3306", "dbName", "user", "passWd");
            Assert.assertEquals(((DruidDataSourceMock) x).isInited(), true);
            Assert.assertEquals(((DruidDataSourceMock) x).getUrl(), "jdbc:mysql://10.0.0.1:3306/dbName");
            Assert.assertEquals(((DruidDataSourceMock) x).getUserName(), "user");
            Assert.assertEquals(((DruidDataSourceMock) x).getPassword(), "passWd");
            Assert.assertEquals(((DruidDataSourceMock) x).getConnectionProperties(),
                "autoReconnect=true;rewriteBatchedStatements=true;socketTimeout=30000;connectTimeout=10000");
            Assert.assertEquals(((DruidDataSourceMock) x).getValidationQuery(), "SELECT 1");
            Assert.assertEquals(((DruidDataSourceMock) x).getInitialSize(), 1);
            Assert.assertEquals(((DruidDataSourceMock) x).getMaxWaitMillis(), 10000);
        } catch (SQLException e) {
            Assert.fail();
        }
    }

    @Test
    public void testCreateDetailDataSource() {
        DruidDataSourceFactory f = new DruidDataSourceFactory();
        DruidDataSourceMock m = new DruidDataSourceMock();
        f.setDds(m);
        try {
            DataSourceConfig config = new DataSourceConfig();
            config.setConnectionProperties("autoReconnect=false;rewriteBatchedStatements=true;socketTimeout=30000;connectTimeout=10000");
            config.setDbName("dbName");
            config.setIdleTimeoutMill(1000);
            config.setInitialSize(2);
            config.setIp("20.2.2.2");
            config.setMaxActive(12);
            config.setMaxWaitMill(20000);
            config.setMinIdle(20);
            config.setPasswd("passWd");
            config.setPort("3307");
            config.setTestIdleIntervalMill(10000);
            config.setTestOnBorrow(true);
            config.setTestOnReturn(false);
            config.setTestWhileIdle(true);
            config.setUser("user");
            config.setValidateQuery("select * from dual");
            DataSource x = f.createDetailDataSource(config);
            Assert.assertEquals(((DruidDataSourceMock) x).isInited(), true);
            Assert.assertEquals(((DruidDataSourceMock) x).getMinEvictableIdleTimeMillis(), 1000);
            Assert.assertEquals(((DruidDataSourceMock) x).getUrl(), "jdbc:mysql://20.2.2.2:3307/dbName");
            Assert.assertEquals(((DruidDataSourceMock) x).getUserName(), "user");
            Assert.assertEquals(((DruidDataSourceMock) x).getPassword(), "passWd");
            Assert.assertEquals(((DruidDataSourceMock) x).getConnectionProperties(),
                "autoReconnect=false;rewriteBatchedStatements=true;socketTimeout=30000;connectTimeout=10000");
            Assert.assertEquals(((DruidDataSourceMock) x).getValidationQuery(), "select * from dual");
            Assert.assertEquals(((DruidDataSourceMock) x).getInitialSize(), 2);
            Assert.assertEquals(((DruidDataSourceMock) x).getMaxWaitMillis(), 20000);
            Assert.assertEquals(((DruidDataSourceMock) x).getMinIdle(), 20);
            Assert.assertEquals(((DruidDataSourceMock) x).getTimeBetweenEvictionRunsMillis(), 10000);
            Assert.assertEquals(((DruidDataSourceMock) x).isTestOnBorrow(), true);
            Assert.assertEquals(((DruidDataSourceMock) x).isTestOnReturn(), false);
            Assert.assertEquals(((DruidDataSourceMock) x).isTestWhileIdle(), true);
        } catch (SQLException e) {
            Assert.fail();
        }
    }
}
