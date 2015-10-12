package com.taobao.bird.common.model.config;

/**
 * @description
 * @author <a href="junyu@taobao.com">junyu</a>
 * @version 1.0
 * @since 1.6
 */
public enum DBType {
    ORACLE(0), MYSQL(1), INFOBRIGHT(2), HBASE(3), OB(4), DRDS(5);

    private int i;

    private DBType(int i){
        this.i = i;
    }

    public int value() {
        return this.i;
    }

    public static DBType valueOf(int i) {
        for (DBType t : values()) {
            if (t.value() == i) {
                return t;
            }
        }

        throw new IllegalArgumentException("Invalid SqlType:" + i);
    }
}
