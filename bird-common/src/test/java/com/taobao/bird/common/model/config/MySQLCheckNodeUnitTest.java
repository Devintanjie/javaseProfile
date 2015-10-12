package com.taobao.bird.common.model.config;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.taobao.bird.common.model.config.MySQLCheckNode.CheckType;
import com.taobao.bird.common.model.config.StageNode.StageProcess;
import com.taobao.bird.common.model.config.TransferNode.TaskType;
import com.taobao.bird.common.model.stage.OperateStage;

/**
 * @desc
 * @author junyu 2015年2月17日下午12:03:44
 * @version
 **/
public class MySQLCheckNodeUnitTest {

    @Test
    public void testAttrToJson() {
        Calendar d = Calendar.getInstance();
        d.set(2001, 1, 1, 1, 1, 1);
        MySQLCheckNode node = new MySQLCheckNode();
        node.setCheckType(CheckType.FULL);
        node.setGmtCreate(d.getTime());
        node.setGmtModified(d.getTime());
        node.setIncrementTimeInterval(2);
        node.setIncrementTimeUnit(TimeUnit.MINUTES);
        // node.setInsertConcurrency(8);
        node.setNodeDesc("check node");
        node.setNodeId("CHECK_APP_X");
        node.setQueryConcurrency(4);
        Calendar samplingTimeEnd = Calendar.getInstance();
        samplingTimeEnd.set(1999, 1, 1, 1, 1, 1);
        node.setSamplingTimeEnd(samplingTimeEnd.getTime());
        Calendar samplingTimeStart = Calendar.getInstance();
        samplingTimeStart.set(1998, 1, 1, 1, 1, 1);
        node.setSamplingTimeStart(samplingTimeStart.getTime());
        node.setSourceDbName("s_db");
        node.setSourceIp("10.1.1.1");
        node.setSourcePasswd("s_passwd");
        node.setSourcePort("3307");
        node.setSourceType(DBType.HBASE);
        node.setSourceUserName("s_user");
        StageNode s = new StageNode();
        s.setStageProcess(StageProcess.CHECK);
        s.setStage(OperateStage.fromStringToStage("Check"));
        s.setAutoRun(false);
        s.setNodeId("stageNodeId");
        s.setNodeDesc("stageNodeDesc");
        s.setOwnerNodeId("CHECK_APP_X");
        node.setStageNode(s);
        StatsNode statsNode = new StatsNode();
        statsNode.setCurrDb("currDb");
        statsNode.setCurrRecord("currRecord");
        statsNode.setCurrUrl("currUrl");
        statsNode.setCurrTable("currTable");
        statsNode.setHandledCount(2000l);
        statsNode.setNodeId("statsNodeId");
        statsNode.setTotalCount(3000l);
        statsNode.setOwnerNodeId("CHECK_APP_X");
        node.setStatsNode(statsNode);
        List<String> tabs = new ArrayList<String>();
        tabs.add("A");
        tabs.add("B");
        tabs.add("C");
        tabs.add("D");
        node.setTableWhiteList(tabs);
        node.setTargetDbName("t_db");
        node.setTargetIp("30.1.1.1");
        node.setTargetPasswd("t_passwd");
        node.setTargetPort("3308");
        node.setTargetType(DBType.INFOBRIGHT);
        node.setTargetUserName("t_user");
        node.setTaskType(TaskType.MYSQL_CHECK);
        node.setTimeColumn("gmt_create");

        JSONObject o = node.attrToJson();
        Assert.assertEquals("30.1.1.1", o.getString("targetIp"));
        Assert.assertEquals(0, o.getIntValue("insertConcurrency"));
        Assert.assertEquals(4, o.getIntValue("queryConcurrency"));
        Assert.assertEquals("gmt_create", o.getString("timeColumn"));
        Assert.assertEquals("FULL", o.getString("checkType"));
        Assert.assertEquals("MINUTES", o.getString("incrementTimeUnit"));
        Assert.assertEquals(2, o.getIntValue("incrementTimeInterval"));
        Assert.assertEquals("HBASE", o.getString("sourceType"));
        Assert.assertEquals("1999-02-01 01:01:01", o.getString("samplingTimeEnd"));
        Assert.assertEquals("1998-02-01 01:01:01", o.getString("samplingTimeStart"));
        String j = "{\"gmtModified\":\"2001-02-01 01:01:01\",\"sourceDbName\":\"s_db\",\"tableWhiteList\":[\"A\",\"B\",\"C\",\"D\"],\"targetUserName\":\"t_user\",\"taskType\":\"MYSQL_CHECK\",\"sourcePort\":\"3307\",\"timeColumn\":\"gmt_create\",\"tableBlackList\":[],\"nodeId\":\"CHECK_APP_X\",\"nodeDesc\":\"check node\",\"sourceUserName\":\"s_user\",\"incrementTimeInterval\":2,\"checkType\":\"FULL\",\"targetType\":\"INFOBRIGHT\",\"targetPasswd\":\"t_passwd\",\"sourceType\":\"HBASE\",\"incrementTimeUnit\":\"MINUTES\",\"sourcePasswd\":\"s_passwd\",\"samplingTimeStart\":\"1998-02-01 01:01:01\",\"gmtCreate\":\"2001-02-01 01:01:01\",\"insertConcurrency\":0,\"samplingTimeEnd\":\"1999-02-01 01:01:01\",\"queryConcurrency\":4,\"targetPort\":\"3308\",\"sourceIp\":\"10.1.1.1\",\"targetDbName\":\"t_db\",\"targetIp\":\"30.1.1.1\"}";
        Assert.assertEquals(j, node.toJsonStr());
    }

