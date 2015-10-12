package com.taobao.bird.common.log;

import java.io.File;

/**
 * @desc
 * @author junyu 
 * @version
 **/
public interface LoggerAdapter {

    public Logger getLogger(Class<?> cls);

    public Logger getLogger(String key);

    public void setLevel(Level level);

    public Level getLevel();

    public File getFile();

    public void setFile(File file);

}
