package com.taobao.bird.common.model.stage;

import com.taobao.bird.common.model.config.StageNode.StageProcess;

public class IncreCatchup extends OperateStage {

    @Override
    public Stage stop() {
        return new FullComplete();
    }

    @Override
    public Stage next() {
        return new Complete();
    }

    @Override
    public Stage next(StageProcess stateProcess) {
        if (stateProcess == StageProcess.ALL) {
            return next();
        } else if (stateProcess == StageProcess.INCRE_ONLY) {
            return next();
        } else {
            throw new IllegalStateException("process is " + stateProcess + ", but current state is " + this);
        }
    }

    @Override
    public Stage stop(StageProcess stateProcess) {
        if (stateProcess == StageProcess.ALL) {
            return stop();
        } else if (stateProcess == StageProcess.INCRE_ONLY) {
            return new PositionMarked();
        } else {
            throw new IllegalStateException("process is " + stateProcess + ", but current state is " + this);
        }
    }
}
