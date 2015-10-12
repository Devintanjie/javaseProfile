package com.taobao.bird.extractor.binlog.event;

import java.util.BitSet;

import com.taobao.bird.extractor.binlog.LogBuffer;
import com.taobao.bird.extractor.binlog.LogEvent;

/**
 * @desc
 * @author junyu 
 * @version
 **/
public final class TableMapLogEvent extends LogEvent {

    /**
     * Fixed data part:
     * <ul>
     * <li>6 bytes. The table ID.</li>
     * <li>2 bytes. Reserved for future use.</li>
     * </ul>
     * <p>
     * Variable data part:
     * <ul>
     * <li>1 byte. The length of the database name.</li>
     * <li>Variable-sized. The database name (null-terminated).</li>
     * <li>1 byte. The length of the table name.</li>
     * <li>Variable-sized. The table name (null-terminated).</li>
     * <li>Packed integer. The number of columns in the table.</li>
     * <li>Variable-sized. An array of column types, one byte per column.</li>
     * <li>Packed integer. The length of the metadata block.</li>
     * <li>Variable-sized. The metadata block; see log_event.h for contents and
     * format.</li>
     * <li>Variable-sized. Bit-field indicating whether each column can be NULL,
     * one bit per column. For this field, the amount of storage required for N
     * columns is INT((N+7)/8) bytes.</li>
     * </ul>
     * Source : http://forge.mysql.com/wiki/MySQL_Internals_Binary_Log
     */
    protected final String dbname;
    protected final String tblname;

    /**
     * Holding mysql column information.
     */
    public static final class ColumnInfo {

        public int type;
        public int meta;
    }

    protected final int          columnCnt;
    protected final ColumnInfo[] columnInfo;         // buffer for field
                                                      // metadata

    protected final long         tableId;
    protected BitSet             nullBits;

    /** TM = "Table Map" */
    public static final int      TM_MAPID_OFFSET = 0;
    public static final int      TM_FLAGS_OFFSET = 6;

    /**
     * Constructor used by slave to read the event from the binary log.
     */
    public TableMapLogEvent(LogHeader header, LogBuffer buffer, FormatDescriptionLogEvent descriptionEvent){
        super(header);

        final int commonHeaderLen = descriptionEvent.commonHeaderLen;
        final int postHeaderLen = descriptionEvent.postHeaderLen[header.type - 1];
        /* Read the post-header */
        buffer.position(commonHeaderLen + TM_MAPID_OFFSET);
        if (postHeaderLen == 6) {
            /*
             * Master is of an intermediate source tree before 5.1.4. Id is 4
             * bytes
             */
            tableId = buffer.getUint32();
        } else {
            // DBUG_ASSERT(post_header_len == TABLE_MAP_HEADER_LEN);
            tableId = buffer.getUlong48();
        }
        // flags = buffer.getUint16();

        /* Read the variable part of the event */
        buffer.position(commonHeaderLen + postHeaderLen);
        dbname = buffer.getString();
        buffer.forward(1); /* termination null */
        tblname = buffer.getString();
        buffer.forward(1); /* termination null */

        // Read column information from buffer
        columnCnt = (int) buffer.getPackedLong();
        columnInfo = new ColumnInfo[columnCnt];
        for (int i = 0; i < columnCnt; i++) {
            ColumnInfo info = new ColumnInfo();
            info.type = buffer.getUint8();
            columnInfo[i] = info;
        }

        if (buffer.position() < buffer.limit()) {
            final int fieldSize = (int) buffer.getPackedLong();
            decodeFields(buffer, fieldSize);
            nullBits = buffer.getBitmap(columnCnt);
        }
    }

    /**
     * Decode field metadata by column types.
     * 
     * @see mysql-5.1.60/sql/rpl_utility.h
     */
    private final void decodeFields(LogBuffer buffer, final int len) {
        final int limit = buffer.limit();

        buffer.limit(len + buffer.position());
        for (int i = 0; i < columnCnt; i++) {
            ColumnInfo info = columnInfo[i];

            switch (info.type) {
                case MYSQL_TYPE_TINY_BLOB:
                case MYSQL_TYPE_BLOB:
                case MYSQL_TYPE_MEDIUM_BLOB:
                case MYSQL_TYPE_LONG_BLOB:
                case MYSQL_TYPE_DOUBLE:
                case MYSQL_TYPE_FLOAT:
                case MYSQL_TYPE_GEOMETRY:
                    /*
                     * These types store a single byte.
                     */
                    info.meta = buffer.getUint8();
                    break;
                case MYSQL_TYPE_SET:
                case MYSQL_TYPE_ENUM:
                    /*
                     * log_event.h : MYSQL_TYPE_SET & MYSQL_TYPE_ENUM : This
                     * enumeration value is only used internally and cannot
                     * exist in a binlog.
                     */
                    logger.warn("This enumeration value is only used internally "
                                + "and cannot exist in a binlog: type=" + info.type);
                    break;
                case MYSQL_TYPE_STRING: {
                    /*
                     * log_event.h : The first byte is always
                     * MYSQL_TYPE_VAR_STRING (i.e., 253). The second byte is the
                     * field size, i.e., the number of bytes in the
                     * representation of size of the string: 3 or 4.
                     */
                    int x = (buffer.getUint8() << 8); // real_type
                    x += buffer.getUint8(); // pack or field length
                    info.meta = x;
                    break;
                }
                case MYSQL_TYPE_BIT:
                    info.meta = buffer.getUint16();
                    break;
                case MYSQL_TYPE_VARCHAR:
                    /*
                     * These types store two bytes.
                     */
                    info.meta = buffer.getUint16();
                    break;
                case MYSQL_TYPE_NEWDECIMAL: {
                    int x = buffer.getUint8() << 8; // precision
                    x += buffer.getUint8(); // decimals
                    info.meta = x;
                    break;
                }
                case MYSQL_TYPE_TIME2:
                case MYSQL_TYPE_DATETIME2:
                case MYSQL_TYPE_TIMESTAMP2: {
                    info.meta = buffer.getUint8();
                    break;
                }
                default:
                    info.meta = 0;
                    break;
            }
        }
        buffer.limit(limit);
    }

    public final String getDbName() {
        return dbname;
    }

    public final String getTableName() {
        return tblname;
    }

    public final int getColumnCnt() {
        return columnCnt;
    }

    public final ColumnInfo[] getColumnInfo() {
        return columnInfo;
    }

    public final long getTableId() {
        return tableId;
    }
}
