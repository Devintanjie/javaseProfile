package com.taobao.bird.common.model.stage;

import com.taobao.bird.common.model.config.StageNode.StageProcess;

public class Complete extends OperateStage {

    @Override
    public Stage stop() {
        return reset();
    }

    @Override
    public Stage next() {
        return this;
    }

    @Override
    public Stage next(StageProcess stateProcess) {
        return next();
    }

    @Override
    public Stage stop(StageProcess stateProcess) {
        if (stateProcess == StageProcess.ALL) {
            return stop();
        } else if (stateProcess == StageProcess.FULL_ONLY || stateProcess == StageProcess.CHECK) {
            return new Initialization();
        } else if (stateProcess == StageProcess.INCRE_ONLY) {
            return new PositionMarked();
        } else {
            throw new IllegalStateException("process is " + stateProcess + ", but current state is " + this);
        }
    }

}
