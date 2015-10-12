package com.taobao.bird.common.model.config;

import com.alibaba.fastjson.JSONObject;

/**
 * @desc
 * @author junyu 
 * @version
 **/
public class StatsNode extends SimpleNode {

    private String             ownerNodeId;

    private String             currRecord;

    private String             currTable;

    private String             currDb;

    private String             currUrl;

    private String             currPosition;

    private Long               handledCount;

    private Long               totalCount;

    public static final String DEFAULT_STATS_NODE_NAME = "stats";

    @Override
    public JSONObject attrToJson() {
        JSONObject j = super.attrToJson();
        j.put("currRecord", currRecord);
        j.put("currTable", currTable);
        j.put("currDb", currDb);
        j.put("currUrl", currUrl);
        j.put("currPosition", currPosition);
        j.put("handledCount", handledCount);
        j.put("totalCount", totalCount);
        return j;
    }

    @Override
    public void jsonToAttr(JSONObject json) {
        super.jsonToAttr(json);
        if (json.containsKey("currRecord")) {
            this.currRecord = json.getString("currRecord");
        }

        if (json.containsKey("currTable")) {
            this.currTable = json.getString("currTable");
        }

        if (json.containsKey("currDb")) {
            this.currDb = json.getString("currDb");
        }

        if (json.containsKey("currUrl")) {
            this.currUrl = json.getString("currUrl");
        }

        if (json.containsKey("currPosition")) {
            this.currPosition = json.getString("currPosition");
        }

        if (json.containsKey("handledCount")) {
            this.handledCount = json.getLong("handledCount");
        }

        if (json.containsKey("totalCount")) {
            this.totalCount = json.getLong("totalCount");
        }
    }

    public String getCurrRecord() {
        return currRecord;
    }

    public void setCurrRecord(String currRecord) {
        this.currRecord = currRecord;
    }

    public String getCurrTable() {
        return currTable;
    }

    public void setCurrTable(String currTable) {
        this.currTable = currTable;
    }

    public String getCurrDb() {
        return currDb;
    }

    public void setCurrDb(String currDb) {
        this.currDb = currDb;
    }

    public String getCurrUrl() {
        return currUrl;
    }

    public void setCurrUrl(String currUrl) {
        this.currUrl = currUrl;
    }

    public String getCurrPosition() {
        return currPosition;
    }

    public void setCurrPosition(String currPosition) {
        this.currPosition = currPosition;
    }

    public Long getHandledCount() {
        return handledCount;
    }

    public void setHandledCount(Long handledCount) {
        this.handledCount = handledCount;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public String getOwnerNodeId() {
        return ownerNodeId;
    }

    public void setOwnerNodeId(String ownerNodeId) {
        this.ownerNodeId = ownerNodeId;
    }
}
