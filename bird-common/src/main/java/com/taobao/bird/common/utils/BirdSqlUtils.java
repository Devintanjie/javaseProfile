package com.taobao.bird.common.utils;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.taobao.bird.common.model.runtime.BirdColumnValue;
import com.taobao.bird.common.model.runtime.ColumnMeta;

/**
 * @desc
 * @author junyu
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

    public static Object getMysqlValue(ResultSet rs, ColumnMeta col, String sourceEncoding, String targetEncoding)
                                                                                                              throws SQLException,
                                                                                                              UnsupportedEncodingException {
        switch (col.getType()) {
            case Types.BIT:
                return rs.getInt(col.getName());
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
            case Types.CLOB:
            case Types.NCLOB:
                if (StringUtils.isNotBlank(sourceEncoding) && sourceEncoding.equals("latin1")) {
                    Object v = rs.getObject(col.getName());
                    if (v != null && v instanceof String) {
                        byte[] bytes = rs.getBytes(col.getName());
                        if (StringUtils.isNotBlank(targetEncoding)) {
                            v = new String(bytes, targetEncoding);
                        } else {
                            v = new String(bytes);
                        }

                        return v;
                    } else {
                        return v;
                    }
                } else {
                    return rs.getObject(col.getName());
                }
            case Types.TIMESTAMP:
            case Types.TIME:
            case Types.DATE:
                byte[] b = rs.getBytes(col.getName());
                if (b != null) {
                    return new String(b, "utf-8");
                } else {
                    return null;
                }
            default:
                return rs.getObject(col.getName());
        }
    }
}
