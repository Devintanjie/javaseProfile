package com.taobao.bird.common.model.stage;

import org.apache.commons.lang.StringUtils;

public abstract class OperateStage implements Stage {
    public String getStageValue() {
        return getClassWithoutPackage(this);
    }

    @Override
    public Stage reset() {
        return new Initialization();
    }

    protected String getClassWithoutPackage(OperateStage stage) {
        if (null == stage) {
            throw new IllegalArgumentException("stage can't not be null.");
        }
        String[] strings = stage.getClass().getName().split("\\.");
        return strings[strings.length - 1];
    }

    public static OperateStage fromStringToStage(String operateValue) {
        if (StringUtils.isEmpty(operateValue)) {
            throw new IllegalArgumentException("operateValue can't not be null.");
        }

        OperateStage operate = null;
        switch (operateValue) {
            case "Initialization":
                operate = new Initialization();
                break;
            case "GetPosition":
                operate = new GetPosition();
                break;
            case "PositionMarked":
                operate = new PositionMarked();
                break;
            case "FullTransfer":
                operate = new FullTransfer();
                break;
            case "FullComplete":
                operate = new FullComplete();
                break;
            case "IncreTransfer":
                operate = new IncreTransfer();
                break;
            case "IncreCatchup":
                operate = new IncreCatchup();
                break;
            case "IncreStopWrite":
                operate = new IncreStopWrite();
                break;
            case "Complete":
                operate = new Complete();
                break;
            case "Check":
                operate = new Check();
                break;
            case "AgentStart":
                operate=new AgentStart();
                break;
            default:
                throw new IllegalArgumentException("not support OperateValue:" + operateValue);
        }
        return operate;
    }
}