    @Test
    public void testJsonToAttr() {
        String j = "{\"gmtModified\":\"2001-02-01 01:01:01\",\"sourceDbName\":\"s_db\",\"tableWhiteList\":[\"A\",\"B\",\"C\",\"D\"],\"targetUserName\":\"t_user\",\"taskType\":\"MYSQL_CHECK\",\"sourcePort\":\"3307\",\"timeColumn\":\"gmt_create\",\"tableBlackList\":[],\"nodeId\":\"CHECK_APP_X\",\"nodeDesc\":\"check node\",\"sourceUserName\":\"s_user\",\"incrementTimeInterval\":2,\"checkType\":\"FULL\",\"targetType\":\"INFOBRIGHT\",\"targetPasswd\":\"t_passwd\",\"sourceType\":\"HBASE\",\"incrementTimeUnit\":\"MINUTES\",\"sourcePasswd\":\"s_passwd\",\"samplingTimeStart\":\"1998-02-01 01:01:01\",\"gmtCreate\":\"2001-02-01 01:01:01\",\"insertConcurrency\":0,\"samplingTimeEnd\":\"1999-02-01 01:01:01\",\"queryConcurrency\":4,\"targetPort\":\"3308\",\"sourceIp\":\"10.1.1.1\",\"targetDbName\":\"t_db\",\"targetIp\":\"30.1.1.1\"}";
        JSONObject o = JSONObject.parseObject(j);
        MySQLCheckNode node = new MySQLCheckNode();
        node.jsonToAttr(o);
        Assert.assertEquals("30.1.1.1", node.getTargetIp());
        Assert.assertEquals(0, node.getInsertConcurrency());
        Assert.assertEquals(4, node.getQueryConcurrency());
        Assert.assertEquals("gmt_create", node.getTimeColumn());
        Assert.assertEquals(CheckType.FULL, node.getCheckType());
        Assert.assertEquals(TimeUnit.MINUTES, node.getIncrementTimeUnit());
        Assert.assertEquals(2, node.getIncrementTimeInterval());
        Calendar d1 = Calendar.getInstance();
        d1.set(1999, 1, 1, 1, 1, 1);
        d1.set(Calendar.MILLISECOND, 0);
        Assert.assertEquals(d1.getTimeInMillis(), node.getSamplingTimeEnd().getTime());
        Calendar d2 = Calendar.getInstance();
        d2.set(1998, 1, 1, 1, 1, 1);
        d2.set(Calendar.MILLISECOND, 0);
        Assert.assertEquals(d2.getTimeInMillis(), node.getSamplingTimeStart().getTime());
    }
}
