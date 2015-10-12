package com.taobao.bird.common.model.config.manager.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.base.Joiner;
import com.taobao.bird.common.datasource.DataSourceConfig;
import com.taobao.bird.common.datasource.DataSourceFactory;
import com.taobao.bird.common.datasource.impl.DruidDataSourceFactory;
import com.taobao.bird.common.lifecycle.BirdLifeCycle;
import com.taobao.bird.common.model.config.AgentNode;
import com.taobao.bird.common.model.config.DBType;
import com.taobao.bird.common.model.config.MySQLCheckNode;
import com.taobao.bird.common.model.config.MySQLCheckNode.CheckType;
import com.taobao.bird.common.model.config.MySQLTransferNode;
import com.taobao.bird.common.model.config.StageNode;
import com.taobao.bird.common.model.config.StageNode.StageProcess;
import com.taobao.bird.common.model.config.StatsNode;
import com.taobao.bird.common.model.config.TransferNode.TaskType;
import com.taobao.bird.common.model.config.manager.AgentNodeListener;
import com.taobao.bird.common.model.config.manager.BirdConfigManager;
import com.taobao.bird.common.model.config.manager.StageNodeListener;
import com.taobao.bird.common.model.stage.OperateStage;
import com.taobao.bird.common.thread.NamedThreadFactory;
import com.taobao.bird.common.utils.Constants;

/**
 * @desc
 * @author junyu 2015年2月18日下午12:11:30
 * @version
 **/
public class MySQLBirdConfigManager implements BirdConfigManager, Runnable, BirdLifeCycle {

    private static Logger                             log                          = LoggerFactory.getLogger(Constants.LOGGER_MYSQL_CONFIG);

    public static final String                        CONFIG_MYSQL_IP              = "config.mysql.ip";

    public static final String                        CONFIG_MYSQL_PORT            = "config.mysql.port";

    public static final String                        CONFIG_MYSQL_DB              = "config.mysql.db";

    public static final String                        CONFIG_MYSQL_USER            = "config.mysql.user";

    public static final String                        CONFIG_MYSQL_PASSWD          = "config.mysql.passwd";

    public static final String                        CONFIG_MSYQL_CONNECTION_PROP = "config.mysql.connection_prop";

    private AtomicBoolean                             inited                       = new AtomicBoolean(false);
    private AtomicBoolean                             started                      = new AtomicBoolean(false);
    private DataSource                                ds;
    private JdbcTemplate                              template;

    private DataSourceTransactionManager              txManager;
    private ScheduledThreadPoolExecutor               schedule;

    private Map<String, Map<AgentNodeListener, Long>> agentListeners;

    private Map<String, Map<StageNodeListener, Long>> stageListeners;

    private ReentrantLock                             lock                         = new ReentrantLock();
    private ReentrantLock                             initialLock                  = new ReentrantLock();

    private final Properties                          config;

    public MySQLBirdConfigManager(Properties config){
        this.config = config;
    }

    public void init() {
        try {
            initialLock.lock();

            if (inited.compareAndSet(false, true)) {
                String dbIp = config.getProperty(CONFIG_MYSQL_IP);
                String dbPort = config.getProperty(CONFIG_MYSQL_PORT);
                String dbName = config.getProperty(CONFIG_MYSQL_DB);
                String dbUser = config.getProperty(CONFIG_MYSQL_USER);
                String dbPasswd = config.getProperty(CONFIG_MYSQL_PASSWD);
                String connectionProp = config.getProperty(CONFIG_MSYQL_CONNECTION_PROP);

                if (StringUtils.isBlank(dbIp) || StringUtils.isBlank(dbPort) || StringUtils.isBlank(dbName)
                    || StringUtils.isBlank(dbUser) || StringUtils.isBlank(dbPasswd)) {
                    throw new IllegalArgumentException(CONFIG_MYSQL_IP + " or " + CONFIG_MYSQL_PORT + " or "
                                                       + CONFIG_MYSQL_DB + " or " + CONFIG_MYSQL_USER + " or "
                                                       + CONFIG_MYSQL_PASSWD + " is blank.");
                }

                DataSourceFactory factory = new DruidDataSourceFactory();
                DataSourceConfig dc = new DataSourceConfig();
                dc.setIp(dbIp);
                dc.setPort(dbPort);
                dc.setDbName(dbName);
                dc.setUser(dbUser);
                dc.setPasswd(dbPasswd);
                dc.setConnectionProperties(connectionProp);
                try {
                    ds = factory.createDetailDataSource(dc);
                    template = new JdbcTemplate(ds);
                    template.setFetchSize(Integer.MIN_VALUE);

                    txManager = new DataSourceTransactionManager(ds);

                    agentListeners = new HashMap<String, Map<AgentNodeListener, Long>>();
                    stageListeners = new HashMap<String, Map<StageNodeListener, Long>>();
                } catch (SQLException e) {
                    String errMsg = "create datasource error.ip:" + dbIp + ",port:" + dbPort + ",dbName:" + dbName
                                    + ",user:" + dbUser + ",errorCode:" + e.getErrorCode() + ",errorMsg:"
                                    + e.getMessage();
                    log.error(errMsg, e);
                    throw new RuntimeException(errMsg);
                }
            } else {
                log.warn("MySQLBirdConfigManager already inited.");
            }
        } finally {
            initialLock.unlock();
        }
    }

