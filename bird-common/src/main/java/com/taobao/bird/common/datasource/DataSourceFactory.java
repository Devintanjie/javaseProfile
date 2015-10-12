package com.taobao.bird.common.datasource;

import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * @desc
 * @author junyu 2015年2月18日下午1:46:28
 * @version
 **/
public interface DataSourceFactory {

    public DataSource createBasicDataSource(String ip, String port, String dbName, String user, String passwd)
                                                                                                              throws SQLException;

    public DataSource createDetailDataSource(DataSourceConfig config) throws SQLException;
}
