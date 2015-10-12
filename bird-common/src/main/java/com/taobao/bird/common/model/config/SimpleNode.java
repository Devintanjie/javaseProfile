package com.taobao.bird.common.model.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.taobao.bird.common.utils.Constants;

/**
 * @desc
 * @author junyu 2015年2月16日下午1:53:18
 * @version
 **/
public class SimpleNode {

    private String nodeId;
    private String nodeDesc;
    private Date   gmtCreate;
    private Date   gmtModified;

    public JSONObject attrToJson() {
        JSONObject j = new JSONObject();
        j.put("nodeId", nodeId);
        j.put("nodeDesc", nodeDesc);
        j.put("gmtCreate", generateDateString(gmtCreate));
        j.put("gmtModified", generateDateString(gmtModified));
        return j;
    }

    public void jsonToAttr(JSONObject json) {
        if (json.containsKey("nodeId")) {
            nodeId = json.getString("nodeId");
        } else {
            throw new IllegalArgumentException("nodeId is null.");
        }

        if (json.containsKey("nodeDesc")) {
            nodeDesc = json.getString("nodeDesc");
        }

        if (json.containsKey("gmtCreate")) {
            gmtCreate = parseDateString(json.getString("gmtCreate"));
        }

        if (json.containsKey("gmtModified")) {
            gmtModified = parseDateString(json.getString("gmtModified"));
        }
    }

    public static String generateDateString(Date date) {
        SimpleDateFormat f = new SimpleDateFormat(Constants.BIRD_DATE_FORMAT);
        return f.format(date);
    }

    public static Date parseDateString(String dateStr) {
        SimpleDateFormat f = new SimpleDateFormat(Constants.BIRD_DATE_FORMAT);
        try {
            return f.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("parse date error,default date format:" + Constants.BIRD_DATE_FORMAT
                                       + ",date str:" + dateStr);
        }
    }

    public String toJsonStr() {
        JSONObject json = attrToJson();
        return json.toJSONString();
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeDesc() {
        return nodeDesc;
    }

    public void setNodeDesc(String nodeDesc) {
        this.nodeDesc = nodeDesc;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

}
