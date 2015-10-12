package com.taobao.bird.common.model.stage;

import com.taobao.bird.common.model.config.StageNode.StageProcess;

/**
 * @desc
 * @author junyu 
 * @version
 **/
public class AgentStart extends OperateStage {

    @Override
    public Stage stop() {
        return new Initialization();
    }

    @Override
    public Stage next() {
        return stop();
    }

    @Override
    public Stage next(StageProcess stateProcess) {
        if (stateProcess == StageProcess.AGENT) {
            return next();
        } else {
            throw new IllegalStateException("process is " + stateProcess + ", but current state is " + this);
        }
    }

    @Override
    public Stage stop(StageProcess stateProcess) {
        if (stateProcess == StageProcess.AGENT) {
            return stop();
        } else {
            throw new IllegalStateException("process is " + stateProcess + ", but current state is " + this);
        }
    }
}
