package com.taobao.bird.applier;


/**
 * @desc 
 * @author junyu 2015年10月4日下午10:39:18
 * @version 
 **/
public interface Applier<T> {
    public void apply(T data);
}
