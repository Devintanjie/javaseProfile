package com.taobao.bird.common.model.stage;

import com.taobao.bird.common.model.config.StageNode.StageProcess;

public class GetPosition extends OperateStage {

    @Override
    public Stage stop() {
        return new Initialization();
    }

    @Override
    public Stage next() {
        return new PositionMarked();
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
        return stop();
    }

}
