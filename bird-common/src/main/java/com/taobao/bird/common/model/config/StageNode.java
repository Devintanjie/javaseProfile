package com.taobao.bird.common.model.config;

import com.alibaba.fastjson.JSONObject;
import com.taobao.bird.common.model.stage.OperateStage;

/**
 * @desc
 * @author junyu 
 * @version
 **/
public class StageNode extends SimpleNode {

    private String             ownerNodeId;

    protected boolean          autoRun                 = false;

    private OperateStage       stage;

    private StageProcess       stageProcess            = StageProcess.ALL;

    public static final String DEFAULT_STAGE_NODE_NAME = "stage";

    @Override
    public JSONObject attrToJson() {
        JSONObject j = super.attrToJson();
        j.put("autoRun", this.autoRun);
        j.put("stage", stage.getStageValue());
        j.put("stageProcess", stageProcess);
        return j;
    }

    @Override
    public void jsonToAttr(JSONObject json) {
        super.jsonToAttr(json);
        if (json.containsKey("autoRun")) {
            this.autoRun = json.getBooleanValue("autoRun");
        }

        if (json.containsKey("stage")) {
            this.stage = OperateStage.fromStringToStage(json.getString("stage"));
        } else {
            throw new IllegalArgumentException("no state value at all");
        }

        if (json.containsKey("stageProcess")) {
            this.stageProcess = StageProcess.valueOf(json.getString("stageProcess"));
        }
    }

    public String getOwnerNodeId() {
        return ownerNodeId;
    }

    public void setOwnerNodeId(String ownerNodeId) {
        this.ownerNodeId = ownerNodeId;
    }

    public boolean isAutoRun() {
        return autoRun;
    }

    public void setAutoRun(boolean autoRun) {
        this.autoRun = autoRun;
    }

    public StageProcess getStageProcess() {
        return stageProcess;
    }

    public void setStageProcess(StageProcess stageProcess) {
        this.stageProcess = stageProcess;
    }

    public OperateStage getStage() {
        return stage;
    }

    public void setStage(OperateStage stage) {
        this.stage = stage;
    }

    public enum StageProcess {
        ALL, FULL_ONLY, INCRE_ONLY, CHECK,AGENT
    }

}
