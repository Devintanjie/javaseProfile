package com.taobao.bird.common.model.config.manager.impl;

import com.taobao.bird.common.model.config.AgentNode;
import com.taobao.bird.common.model.config.MySQLCheckNode;
import com.taobao.bird.common.model.config.MySQLTransferNode;
import com.taobao.bird.common.model.config.StageNode;
import com.taobao.bird.common.model.config.StatsNode;
import com.taobao.bird.common.model.config.manager.AgentNodeListener;
import com.taobao.bird.common.model.config.manager.BirdConfigManager;
import com.taobao.bird.common.model.config.manager.StageNodeListener;

/**
 * @desc
 * @author junyu 2015年2月18日下午12:07:32
 * @version
 **/
public class ZkBirdConfigManager implements BirdConfigManager {

    @Override
    public MySQLTransferNode getMySQLTransferNode(String nodeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MySQLCheckNode getMySQLCheckNode(String nodeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StatsNode getStatsNodeByOwner(String ownerNodeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StageNode getStageNodeByOwner(String ownerNodeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void insertMySQLTransferNodeComplete(MySQLTransferNode node, StageNode stage, StatsNode stats) {
        // TODO Auto-generated method stub

    }

    @Override
    public void insertMySQLCheckNodeComplete(MySQLCheckNode node, StageNode stage, StatsNode stats) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateMySQLTransferNode(MySQLTransferNode node) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateMySQLCheckNode(MySQLCheckNode node) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateStageNodeByOwner(StageNode stage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateStatsNodeByOwner(StatsNode stats) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteMySQLTransferNodeComplete(MySQLTransferNode node) {
        // TODO Auto-generated method stub

    }

    @Override
    public void watchStageNode(StageNode stage, StageNodeListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void watchAgentNode(AgentNode node, AgentNodeListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
    public AgentNode getAgentNode(String nodeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void insertAgentNodeComplete(AgentNode node, StageNode stage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateAgentNode(AgentNode node) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unwatchStageNode(StageNode stage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unwatchAgentNode(AgentNode node) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAgentNodeComplete(AgentNode node) {
        // TODO Auto-generated method stub

    }

}
