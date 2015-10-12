package com.taobao.bird.common.model.config.manager;

/**
 * @desc
 * @author junyu
 * @version
 **/
public interface BirdConfigListener<T,X> {

    public void handleConfigChange(T config);
    
    public void handleConfigDelete(X config);
}
