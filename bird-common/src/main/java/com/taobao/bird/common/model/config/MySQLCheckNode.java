package com.taobao.bird.common.model.config;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;

/**
 * @desc
 * @author junyu
 * @version
 **/
public class MySQLCheckNode extends MySQLTransferNode {

    public enum CheckType {
        FULL, SAMPLING, INCREMENT
    }

    private CheckType checkType = CheckType.FULL;

    private Date      samplingTimeStart;
    private Date      samplingTimeEnd;

    private int       incrementTimeInterval;
    private TimeUnit  incrementTimeUnit;

    private String    timeColumn;

    @Override
    public JSONObject attrToJson() {
        JSONObject j = super.attrToJson();
        j.put("checkType", checkType.name());
        j.put("samplingTimeStart", generateDateString(samplingTimeStart));
        j.put("samplingTimeEnd", generateDateString(samplingTimeEnd));
        j.put("incrementTimeInterval", incrementTimeInterval);
        j.put("incrementTimeUnit", incrementTimeUnit.name());
        j.put("timeColumn", timeColumn);
        return j;
    }

    @Override
    public void jsonToAttr(JSONObject json) {
        super.jsonToAttr(json);
        if (json.containsKey("checkType")) {
            this.checkType = CheckType.valueOf(json.getString("checkType"));
        } else {
            throw new IllegalArgumentException("checkType is null.");
        }

        if (json.containsKey("samplingTimeStart")) {
            this.samplingTimeStart = parseDateString(json.getString("samplingTimeStart"));
        }

        if (json.containsKey("samplingTimeEnd")) {
            this.samplingTimeEnd = parseDateString(json.getString("samplingTimeEnd"));
        }

        if (json.containsKey("incrementTimeInterval")) {
            this.incrementTimeInterval = json.getIntValue("incrementTimeInterval");
        }

        if (json.containsKey("incrementTimeUnit")) {
            this.incrementTimeUnit = TimeUnit.valueOf(json.getString("incrementTimeUnit"));
        }

        if (json.containsKey("timeColumn")) {
            this.timeColumn = json.getString("timeColumn");
        }
    }

    public CheckType getCheckType() {
        return checkType;
    }

    public void setCheckType(CheckType checkType) {
        this.checkType = checkType;
    }

    public Date getSamplingTimeStart() {
        return samplingTimeStart;
    }

    public void setSamplingTimeStart(Date samplingTimeStart) {
        this.samplingTimeStart = samplingTimeStart;
    }

    public Date getSamplingTimeEnd() {
        return samplingTimeEnd;
    }

    public void setSamplingTimeEnd(Date samplingTimeEnd) {
        this.samplingTimeEnd = samplingTimeEnd;
    }

    public int getIncrementTimeInterval() {
        return incrementTimeInterval;
    }

    public void setIncrementTimeInterval(int incrementTimeInterval) {
        this.incrementTimeInterval = incrementTimeInterval;
    }

    public TimeUnit getIncrementTimeUnit() {
        return incrementTimeUnit;
    }

    public void setIncrementTimeUnit(TimeUnit incrementTimeUnit) {
        this.incrementTimeUnit = incrementTimeUnit;
    }

    public String getTimeColumn() {
        return timeColumn;
    }

    public void setTimeColumn(String timeColumn) {
        this.timeColumn = timeColumn;
    }

}
