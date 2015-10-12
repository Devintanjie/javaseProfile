package com.taobao.bird.common.model.stage;

import com.taobao.bird.common.model.config.StageNode.StageProcess;

public class Check extends OperateStage {

    @Override
    public Stage stop() {
        return new Initialization();
    }

    @Override
    public Stage next() {
        return new Complete();
    }

    @Override
    public Stage next(StageProcess stateProcess) {
        if (stateProcess == StageProcess.CHECK) {
            return next();
        } else {
            throw new IllegalStateException("process is " + stateProcess + ", but current state is " + this);
        }
    }

    @Override
    public Stage stop(StageProcess stateProcess) {
        if (stateProcess == StageProcess.CHECK) {
            return stop();
        } else {
            throw new IllegalStateException("process is " + stateProcess + ", but current state is " + this);
        }
    }

}
