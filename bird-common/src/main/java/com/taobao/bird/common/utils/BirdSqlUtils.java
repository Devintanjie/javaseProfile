package com.taobao.bird.common.utils;

import java.io.UnsupportedEncodingException;
import java.sql.Types;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.taobao.bird.common.model.runtime.BirdColumnValue;
import com.taobao.bird.common.model.runtime.ColumnMeta;

/**
 * @desc
 * @author junyu 2015年2月17日下午12:12:43
 * @version
 **/
public class BirdSqlUtils {

    public static String makeQuoteUpdateSet(List<BirdColumnValue> cvl) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < cvl.size(); i++) {
            sb.append("`");
            sb.append(cvl.get(i).getColumn().getName());
            sb.append("`");
            sb.append("=? ");
            if (i != (cvl.size() - 1)) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * 根据列的数量拼写insert into tab values 后的参数字符串 ?,?,?,...
     * 
     * @param cols
     * @return
     */
    public static String makeInsertValues(List<ColumnMeta> cols) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < cols.size(); i++) {
            sb.append("?");
            if (i != (cols.size() - 1)) {
                sb.append(",");
            }
        }

        return sb.toString();
    }

    public static String makeQuoteMysqlColumns(List<ColumnMeta> cols) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < cols.size(); i++) {
            sb.append("`");
            sb.append(cols.get(i).getName());
            sb.append("`");
            if (i != (cols.size() - 1)) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public static boolean isCharType(int sqlType) {
        if (sqlType == Types.CHAR || sqlType == Types.VARCHAR || sqlType == Types.NCHAR || sqlType == Types.NVARCHAR) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isClobType(int sqlType) {
        if (sqlType == Types.CLOB || sqlType == Types.LONGVARCHAR || sqlType == Types.NCLOB
            || sqlType == Types.LONGNVARCHAR) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isBlobType(int sqlType) {
        if (sqlType == Types.BLOB || sqlType == Types.BINARY || sqlType == Types.VARBINARY
            || sqlType == Types.LONGVARBINARY) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNumber(int sqlType) {
        if (sqlType == Types.TINYINT || sqlType == Types.SMALLINT || sqlType == Types.INTEGER
            || sqlType == Types.BIGINT || sqlType == Types.NUMERIC || sqlType == Types.DECIMAL) {
            return true;
        } else {
            return false;
        }
    }

    public static Object encoding(Object source, int sqlType, String sourceEncoding, String targetEncoding) {
        switch (sqlType) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
            case Types.CLOB:
            case Types.NCLOB:
                if (source instanceof String) {
                    String str = (String) source;
                    if (false == StringUtils.isEmpty(str)) {
                        if (false == StringUtils.equalsIgnoreCase(sourceEncoding, targetEncoding)) {
                            try {
                                byte[] bytes = null;
                                if (StringUtils.isBlank(sourceEncoding)) {
                                    bytes = str.getBytes();
                                } else {
                                    bytes = str.getBytes(sourceEncoding);
                                }

                                if (StringUtils.isBlank(targetEncoding)) {
                                    return new String(bytes);
                                } else {
                                    return new String(bytes, targetEncoding);
                                }
                            } catch (UnsupportedEncodingException e) {
                                throw new IllegalArgumentException(e.getMessage(), e);
                            }
                        }
                    }
                }
        }

        return source;
    }
}
