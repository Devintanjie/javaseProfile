package com.taobao.bird.common.model.config;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @desc
 * @author junyu 2015年2月19日下午5:20:28
 * @version
 **/
public class AgentNode extends SimpleNode {

    private volatile Set<String> taskNames = new HashSet<String>();

    private StageNode            stageNode;

    @Override
    public JSONObject attrToJson() {
        JSONObject json = super.attrToJson();
        JSONArray jsonArray = new JSONArray();
        for (String taskName : this.taskNames) {
            jsonArray.add(taskName);
        }

        json.put("taskNames", jsonArray);
        return json;
    }

    @Override
    public void jsonToAttr(JSONObject json) {
        super.jsonToAttr(json);
        JSONArray jsonArray = json.getJSONArray("taskNames");
        if (null != jsonArray) {
            Set<String> newTaskNames = new HashSet<String>(jsonArray.size());
            for (int i = 0; i < jsonArray.size(); i++) {
                String taskName = jsonArray.getString(i);
                if (StringUtils.isNotBlank(taskName)) {
                    newTaskNames.add(taskName);
                }
            }
            this.taskNames = newTaskNames;
        }
    }

    public Set<String> getTaskNames() {
        return taskNames;
    }

    public void setTaskNames(Set<String> taskNames) {
        this.taskNames = taskNames;
    }

    public StageNode getStageNode() {
        return stageNode;
    }

    public void setStageNode(StageNode stageNode) {
        this.stageNode = stageNode;
    }
}
