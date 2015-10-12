package com.taobao.bird.common.datasource.impl;

import java.io.PrintWriter;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;

import javax.management.ObjectName;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceStatLogger;
import com.alibaba.druid.pool.ExceptionSorter;
import com.alibaba.druid.pool.ValidConnectionChecker;

/**
 * @desc
 * @author junyu 2015年3月13日下午3:45:23
 * @version
 **/
public class DruidDataSourceMock extends DruidDataSource {

    /**
     * 
     */
    private static final long serialVersionUID = 2961966021311925242L;

    @Override
    public void setUseGlobalDataSourceStat(boolean useGlobalDataSourceStat) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setResetStatEnable(boolean resetStatEnable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setEnable(boolean enable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPoolPreparedStatements(boolean value) {
        throw new UnsupportedOperationException();
    }

    private int maxActive;

    @Override
    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    private Properties connectProperties;

    @Override
    public void setConnectProperties(Properties properties) {
        this.connectProperties = properties;
    }

    private boolean inited = false;

    @Override
    public void init() throws SQLException {
        this.inited = true;
    }

    @Override
    public void setLogDifferentThread(boolean logDifferentThread) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setUseLocalSessionState(boolean useLocalSessionState) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStatLoggerClassName(String className) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStatLogger(DruidDataSourceStatLogger statLogger) {
        throw new UnsupportedOperationException();
    }

    private long timeBetweenLogStatsMillis;

    @Override
    public void setTimeBetweenLogStatsMillis(long timeBetweenLogStatsMillis) {
        this.timeBetweenLogStatsMillis = timeBetweenLogStatsMillis;
    }

    @Override
    public void setOracle(boolean isOracle) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setUseUnfairLock(boolean useUnfairLock) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setUseOracleImplicitCache(boolean useOracleImplicitCache) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTransactionQueryTimeout(int transactionQueryTimeout) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDupCloseLogEnable(boolean dupCloseLogEnable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setObjectName(ObjectName objectName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTransactionThresholdMillis(long transactionThresholdMillis) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBreakAfterAcquireFailure(boolean breakAfterAcquireFailure) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setConnectionErrorRetryAttempts(int connectionErrorRetryAttempts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxPoolPreparedStatementPerConnectionSize(int maxPoolPreparedStatementPerConnectionSize) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSharePreparedStatements(boolean sharePreparedStatements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setValidConnectionChecker(ValidConnectionChecker validConnectionChecker) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setValidConnectionCheckerClassName(String validConnectionCheckerClass) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDbType(String dbType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setConnectionInitSqls(Collection<? extends Object> connectionInitSqls) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTimeBetweenConnectErrorMillis(long timeBetweenConnectErrorMillis) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxOpenPreparedStatements(int maxOpenPreparedStatements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLogAbandoned(boolean logAbandoned) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRemoveAbandonedTimeout(int removeAbandonedTimeout) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRemoveAbandonedTimeoutMillis(long removeAbandonedTimeoutMillis) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRemoveAbandoned(boolean removeAbandoned) {
        throw new UnsupportedOperationException();
    }

    private long minEvictableIdleTimeMillis;

    @Override
    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    @Override
    public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
        throw new UnsupportedOperationException();
    }

    private long timeBetweenEvictionRunsMillis;

    @Override
    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    @Override
    public void setMaxWaitThreadCount(int maxWaithThreadCount) {
        throw new UnsupportedOperationException();
    }

    private String validationQuery;

    @Override
    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    private int validationQueryTimeout;

    @Override
    public void setValidationQueryTimeout(int validationQueryTimeout) {
        this.validationQueryTimeout = validationQueryTimeout;
    }

    @Override
    public void setAccessToUnderlyingConnectionAllowed(boolean accessToUnderlyingConnectionAllowed) {
        throw new UnsupportedOperationException();
    }

    private boolean testOnBorrow;

    @Override
    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    private boolean testOnReturn;

    @Override
    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    private boolean testWhileIdle;

    @Override
    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    @Override
    public void setDefaultAutoCommit(boolean defaultAutoCommit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDefaultReadOnly(Boolean defaultReadOnly) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDefaultTransactionIsolation(Integer defaultTransactionIsolation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDefaultCatalog(String defaultCatalog) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPasswordCallback(PasswordCallback passwordCallback) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPasswordCallbackClassName(String passwordCallbackClassName) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setUserCallback(NameCallback userCallback) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setQueryTimeout(int seconds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setName(String name) {
        throw new UnsupportedOperationException();
    }

    private long maxWaitMillis;

    @Override
    public void setMaxWait(long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    @Override
    public void setNotFullTimeoutRetryCount(int notFullTimeoutRetryCount) {
        throw new UnsupportedOperationException();
    }

    private int minIdle;

    @Override
    public void setMinIdle(int value) {
        this.minIdle = value;
    }

    @Override
    public void setMaxIdle(int maxIdle) {
        throw new UnsupportedOperationException();
    }

    private int initialSize;

    @Override
    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    private String userName;

    @Override
    public void setUsername(String username) {
        this.userName = username;
    }

    private String password;

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    private String connectionProperties;

    @Override
    public void setConnectionProperties(String connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    private String url;

    @Override
    public void setUrl(String jdbcUrl) {
        this.url = jdbcUrl;
    }

    @Override
    public void setDriverClassName(String driverClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDriverClassLoader(ClassLoader driverClassLoader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLoginTimeout(int seconds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDriver(Driver driver) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setExceptionSorter(ExceptionSorter exceptionSoter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setExceptionSorterClassName(String exceptionSorter) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setExceptionSorter(String exceptionSorter) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setProxyFilters(List<Filter> filters) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFilters(String filters) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setClearFiltersEnable(boolean clearFiltersEnable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAsyncCloseConnectionEnable(boolean asyncCloseConnectionEnable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCreateScheduler(ScheduledExecutorService createScheduler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDestroyScheduler(ScheduledExecutorService destroyScheduler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxCreateTaskCount(int maxCreateTaskCount) {
        throw new UnsupportedOperationException();
    }

    public boolean isInited() {
        return inited;
    }

    public void setInited(boolean inited) {
        this.inited = inited;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public Properties getConnectProperties() {
        return connectProperties;
    }

    public long getTimeBetweenLogStatsMillis() {
        return timeBetweenLogStatsMillis;
    }

    public long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public int getValidationQueryTimeout() {
        return validationQueryTimeout;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    public long getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public String getConnectionProperties() {
        return connectionProperties;
    }

}
