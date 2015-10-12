package com.taobao.bird.common.log;

/**
 * @desc
 * @author junyu 
 * @version
 **/
public class FailsafeLogger implements Logger {

    private Logger logger;

    public FailsafeLogger(Logger logger){
        this.logger = logger;
    }

    @Override
    public void trace(String msg) {
        try {
            this.logger.trace(msg);
        } catch (Throwable e) {
        }
    }

    @Override
    public void trace(Throwable e) {
        try {
            this.logger.trace(e);
        } catch (Throwable e1) {
        }
    }

    @Override
    public void trace(String msg, Throwable e) {
        try {
            this.logger.trace(msg, e);
        } catch (Throwable e1) {
        }
    }

    @Override
    public void debug(String msg) {
        try {
            this.logger.debug(msg);
        } catch (Throwable e) {
        }
    }

    @Override
    public void debug(Throwable e) {
        try {
            this.logger.debug(e);
        } catch (Throwable e1) {
        }
    }

    @Override
    public void debug(String msg, Throwable e) {
        try {
            this.logger.debug(msg, e);
        } catch (Throwable e1) {
        }
    }

    @Override
    public void info(String msg) {
        try {
            this.logger.info(msg);
        } catch (Throwable e) {
        }
    }

    @Override
    public void info(Throwable e) {
        try {
            this.logger.info(e);
        } catch (Throwable e1) {
        }
    }

    @Override
    public void info(String msg, Throwable e) {
        try {
            this.logger.info(msg, e);
        } catch (Throwable e1) {
        }
    }

    @Override
    public void warn(String msg) {
        try {
            this.logger.warn(msg);
        } catch (Throwable e) {
        }
    }

    @Override
    public void warn(Throwable e) {
        try {
            this.logger.warn(e);
        } catch (Throwable e1) {
        }
    }

    @Override
    public void warn(String msg, Throwable e) {
        try {
            this.logger.warn(msg, e);
        } catch (Throwable e1) {
        }
    }

    @Override
    public void error(String msg) {
        try {
            this.logger.error(msg);
        } catch (Throwable e) {
        }
    }

    @Override
    public void error(Throwable e) {
        try {
            this.logger.error(e);
        } catch (Throwable e1) {
        }
    }

    @Override
    public void error(String msg, Throwable e) {
        try {
            this.logger.error(msg, e);
        } catch (Throwable e1) {
        }

    }

    @Override
    public boolean isTraceEnabled() {
        try {
            return this.logger.isTraceEnabled();
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public boolean isDebugEnabled() {
        try {
            return this.logger.isDebugEnabled();
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public boolean isInfoEnabled() {
        try {
            return this.logger.isInfoEnabled();
        } catch (Throwable e) {
            return false;
        }

    }

    @Override
    public boolean isWarnEnabled() {
        try {
            return this.logger.isWarnEnabled();
        } catch (Throwable e) {
            return false;
        }

    }

    @Override
    public boolean isErrorEnabled() {
        try {
            return this.logger.isErrorEnabled();
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public Object getDelegate() {
        return this.logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

}
