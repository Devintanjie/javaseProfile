package com.taobao.bird.common.datasource;

import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * @desc
 * @author junyu 2015��2��18������1:46:28
 * @version
 **/
public interface DataSourceFactory {

    public DataSource createBasicDataSource(String ip, String port, String dbName, String user, String passwd)
                                                                                                              throws SQLException;

    public DataSource createDetailDataSource(DataSourceConfig config) throws SQLException;
}
