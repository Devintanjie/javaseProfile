package com.taobao.bird.common.model.config.manager;

import com.taobao.bird.common.model.config.AgentNode;
import com.taobao.bird.common.model.config.MySQLCheckNode;
import com.taobao.bird.common.model.config.MySQLTransferNode;
import com.taobao.bird.common.model.config.StageNode;
import com.taobao.bird.common.model.config.StatsNode;

/**
 * @desc
 * @author junyu 
 * @version
 **/
public interface BirdConfigManager {

    public MySQLTransferNode getMySQLTransferNode(String nodeId);

    public MySQLCheckNode getMySQLCheckNode(String nodeId);

    public AgentNode getAgentNode(String nodeId);

    public StatsNode getStatsNodeByOwner(String ownerNodeId);

    public StageNode getStageNodeByOwner(String ownerNodeId);

    public void insertMySQLTransferNodeComplete(MySQLTransferNode node, StageNode stage, StatsNode stats);

    public void insertMySQLCheckNodeComplete(MySQLCheckNode node, StageNode stage, StatsNode stats);

    public void insertAgentNodeComplete(AgentNode node, StageNode stage);

    public void updateMySQLTransferNode(MySQLTransferNode node);

    public void updateMySQLCheckNode(MySQLCheckNode node);

    public void updateAgentNode(AgentNode node);

    public void updateStageNodeByOwner(StageNode stage);

    public void updateStatsNodeByOwner(StatsNode stats);

    public void deleteMySQLTransferNodeComplete(MySQLTransferNode node);

    public void deleteAgentNodeComplete(AgentNode node);

    public void watchStageNode(StageNode stage, StageNodeListener listener);

    public void watchAgentNode(AgentNode node, AgentNodeListener listener);

    public void unwatchStageNode(StageNode stage);

    public void unwatchAgentNode(AgentNode node);
}
