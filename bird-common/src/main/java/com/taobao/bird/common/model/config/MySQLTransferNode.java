package com.taobao.bird.common.model.config;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.bird.common.utils.BirdCollectionUtils;

/**
 * @desc
 * @author junyu
 * @version
 **/
public class MySQLTransferNode extends TransferNode {

    private String       sourceDbName;

    private String       targetDbName;

    private String       sourceUserName;

    private String       sourcePasswd;

    private String       targetUserName;

    private String       targetPasswd;

    private List<String> tableWhiteList = new ArrayList<String>();

    private List<String> tableBlackList = new ArrayList<String>();

    private StageNode    stageNode;

    private StatsNode    statsNode;

    private int          queryConcurrency;

    private int          insertConcurrency;

    @Override
    public JSONObject attrToJson() {
        JSONObject j = super.attrToJson();
        j.put("sourceDbName", sourceDbName);
        j.put("targetDbName", targetDbName);
        j.put("sourceUserName", sourceUserName);
        j.put("targetUserName", targetUserName);
        j.put("sourcePasswd", sourcePasswd);
        j.put("targetPasswd", targetPasswd);
        JSONArray wTables = new JSONArray();
        wTables.addAll(tableWhiteList);
        JSONArray bTables = new JSONArray();
        bTables.addAll(tableBlackList);
        j.put("tableWhiteList", wTables);
        j.put("tableBlackList", bTables);
        j.put("queryConcurrency", queryConcurrency);
        j.put("insertConcurrency", insertConcurrency);
        return j;
    }

    @Override
    public void jsonToAttr(JSONObject json) {
        super.jsonToAttr(json);
        if (json.containsKey("sourceDbName")) {
            this.sourceDbName = json.getString("sourceDbName");
        } else {
            throw new IllegalArgumentException("sourceDbName is null.");
        }

        if (json.containsKey("targetDbName")) {
            this.targetDbName = json.getString("targetDbName");
        }

        if (json.containsKey("sourceUserName")) {
            this.sourceUserName = json.getString("sourceUserName");
        } else {
            throw new IllegalArgumentException("sourceUserName is null.");
        }

        if (json.containsKey("targetUserName")) {
            this.targetUserName = json.getString("targetUserName");
        }

        if (json.containsKey("sourcePasswd")) {
            this.sourcePasswd = json.getString("sourcePasswd");
        } else {
            throw new IllegalArgumentException("sourcePasswd is null.");
        }

        if (json.containsKey("targetPasswd")) {
            this.targetPasswd = json.getString("targetPasswd");
        }

        if (json.containsKey("tableWhiteList")) {
            JSONArray a = json.getJSONArray("tableWhiteList");
            tableWhiteList = BirdCollectionUtils.changeJsonArrayToList(a);
        }

        if (json.containsKey("tableBlackList")) {
            JSONArray b = json.getJSONArray("tableBlackList");
            tableBlackList = BirdCollectionUtils.changeJsonArrayToList(b);
        }

        if (json.containsKey("queryConcurrency")) {
            this.queryConcurrency = json.getIntValue("queryConcurrency");
        }

        if (json.containsKey("insertConcurrency")) {
            this.insertConcurrency = json.getIntValue("insertConcurrency");
        }
    }

    public String getSourceDbName() {
        return sourceDbName;
    }

    public void setSourceDbName(String sourceDbName) {
        this.sourceDbName = sourceDbName;
    }

    public String getTargetDbName() {
        return targetDbName;
    }

    public void setTargetDbName(String targetDbName) {
        this.targetDbName = targetDbName;
    }

    public String getSourceUserName() {
        return sourceUserName;
    }

    public void setSourceUserName(String sourceUserName) {
        this.sourceUserName = sourceUserName;
    }

    public String getSourcePasswd() {
        return sourcePasswd;
    }

    public void setSourcePasswd(String sourcePasswd) {
        this.sourcePasswd = sourcePasswd;
    }

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
    }

    public String getTargetPasswd() {
        return targetPasswd;
    }

    public void setTargetPasswd(String targetPasswd) {
        this.targetPasswd = targetPasswd;
    }

    public List<String> getTableWhiteList() {
        return tableWhiteList;
    }

    public void setTableWhiteList(List<String> tableWhiteList) {
        this.tableWhiteList = tableWhiteList;
    }

    public List<String> getTableBlackList() {
        return tableBlackList;
    }

    public void setTableBlackList(List<String> tableBlackList) {
        this.tableBlackList = tableBlackList;
    }

    public StageNode getStageNode() {
        return stageNode;
    }

    public void setStageNode(StageNode stageNode) {
        this.stageNode = stageNode;
    }

    public StatsNode getStatsNode() {
        return statsNode;
    }

    public void setStatsNode(StatsNode statsNode) {
        this.statsNode = statsNode;
    }

    public int getQueryConcurrency() {
        return queryConcurrency;
    }

    public void setQueryConcurrency(int queryConcurrency) {
        this.queryConcurrency = queryConcurrency;
    }

    public int getInsertConcurrency() {
        return insertConcurrency;
    }

    public void setInsertConcurrency(int insertConcurrency) {
        this.insertConcurrency = insertConcurrency;
    }
}
