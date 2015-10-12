package com.taobao.bird.common.model.stage;

import com.taobao.bird.common.model.config.StageNode.StageProcess;

public class FullTransfer extends OperateStage {

    @Override
    public Stage stop() {
        return new PositionMarked();
    }

    @Override
    public Stage next() {
        return new FullComplete();
    }

    @Override
    public Stage next(StageProcess stateProcess) {
        if (stateProcess == StageProcess.ALL) {
            return next();
        } else if (stateProcess == StageProcess.FULL_ONLY) {
            return new Complete();
        } else {
            throw new IllegalStateException("process is " + stateProcess + ", but current state is " + this);
        }
    }

    @Override
    public Stage stop(StageProcess stateProcess) {
        if (stateProcess == StageProcess.ALL) {
            return stop();
        } else if (stateProcess == StageProcess.FULL_ONLY) {
            return new Initialization();
        } else {
            throw new IllegalStateException("process is " + stateProcess + ", but current state is " + this);
        }
    }

}
