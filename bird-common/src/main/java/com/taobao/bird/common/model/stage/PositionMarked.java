package com.taobao.bird.common.model.stage;

import com.taobao.bird.common.model.config.StageNode.StageProcess;

public class PositionMarked extends OperateStage {

    @Override
    public Stage stop() {
        return this;
    }

    @Override
    public Stage next() {
        return new FullTransfer();
    }

    @Override
    public Stage next(StageProcess stateProcess) {
        if (stateProcess == StageProcess.ALL) {
            return next();
        } else if (stateProcess == StageProcess.INCRE_ONLY) {
            return new IncreTransfer();
        } else {
            throw new IllegalStateException("process is " + stateProcess + ", but current state is " + this);
        }
    }

    @Override
    public Stage stop(StageProcess stateProcess) {
        return stop();
    }
}
