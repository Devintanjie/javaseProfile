package com.taobao.bird.common.model.stage;

import com.taobao.bird.common.model.config.StageNode.StageProcess;

public interface Stage {

    public String getStageValue();

    public Stage reset();

    public Stage stop();

    public Stage next();

    public Stage next(StageProcess stageProcess);

    public Stage stop(StageProcess stageProcess);
}
