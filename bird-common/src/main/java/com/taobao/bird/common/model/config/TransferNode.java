package com.taobao.bird.common.model.config;

import com.alibaba.fastjson.JSONObject;

/**
 * @desc
 * @author junyu 
 * @version
 **/
public class TransferNode extends SimpleNode {

    private String   sourceIp;
    private String   sourcePort;
    private String   targetIp;
    private String   targetPort;
    private String   translateCode;
    private DBType   sourceType = DBType.MYSQL;
    private DBType   targetType = DBType.MYSQL;
    private TaskType taskType;

    public JSONObject attrToJson() {
        JSONObject j = super.attrToJson();
        j.put("sourceIp", sourceIp);
        j.put("sourcePort", sourcePort);
        j.put("targetIp", targetIp);
        j.put("targetPort", targetPort);
        j.put("translateCode", translateCode);
        j.put("sourceType", sourceType);
        j.put("targetType", targetType);
        j.put("taskType", taskType.name());
        return j;
    }

    public void jsonToAttr(JSONObject json) {
        super.jsonToAttr(json);

        if (json.containsKey("sourceIp")) {
            sourceIp = json.getString("sourceIp");
        } else {
            throw new IllegalArgumentException("sourceIp is null.");
        }

        if (json.containsKey("sourcePort")) {
            sourcePort = json.getString("sourcePort");
        } else {
            throw new IllegalArgumentException("sourcePort is null.");
        }

        if (json.containsKey("targetIp")) {
            targetIp = json.getString("targetIp");
        }

        if (json.containsKey("targetPort")) {
            targetPort = json.getString("targetPort");
        }

        if (json.containsKey("translateCode")) {
            translateCode = json.getString("translateCode");
        }

        if (json.containsKey("sourceType")) {
            sourceType = DBType.valueOf(json.getString("sourceType"));
        }

        if (json.containsKey("targetType")) {
            targetType = DBType.valueOf(json.getString("targetType"));
        }

        if (json.containsKey("taskType")) {
            taskType = TaskType.valueOf(json.getString("taskType"));
        } else {
            throw new IllegalArgumentException("taskType is null.");
        }
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(String sourcePort) {
        this.sourcePort = sourcePort;
    }

    public String getTargetIp() {
        return targetIp;
    }

    public void setTargetIp(String targetIp) {
        this.targetIp = targetIp;
    }

    public String getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(String targetPort) {
        this.targetPort = targetPort;
    }

    public String getTranslateCode() {
        return translateCode;
    }

    public void setTranslateCode(String translateCode) {
        this.translateCode = translateCode;
    }

    public DBType getSourceType() {
        return sourceType;
    }

    public void setSourceType(DBType sourceType) {
        this.sourceType = sourceType;
    }

    public DBType getTargetType() {
        return targetType;
    }

    public void setTargetType(DBType targetType) {
        this.targetType = targetType;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public enum TaskType {
        MYSQL_TRANSFER, MYSQL_CHECK
    }
}
