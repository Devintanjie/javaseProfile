package com.taobao.bird.common.lifecycle;


/**
 * @desc
 * @author junyu
 * @version
 **/
public interface BirdLifeCycle {
    public void init();

    public void start();

    public void stop();

    public void abort(String why, Throwable e);

    public boolean isStart();

    public boolean isStop();
}