    private static final String queryColumns          = "gmt_create,gmt_modified,node_id,node_desc,source_ip,"
                                                        + "source_port,target_ip,target_port,translate_code,source_type,target_type,source_db_name,"
                                                        + "target_db_name,source_user_name,source_passwd,target_user_name,target_passwd,table_white_list,"
                                                        + "table_black_list,query_concurrency,insert_concurrency,task_type,is_delete";

    private static final String queryColumnsWithCheck = queryColumns
                                                        + ",check_type,sampling_time_start,"
                                                        + "sampling_time_end,increment_time_interval,increment_time_unit,time_column";

    @Override
    public MySQLTransferNode getMySQLTransferNode(final String nodeId) {
        MySQLTransferNode result = (MySQLTransferNode) template.query(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement("select "
                                                            + queryColumns
                                                            + "  from bird_mysql_transfer_task where node_id=? and is_delete=?");
                ps.setString(1, nodeId);
                ps.setInt(2, 0);
                return ps;
            }
        },
            new ResultSetExtractor() {

                @Override
                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    MySQLTransferNode node = new MySQLTransferNode();
                    int i = 0;
                    while (rs.next()) {
                        if (i > 1) {
                            throw new SQLException("one node_id(" + nodeId + ") more than one result.");
                        }

                        i++;
                        node.setNodeId(rs.getString("node_id"));
                        node.setNodeDesc(rs.getString("node_desc"));
                        node.setSourceIp(rs.getString("source_ip"));
                        node.setSourcePort(rs.getString("source_port"));
                        node.setTargetIp(rs.getString("target_ip"));
                        node.setTargetPort(rs.getString("target_port"));
                        node.setTranslateCode(rs.getString("translate_code"));
                        node.setSourceType(DBType.valueOf(rs.getString("source_type")));
                        node.setTargetType(DBType.valueOf(rs.getString("target_type")));
                        node.setSourceDbName(rs.getString("source_db_name"));
                        node.setTargetDbName(rs.getString("target_db_name"));
                        node.setSourceUserName(rs.getString("source_user_name"));
                        node.setSourcePasswd(rs.getString("source_passwd"));
                        node.setTargetUserName(rs.getString("target_user_name"));
                        node.setTargetPasswd(rs.getString("target_passwd"));
                        String wTablesStr = rs.getString("table_white_list");
                        if (StringUtils.isNotBlank(wTablesStr)) {
                            String[] ts = wTablesStr.split(";");
                            node.setTableWhiteList(Arrays.asList(ts));
                        }

                        String bTablesStr = rs.getString("table_black_list");
                        if (StringUtils.isNotBlank(bTablesStr)) {
                            String[] ts = bTablesStr.split(";");
                            node.setTableBlackList(Arrays.asList(ts));
                        }

                        node.setQueryConcurrency(rs.getInt("query_concurrency"));
                        node.setInsertConcurrency(rs.getInt("insert_concurrency"));
                        node.setTaskType(TaskType.valueOf(rs.getString("task_type")));
                        node.setGmtCreate(rs.getDate("gmt_create"));
                        node.setGmtModified(rs.getDate("gmt_modified"));
                    }
                    return node;
                }
            });
        return result;
    }

    @Override
    public MySQLCheckNode getMySQLCheckNode(final String nodeId) {
        MySQLCheckNode result = (MySQLCheckNode) template.query(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement("select "
                                                            + queryColumnsWithCheck
                                                            + "  from bird_mysql_transfer_task where node_id=? and is_delete=?");
                ps.setString(1, nodeId);
                ps.setInt(2, 0);
                return ps;
            }
        },
            new ResultSetExtractor() {

                @Override
                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    MySQLCheckNode node = new MySQLCheckNode();
                    int i = 0;
                    while (rs.next()) {
                        if (i > 1) {
                            throw new SQLException("one node_id(" + nodeId + ") more than one result.");
                        }

                        i++;
                        node.setNodeId(rs.getString("node_id"));
                        node.setNodeDesc(rs.getString("node_desc"));
                        node.setSourceIp(rs.getString("source_ip"));
                        node.setSourcePort(rs.getString("source_port"));
                        node.setTargetIp(rs.getString("target_ip"));
                        node.setTargetPort(rs.getString("target_port"));
                        node.setTranslateCode(rs.getString("translate_code"));
                        node.setSourceType(DBType.valueOf(rs.getString("source_type")));
                        node.setTargetType(DBType.valueOf(rs.getString("target_type")));
                        node.setSourceDbName(rs.getString("source_db_name"));
                        node.setTargetDbName(rs.getString("target_db_name"));
                        node.setSourceUserName(rs.getString("source_user_name"));
                        node.setSourcePasswd(rs.getString("source_passwd"));
                        node.setTargetUserName(rs.getString("target_user_name"));
                        node.setTargetPasswd(rs.getString("target_passwd"));
                        String wTablesStr = rs.getString("table_white_list");
                        if (StringUtils.isNotBlank(wTablesStr)) {
                            String[] ts = wTablesStr.split(";");
                            node.setTableWhiteList(Arrays.asList(ts));
                        }

                        String bTablesStr = rs.getString("table_black_list");
                        if (StringUtils.isNotBlank(bTablesStr)) {
                            String[] ts = bTablesStr.split(";");
                            node.setTableBlackList(Arrays.asList(ts));
                        }

                        node.setQueryConcurrency(rs.getInt("query_concurrency"));
                        node.setInsertConcurrency(rs.getInt("insert_concurrency"));
                        node.setCheckType(CheckType.valueOf(rs.getString("check_type")));
                        node.setSamplingTimeStart(rs.getDate("sampling_time_start"));
                        node.setSamplingTimeEnd(rs.getDate("sampling_time_end"));
                        node.setIncrementTimeInterval(rs.getInt("increment_time_interval"));
                        node.setIncrementTimeUnit(TimeUnit.valueOf(rs.getString("increment_time_unit")));
                        node.setTimeColumn(rs.getString("time_column"));
                        node.setTaskType(TaskType.valueOf(rs.getString("task_type")));
                        node.setGmtCreate(rs.getDate("gmt_create"));
                        node.setGmtModified(rs.getDate("gmt_modified"));
                    }
                    return node;
                }
            });
        return result;
    }

    private static final String statsQueryColumns = "gmt_create,gmt_modified,owner_node_id,curr_record,curr_table,curr_db,curr_url,"
                                                    + "curr_position,handled_count,total_count,node_id,node_desc,is_delete";

    @Override
    public StatsNode getStatsNodeByOwner(final String ownerNodeId) {
        StatsNode result = (StatsNode) template.query(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement("select " + statsQueryColumns
                                                            + "  from bird_stats where owner_node_id=? and is_delete=?");
                ps.setString(1, ownerNodeId);
                ps.setInt(2, 0);
                return ps;
            }
        },
            new ResultSetExtractor() {

                @Override
                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    StatsNode node = new StatsNode();
                    int i = 0;
                    while (rs.next()) {
                        if (i > 1) {
                            throw new SQLException("one owner_node_id(" + ownerNodeId + ") more than one result.");
                        }

                        i++;
                        node.setNodeId(rs.getString("node_id"));
                        node.setNodeDesc(rs.getString("node_desc"));
                        node.setCurrDb(rs.getString("curr_db"));
                        node.setCurrPosition(rs.getString("curr_position"));
                        node.setCurrRecord(rs.getString("curr_record"));
                        node.setCurrTable(rs.getString("curr_table"));
                        node.setCurrUrl(rs.getString("curr_url"));
                        node.setHandledCount(rs.getLong("handled_count"));
                        node.setOwnerNodeId(rs.getString("owner_node_id"));
                        node.setTotalCount(rs.getLong("total_count"));
                        node.setGmtCreate(rs.getDate("gmt_create"));
                        node.setGmtModified(rs.getDate("gmt_modified"));
                    }
                    return node;
                }
            });
        return result;
    }

    private static final String stageQueryColumns = "gmt_create,gmt_modified,owner_node_id,auto_run,stage,process,node_id,node_desc,is_delete";

    @Override
    public StageNode getStageNodeByOwner(final String ownerNodeId) {
        StageNode result = (StageNode) template.query(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement("select " + stageQueryColumns
                                                            + "  from bird_stage where owner_node_id=? and is_delete=?");
                ps.setString(1, ownerNodeId);
                ps.setInt(2, 0);
                return ps;
            }
        },
            new ResultSetExtractor() {

                @Override
                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    StageNode node = new StageNode();
                    int i = 0;
                    while (rs.next()) {
                        if (i > 1) {
                            throw new SQLException("one owner_node_id(" + ownerNodeId + ") more than one result.");
                        }

                        i++;
                        node.setNodeId(rs.getString("node_id"));
                        node.setNodeDesc(rs.getString("node_desc"));
                        int autoRunInt = rs.getInt("auto_run");
                        if (autoRunInt == 0) {
                            node.setAutoRun(false);
                        } else if (autoRunInt == 1) {
                            node.setAutoRun(true);
                        } else {
                            throw new IllegalArgumentException("not supported auto_run value:" + autoRunInt);
                        }

                        node.setOwnerNodeId(rs.getString("owner_node_id"));
                        node.setStage(OperateStage.fromStringToStage(rs.getString("stage")));
                        node.setStageProcess(StageProcess.valueOf(rs.getString("process")));
                        node.setGmtCreate(rs.getDate("gmt_create"));
                        node.setGmtModified(rs.getDate("gmt_modified"));
                    }
                    return node;
                }
            });
        return result;
    }

    @Override
    public void insertMySQLTransferNodeComplete(final MySQLTransferNode node, final StageNode stage,
                                                final StatsNode stats) {
        final TransactionTemplate txTemplate = new TransactionTemplate(txManager);
        txTemplate.execute(new TransactionCallback() {

            @Override
            public Object doInTransaction(TransactionStatus status) {
                try {
                    template.update("insert into bird_mysql_transfer_task (" + queryColumns
                                    + ") values(now(),now(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                        new PreparedStatementSetter() {

                            @Override
                            public void setValues(PreparedStatement ps) throws SQLException {
                                ps.setString(1, node.getNodeId());
                                ps.setString(2, node.getNodeDesc());
                                ps.setString(3, node.getSourceIp());
                                ps.setString(4, node.getSourcePort());
                                ps.setString(5, node.getTargetIp());
                                ps.setString(6, node.getTargetPort());
                                ps.setString(7, node.getTranslateCode());
                                ps.setString(8, node.getSourceType().name());
                                ps.setString(9, node.getTargetType().name());
                                ps.setString(10, node.getSourceDbName());
                                ps.setString(11, node.getTargetDbName());
                                ps.setString(12, node.getSourceUserName());
                                ps.setString(13, node.getSourcePasswd());
                                ps.setString(14, node.getTargetUserName());
                                ps.setString(15, node.getTargetPasswd());
                                if (node.getTableWhiteList() != null && node.getTableWhiteList().size() > 0) {
                                    String wTableStr = Joiner.on(";").join(node.getTableWhiteList().iterator());
                                    ps.setString(16, wTableStr);
                                } else {
                                    ps.setString(16, "");
                                }

                                if (node.getTableBlackList() != null && node.getTableBlackList().size() > 0) {
                                    String bTableStr = Joiner.on(";").join(node.getTableBlackList().iterator());
                                    ps.setString(17, bTableStr);
                                } else {
                                    ps.setString(17, "");
                                }
                                ps.setInt(18, node.getQueryConcurrency());
                                ps.setInt(19, node.getInsertConcurrency());
                                ps.setString(20, TaskType.MYSQL_TRANSFER.name());
                                ps.setInt(21, 0);

                            }
                        });

                    template.update("insert into bird_stage (" + stageQueryColumns
                                    + ") values (now(),now(),?,?,?,?,?,?,?)", new PreparedStatementSetter() {

                        @Override
                        public void setValues(PreparedStatement ps) throws SQLException {
                            ps.setString(1, node.getNodeId());
                            if (stage.isAutoRun()) {
                                ps.setInt(2, 1);
                            } else {
                                ps.setInt(2, 0);
                            }

                            ps.setString(3, stage.getStage().getStageValue());
                            ps.setString(4, stage.getStageProcess().name());
                            ps.setString(5, StageNode.DEFAULT_STAGE_NODE_NAME);
                            ps.setString(6, node.getNodeDesc());
                            ps.setInt(7, 0);
                        }
                    });

                    template.update("insert into bird_stats (" + statsQueryColumns
                                    + ") values(now(),now(),?,?,?,?,?,?,?,?,?,?,?)", new PreparedStatementSetter() {

                        @Override
                        public void setValues(PreparedStatement ps) throws SQLException {
                            ps.setString(1, node.getNodeId());
                            ps.setString(2, stats.getCurrRecord());
                            ps.setString(3, stats.getCurrTable());
                            ps.setString(4, stats.getCurrDb());
                            ps.setString(5, stats.getCurrUrl());
                            ps.setString(6, stats.getCurrPosition());
                            ps.setLong(7, stats.getHandledCount());
                            ps.setLong(8, stats.getTotalCount());
                            ps.setString(9, StatsNode.DEFAULT_STATS_NODE_NAME);
                            ps.setString(10, stats.getNodeDesc());
                            ps.setInt(11, 0);
                        }
                    });

                    return null;
                } catch (Throwable e) {
                    status.setRollbackOnly();
                    log.error("transaction error.", e);
                    throw e;
                }
            }
        });
    }

    @Override
    public void insertMySQLCheckNodeComplete(final MySQLCheckNode node, final StageNode stage, final StatsNode stats) {

        final TransactionTemplate txTemplate = new TransactionTemplate(txManager);
        txTemplate.execute(new TransactionCallback() {

            @Override
            public Object doInTransaction(TransactionStatus status) {
                try {
                    template.update("insert into bird_mysql_transfer_task (" + queryColumnsWithCheck
                                    + ") values(now(),now(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                        new PreparedStatementSetter() {

                            @Override
                            public void setValues(PreparedStatement ps) throws SQLException {
                                ps.setString(1, node.getNodeId());
                                ps.setString(2, node.getNodeDesc());
                                ps.setString(3, node.getSourceIp());
                                ps.setString(4, node.getSourcePort());
                                ps.setString(5, node.getTargetIp());
                                ps.setString(6, node.getTargetPort());
                                ps.setString(7, node.getTranslateCode());
                                ps.setString(8, node.getSourceType().name());
                                ps.setString(9, node.getTargetType().name());
                                ps.setString(10, node.getSourceDbName());
                                ps.setString(11, node.getTargetDbName());
                                ps.setString(12, node.getSourceUserName());
                                ps.setString(13, node.getSourcePasswd());
                                ps.setString(14, node.getTargetUserName());
                                ps.setString(15, node.getTargetPasswd());
                                if (node.getTableWhiteList() != null && node.getTableWhiteList().size() > 0) {
                                    String wTableStr = Joiner.on(";").join(node.getTableWhiteList().iterator());
                                    ps.setString(16, wTableStr);
                                } else {
                                    ps.setString(16, "");
                                }

                                if (node.getTableBlackList() != null && node.getTableBlackList().size() > 0) {
                                    String bTableStr = Joiner.on(";").join(node.getTableBlackList().iterator());
                                    ps.setString(17, bTableStr);
                                } else {
                                    ps.setString(17, "");
                                }
                                ps.setInt(18, node.getQueryConcurrency());
                                ps.setInt(19, node.getInsertConcurrency());
                                ps.setString(20, TaskType.MYSQL_CHECK.name());
                                ps.setInt(21, 0);

                                ps.setString(22, node.getCheckType().name());
                                ps.setDate(23, new Date(node.getSamplingTimeStart().getTime()));
                                ps.setDate(24, new Date(node.getSamplingTimeEnd().getTime()));
                                ps.setInt(25, node.getIncrementTimeInterval());
                                ps.setString(26, node.getIncrementTimeUnit().name());
                                ps.setString(27, node.getTimeColumn());
                            }
                        });

                    template.update("insert into bird_stage (" + stageQueryColumns
                                    + ") values (now(),now(),?,?,?,?,?,?,?)", new PreparedStatementSetter() {

                        @Override
                        public void setValues(PreparedStatement ps) throws SQLException {
                            ps.setString(1, node.getNodeId());
                            if (stage.isAutoRun()) {
                                ps.setInt(2, 1);
                            } else {
                                ps.setInt(2, 0);
                            }

                            ps.setString(3, stage.getStage().getStageValue());
                            ps.setString(4, stage.getStageProcess().name());
                            ps.setString(5, StageNode.DEFAULT_STAGE_NODE_NAME);
                            ps.setString(6, node.getNodeDesc());
                            ps.setInt(7, 0);
                        }
                    });

                    template.update("insert into bird_stats (" + statsQueryColumns
                                    + ") values(now(),now(),?,?,?,?,?,?,?,?,?,?,?)", new PreparedStatementSetter() {

                        @Override
                        public void setValues(PreparedStatement ps) throws SQLException {
                            ps.setString(1, node.getNodeId());
                            ps.setString(2, stats.getCurrRecord());
                            ps.setString(3, stats.getCurrTable());
                            ps.setString(4, stats.getCurrDb());
                            ps.setString(5, stats.getCurrUrl());
                            ps.setString(6, stats.getCurrPosition());
                            ps.setLong(7, stats.getHandledCount());
                            ps.setLong(8, stats.getTotalCount());
                            ps.setString(9, StatsNode.DEFAULT_STATS_NODE_NAME);
                            ps.setString(10, stats.getNodeDesc());
                            ps.setInt(11, 0);
                        }
                    });

                    return null;
                } catch (Throwable e) {
                    status.setRollbackOnly();
                    log.error("transaction error.", e);
                    throw e;
                }
            }
        });
    }

    @Override
    public void updateMySQLTransferNode(final MySQLTransferNode node) {
        template.update("update `bird_mysql_transfer_task` set `gmt_modified`=now(),`node_desc`=?,`source_ip`=?,"
                        + "`source_port`=?,`target_ip`=?,`target_port`=?,`translate_code`=?,`source_type`=?,`target_type`=?,"
                        + "`source_db_name`=?,`target_db_name`=?,`source_user_name`=?,`source_passwd`=?,`target_user_name`=?,"
                        + "`target_passwd`=?,`table_white_list`=?,`table_black_list`=?,`query_concurrency`=?,`insert_concurrency`=?,"
                        + "`task_type`=?  where `node_id`=? ",

            new PreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, node.getNodeDesc());
                    ps.setString(2, node.getSourceIp());
                    ps.setString(3, node.getSourcePort());
                    ps.setString(4, node.getTargetIp());
                    ps.setString(5, node.getTargetPort());
                    ps.setString(6, node.getTranslateCode());
                    ps.setString(7, node.getSourceType().name());
                    ps.setString(8, node.getTargetType().name());
                    ps.setString(9, node.getSourceDbName());
                    ps.setString(10, node.getTargetDbName());
                    ps.setString(11, node.getSourceUserName());
                    ps.setString(12, node.getSourcePasswd());
                    ps.setString(13, node.getTargetUserName());
                    ps.setString(14, node.getTargetPasswd());
                    if (node.getTableWhiteList() != null && node.getTableWhiteList().size() > 0) {
                        String wTableStr = Joiner.on(";").join(node.getTableWhiteList().iterator());
                        ps.setString(15, wTableStr);
                    } else {
                        ps.setString(15, "");
                    }

                    if (node.getTableBlackList() != null && node.getTableBlackList().size() > 0) {
                        String bTableStr = Joiner.on(";").join(node.getTableBlackList().iterator());
                        ps.setString(16, bTableStr);
                    } else {
                        ps.setString(16, "");
                    }
                    ps.setInt(17, node.getQueryConcurrency());
                    ps.setInt(18, node.getInsertConcurrency());
                    ps.setString(19, TaskType.MYSQL_TRANSFER.name());
                    ps.setString(20, node.getNodeId());
                }
            });
    }

    @Override
    public void updateMySQLCheckNode(final MySQLCheckNode node) {
        template.update("update `bird_mysql_transfer_task` set `gmt_modified`=now(),`node_desc`=?,`source_ip`=?,"
                        + "`source_port`=?,`target_ip`=?,`target_port`=?,`translate_code`=?,`source_type`=?,`target_type`=?,"
                        + "`source_db_name`=?,`target_db_name`=?,`source_user_name`=?,`source_passwd`=?,`target_user_name`=?,"
                        + "`target_passwd`=?,`table_white_list`=?,`table_black_list`=?,`query_concurrency`=?,`insert_concurrency`=?,"
                        + "`task_type`=?,`check_type`=?,`sampling_time_start`=?,`sampling_time_end`=?,`increment_time_interval`=?,"
                        + "`increment_time_unit`=?,`time_column`=?  where `node_id`=? ",

            new PreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, node.getNodeDesc());
                    ps.setString(2, node.getSourceIp());
                    ps.setString(3, node.getSourcePort());
                    ps.setString(4, node.getTargetIp());
                    ps.setString(5, node.getTargetPort());
                    ps.setString(6, node.getTranslateCode());
                    ps.setString(7, node.getSourceType().name());
                    ps.setString(8, node.getTargetType().name());
                    ps.setString(9, node.getSourceDbName());
                    ps.setString(10, node.getTargetDbName());
                    ps.setString(11, node.getSourceUserName());
                    ps.setString(12, node.getSourcePasswd());
                    ps.setString(13, node.getTargetUserName());
                    ps.setString(14, node.getTargetPasswd());
                    if (node.getTableWhiteList() != null && node.getTableWhiteList().size() > 0) {
                        String wTableStr = Joiner.on(";").join(node.getTableWhiteList().iterator());
                        ps.setString(15, wTableStr);
                    } else {
                        ps.setString(15, "");
                    }

                    if (node.getTableBlackList() != null && node.getTableBlackList().size() > 0) {
                        String bTableStr = Joiner.on(";").join(node.getTableBlackList().iterator());
                        ps.setString(16, bTableStr);
                    } else {
                        ps.setString(16, "");
                    }
                    ps.setInt(17, node.getQueryConcurrency());
                    ps.setInt(18, node.getInsertConcurrency());
                    ps.setString(19, TaskType.MYSQL_CHECK.name());

                    ps.setString(20, node.getCheckType().name());
                    ps.setDate(21, new Date(node.getSamplingTimeStart().getTime()));
                    ps.setDate(22, new Date(node.getSamplingTimeEnd().getTime()));
                    ps.setInt(23, node.getIncrementTimeInterval());
                    ps.setString(24, node.getIncrementTimeUnit().name());
                    ps.setString(25, node.getTimeColumn());
                    ps.setString(26, node.getNodeId());
                }
            });
    }

    @Override
    public void updateStageNodeByOwner(final StageNode stage) {
        template.update("update `bird_stage` set `gmt_modified`=now(),`auto_run`=?,"
                        + "`stage`=?,`process`=? where `owner_node_id`=? ", new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                if (stage.isAutoRun()) {
                    ps.setInt(1, 1);
                } else {
                    ps.setInt(1, 0);
                }

                ps.setString(2, stage.getStage().getStageValue());
                ps.setString(3, stage.getStageProcess().name());
                ps.setString(4, stage.getOwnerNodeId());
            }
        });
    }

    @Override
    public void updateStatsNodeByOwner(final StatsNode stats) {
        template.update("update `bird_stats` set `gmt_modified`=now(),`curr_record`=?,`curr_table`=?,`curr_db`=?,"
                        + "`curr_url`=?,`curr_position`=?,`handled_count`=?,`total_count`=?  where `owner_node_id`=? ",
            new PreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, stats.getCurrRecord());
                    ps.setString(2, stats.getCurrTable());
                    ps.setString(3, stats.getCurrDb());
                    ps.setString(4, stats.getCurrUrl());
                    ps.setString(5, stats.getCurrPosition());
                    ps.setLong(6, stats.getHandledCount());
                    ps.setLong(7, stats.getTotalCount());
                    ps.setString(8, stats.getOwnerNodeId());
                }
            });
    }

    @Override
    public void deleteMySQLTransferNodeComplete(final MySQLTransferNode node) {

        final TransactionTemplate txTemplate = new TransactionTemplate(txManager);
        txTemplate.execute(new TransactionCallback() {

            @Override
            public Object doInTransaction(TransactionStatus status) {
                try {
                    template.update("delete bird_mysql_transfer_task where node_id=?)", new PreparedStatementSetter() {

                        @Override
                        public void setValues(PreparedStatement ps) throws SQLException {
                            ps.setString(1, node.getNodeId());

                        }
                    });

                    template.update("delete bird_stage where owner_node_id=?", new PreparedStatementSetter() {

                        @Override
                        public void setValues(PreparedStatement ps) throws SQLException {
                            ps.setString(1, node.getNodeId());
                        }
                    });

                    template.update("delete bird_stats where owner_node_id=?", new PreparedStatementSetter() {

                        @Override
                        public void setValues(PreparedStatement ps) throws SQLException {
                            ps.setString(1, node.getNodeId());
                        }
                    });

                    return null;
                } catch (Throwable e) {
                    status.setRollbackOnly();
                    log.error("transaction error.", e);
                    throw e;
                }
            }
        });
    }

    @Override
    public void watchStageNode(StageNode node, StageNodeListener listener) {
        lock.lock();
        try {
            if (stageListeners.get(node.getOwnerNodeId()) == null) {
                Map<StageNodeListener, Long> x = new HashMap<StageNodeListener, Long>();
                x.put(listener, node.getGmtModified().getTime());
                stageListeners.put(node.getOwnerNodeId(), x);
            } else {
                throw new IllegalArgumentException("stage with owner node id:" + node.getOwnerNodeId()
                                                   + " has already add listener.");
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void watchAgentNode(AgentNode node, AgentNodeListener listener) {
        lock.lock();
        try {
            if (agentListeners.get(node.getNodeId()) == null) {
                Map<AgentNodeListener, Long> x = new HashMap<AgentNodeListener, Long>();
                x.put(listener, node.getGmtModified().getTime());
                agentListeners.put(node.getNodeId(), x);
            } else {
                throw new IllegalArgumentException("stage with node id:" + node.getNodeId()
                                                   + " has already add listener.");
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public AgentNode getAgentNode(final String nodeId) {

        AgentNode result = (AgentNode) template.query(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement("select `gmt_create`,`gmt_modified`,`node_id`,`node_desc`,"
                                                            + "`task_names`,`is_delete` from `bird_agent where node_id=? and is_delete=?");
                ps.setString(1, nodeId);
                ps.setInt(2, 0);
                return ps;
            }
        },
            new ResultSetExtractor() {

                @Override
                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    AgentNode node = new AgentNode();
                    int i = 0;
                    while (rs.next()) {
                        if (i > 1) {
                            throw new SQLException("one node_id(" + nodeId + ") more than one result.");
                        }

                        i++;
                        node.setNodeId(rs.getString("node_id"));
                        node.setNodeDesc(rs.getString("node_desc"));

                        String taskNames = rs.getString("task_names");
                        if (StringUtils.isNotBlank(taskNames)) {
                            String[] ts = taskNames.split(";");
                            Set<String> s = new HashSet<String>();
                            s.addAll(Arrays.asList(ts));
                            node.setTaskNames(s);
                        }

                        node.setGmtCreate(rs.getDate("gmt_create"));
                        node.setGmtModified(rs.getDate("gmt_modified"));
                    }
                    return node;
                }
            });
        return result;

    }

    @Override
    public void insertAgentNodeComplete(final AgentNode node, final StageNode stage) {
        final TransactionTemplate txTemplate = new TransactionTemplate(txManager);
        txTemplate.execute(new TransactionCallback() {

            @Override
            public Object doInTransaction(TransactionStatus status) {
                try {
                    template.update("insert into `bird_agent` (`gmt_create`,`gmt_modified`,`node_id`,`node_desc`,"
                                    + "`task_names`,`is_delete`) values(now(),now(),?,?,?,?)",
                        new PreparedStatementSetter() {

                            @Override
                            public void setValues(PreparedStatement ps) throws SQLException {
                                ps.setString(1, node.getNodeId());
                                ps.setString(2, node.getNodeDesc());

                                if (node.getTaskNames() != null && node.getTaskNames().size() > 0) {
                                    String wTableStr = Joiner.on(";").join(node.getTaskNames().iterator());
                                    ps.setString(3, wTableStr);
                                } else {
                                    ps.setString(3, "");
                                }

                                ps.setInt(4, 0);
                            }
                        });

                    template.update("insert into bird_stage (" + stageQueryColumns
                                    + ") values (now(),now(),?,?,?,?,?,?,?)", new PreparedStatementSetter() {

                        @Override
                        public void setValues(PreparedStatement ps) throws SQLException {
                            ps.setString(1, node.getNodeId());
                            if (stage.isAutoRun()) {
                                ps.setInt(2, 1);
                            } else {
                                ps.setInt(2, 0);
                            }

                            ps.setString(3, stage.getStage().getStageValue());
                            ps.setString(4, stage.getStageProcess().name());
                            ps.setString(5, StageNode.DEFAULT_STAGE_NODE_NAME);
                            ps.setString(6, node.getNodeDesc());
                            ps.setInt(7, 0);
                        }
                    });

                    return null;
                } catch (Throwable e) {
                    status.setRollbackOnly();
                    log.error("transaction error.", e);
                    throw e;
                }
            }
        });
    }

    @Override
    public void updateAgentNode(final AgentNode node) {
        template.update("update `bird_agent` set `gmt_modified`=now(),`task_names`=?,`node_desc`=?  where `node_id`=? ",
            new PreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    if (node.getTaskNames() != null && node.getTaskNames().size() > 0) {
                        String taskNames = Joiner.on(";").join(node.getTaskNames().iterator());
                        ps.setString(1, taskNames);
                    } else {
                        ps.setString(1, "");
                    }
                    ps.setString(2, node.getNodeDesc());
                    ps.setString(3, node.getNodeId());
                }
            });

    }

    @Override
    public void unwatchStageNode(StageNode stage) {
        lock.lock();
        try {
            agentListeners.remove(stage.getOwnerNodeId());
            log.warn("remove stage node:" + stage.getOwnerNodeId() + " listener");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void unwatchAgentNode(AgentNode node) {
        lock.lock();
        try {
            agentListeners.remove(node.getNodeId());
            log.warn("remove agent node:" + node.getNodeId() + " listener");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void deleteAgentNodeComplete(final AgentNode node) {
        final TransactionTemplate txTemplate = new TransactionTemplate(txManager);
        txTemplate.execute(new TransactionCallback() {

            @Override
            public Object doInTransaction(TransactionStatus status) {
                try {
                    template.update("delete bird_mysql_transfer_task where node_id=?)", new PreparedStatementSetter() {

                        @Override
                        public void setValues(PreparedStatement ps) throws SQLException {
                            ps.setString(1, node.getNodeId());
                        }
                    });

                    template.update("delete bird_stage where owner_node_id=?", new PreparedStatementSetter() {

                        @Override
                        public void setValues(PreparedStatement ps) throws SQLException {
                            ps.setString(1, node.getNodeId());
                        }
                    });

                    return null;
                } catch (Throwable e) {
                    status.setRollbackOnly();
                    log.error("transaction error.", e);
                    throw e;
                }
            }
        });
    }

    @Override
    public void run() {
        try {
            lock.lock();

            List<String> agentRemovedKey = new ArrayList<String>();
            for (Map.Entry<String, Map<AgentNodeListener, Long>> listener : agentListeners.entrySet()) {
                AgentNode node = this.getAgentNode(listener.getKey());
                if (node != null) {
                    for (Map.Entry<AgentNodeListener, Long> entry : listener.getValue().entrySet()) {
                        if (entry.getValue() != node.getGmtModified().getTime()) {
                            try {
                                entry.getKey().handleConfigChange(node);
                            } catch (Throwable e) {
                                log.error("agent node id:" + listener.getKey()
                                          + ",call listener.handleConfigChange error.");
                            }
                        }
                    }
                } else {
                    for (Map.Entry<AgentNodeListener, Long> entry : listener.getValue().entrySet()) {
                        try {
                            entry.getKey().handleConfigDelete(listener.getKey());
                        } catch (Throwable e) {
                            log.error("agent node id:" + listener.getKey() + ",call listener.handleConfigDelete error.");
                        }
                    }
                    agentRemovedKey.add(listener.getKey());
                }
            }

            List<String> stageRemovedKey = new ArrayList<String>();
            for (Map.Entry<String, Map<StageNodeListener, Long>> listener : stageListeners.entrySet()) {
                StageNode node = this.getStageNodeByOwner(listener.getKey());
                if (node != null) {
                    for (Map.Entry<StageNodeListener, Long> entry : listener.getValue().entrySet()) {
                        if (entry.getValue() != node.getGmtModified().getTime()) {
                            try {
                                entry.getKey().handleConfigChange(node);
                            } catch (Throwable e) {
                                log.error("stage owner node id:" + listener.getKey()
                                          + ",call listener.handleConfigChange error.");
                            }
                        }
                    }
                } else {
                    for (Map.Entry<StageNodeListener, Long> entry : listener.getValue().entrySet()) {
                        try {
                            entry.getKey().handleConfigDelete(listener.getKey());
                        } catch (Throwable e) {
                            log.error("stage owner node id:" + listener.getKey()
                                      + ",call listener.handleConfigDelete error.");
                        }
                    }
                    stageRemovedKey.add(listener.getKey());
                }

                for (String r : agentRemovedKey) {
                    log.warn("agent:" + r + " config has bean removed,remove the listeners");
                    agentListeners.remove(r);
                }

                for (String r : stageRemovedKey) {
                    log.warn("stage:" + r + " config has bean removed,remove the listeners");
                    stageListeners.remove(r);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void start() {
        try {
            initialLock.lock();
            if (inited.get() == false) {
                throw new RuntimeException("can not start,please init first.");
            }

            if (started.compareAndSet(false, true)) {
                schedule = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("mysql_config_scan_schedule",
                    false), new ThreadPoolExecutor.AbortPolicy());
                schedule.scheduleAtFixedRate(this, 60, 10, TimeUnit.SECONDS);
            }
        } finally {
            initialLock.unlock();
        }

    }

    @Override
    public void stop() {
        try {
            initialLock.lock();
            if (started.compareAndSet(true, false) && inited.compareAndSet(true, false)) {
                this.schedule.shutdown();
                if (ds instanceof DruidDataSource) {
                    ((DruidDataSource) ds).close();
                }
            }
        } finally {
            initialLock.unlock();
        }
    }

    @Override
    public void abort(String why, Throwable e) {
        throw new UnsupportedOperationException("MySQLBirdConfigManager not support this operation.");
    }

    @Override
    public boolean isStart() {
        return started.get();
    }

    @Override
    public boolean isStop() {
        return started.get();
    }
}
