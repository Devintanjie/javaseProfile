package com.taobao.bird.common.model.config.manager;

/**
 * @desc
 * @author junyu 2015��2��18������1:08:22
 * @version
 **/
public interface BirdConfigListener<T,X> {

    public void handleConfigChange(T config);
    
    public void handleConfigDelete(X config);
}
