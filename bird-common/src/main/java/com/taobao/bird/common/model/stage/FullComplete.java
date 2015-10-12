package com.taobao.bird.common.model.stage;

import com.taobao.bird.common.model.config.StageNode.StageProcess;

public class FullComplete extends OperateStage {

    @Override
    public Stage stop() {
        return this;
    }

    @Override
    public Stage next() {
        return new IncreTransfer();
    }

    @Override
    public Stage next(StageProcess stateProcess) {
        if (stateProcess == StageProcess.ALL) {
            return next();
        } else {
            throw new IllegalStateException("process is " + stateProcess + ", but current state Is " + this);
        }
    }

    @Override
    public Stage stop(StageProcess stateProcess) {
        if (stateProcess == StageProcess.ALL) {
            return stop();
        }else{
            throw new IllegalStateException("process is " + stateProcess + ", but current state is " + this);
        }
    }

}
