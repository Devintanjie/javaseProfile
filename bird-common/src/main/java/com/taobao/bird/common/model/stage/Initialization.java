package com.taobao.bird.common.model.stage;

import com.taobao.bird.common.model.config.StageNode.StageProcess;

public class Initialization extends OperateStage {

    @Override
    public Stage stop() {
        return this;
    }

    @Override
    public Stage next() {
        return new GetPosition();
    }

    @Override
    public Stage next(StageProcess stageProcess) {
        if (stageProcess == StageProcess.ALL) {
            return next();
        } else if (stageProcess == StageProcess.FULL_ONLY) {
            return new FullTransfer();
        } else if (stageProcess == StageProcess.INCRE_ONLY) {
            return next();
        } else if (stageProcess == StageProcess.CHECK) {
            return new Check();
        } else if (stageProcess == StageProcess.AGENT) {
            return new AgentStart();
        } else {
            throw new IllegalStateException("process is " + stageProcess + ", but current stage is " + this);
        }
    }

    @Override
    public Stage stop(StageProcess stageProcess) {
        return stop();
    }
}
