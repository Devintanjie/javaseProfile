package com.taobao.bird.extractor.full.mysql;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.taobao.bird.common.log.dynamic.BirdLogFactory;
import com.taobao.bird.common.model.runtime.BirdColumnValue;
import com.taobao.bird.common.model.runtime.BirdRecord;
import com.taobao.bird.common.model.runtime.ColumnMeta;
import com.taobao.bird.common.utils.BirdSqlUtils;
import com.taobao.bird.core.ringbuffer.RecordsEvent;
import com.taobao.bird.core.ringbuffer.TableMeta;

/**
 * @desc
 * @author junyu 2015年10月13日下午1:05:31
 * @version
 **/
public class MySQLRecordFetcher implements RecordFetcher {

    private static final String crawSql     = "select {0} from `{1}` where `{2}` > ? limit ?,?";

    private Map<String, String> tableSqlMap = new HashMap<String, String>();

    public void clearTableSqlMap() {
        this.tableSqlMap.clear();
    }

    private final String sourceEncoding;
    private final String targetEncoding;
    private final int    batchCount;

    public MySQLRecordFetcher(String sourceEncoding, String targetEncoding, int batchCount){
        this.sourceEncoding = sourceEncoding;
        this.targetEncoding = targetEncoding;
        this.batchCount = batchCount;
    }

    protected String getCrawlSql(TableMeta meta, String firstPk) {
        String sql = tableSqlMap.get(meta.getTable());
        if (sql == null) {
            String colStr = BirdSqlUtils.makeQuoteMysqlColumns(meta.getColumns());

            sql = new MessageFormat(crawSql).format(new Object[] { colStr, meta.getTable(), firstPk });
            tableSqlMap.put(meta.getTable(), sql);
            BirdLogFactory.getTaskLog().info("put the sql!sql:" + sql + " realtable:" + meta.getTable());
        }

        return sql;
    }

    @Override
    public void fetch(RecordsEvent event) throws Exception{
        Connection conn = null;
        PreparedStatement ps = null;
        List<String> pks = event.getRc().getMeta().getPrimaryKeys();
        List<BirdRecord> results = Lists.newArrayList();
        String firstPk = pks.get(0);
        try {
            String sql = getCrawlSql(event.getRc().getMeta(), firstPk);
            conn = event.getRc().getSourceDs().getConnection();
            ps = conn.prepareStatement(sql);
            Object oriId = event.getRc().getLastId();
            ps.setObject(1, oriId);
            ps.setInt(2, event.getRc().getBatchTime() * batchCount);
            // 多取一条
            ps.setInt(3, batchCount + 1);
            ResultSet rs = ps.executeQuery();
            int i = 0;
            Object prevFirstPk = null;
            boolean con = false;
            while (rs.next()) {
                i++;
                Object currFirstPk = null;
                List<BirdColumnValue> cvl = new ArrayList<BirdColumnValue>();
                List<BirdColumnValue> primaryColumns = new ArrayList<BirdColumnValue>();
                List<BirdColumnValue> condCv = new ArrayList<BirdColumnValue>();
                for (ColumnMeta col : event.getRc().getMeta().getColumns()) {
                    Object value = BirdSqlUtils.getMysqlValue(rs, col, sourceEncoding, targetEncoding);

                    BirdColumnValue cv = new BirdColumnValue();
                    cv.setColumn(col);
                    cv.setValue(value);
                    cvl.add(cv);

                    for (String s : event.getRc().getMeta().getShardKeys()) {
                        if (s.equals(col.getName())) {
                            condCv.add(cv);
                        }
                    }

                    if (pks.contains(col.getName())) {
                        if (col.getName().equals(firstPk)) {
                            // 最后一个pk不加入下一次查询中
                            if (i != (batchCount + 1)) {
                                event.setId(cv.getValue());
                                event.getRc().setLastId(cv.getValue());
                            }

                            currFirstPk = cv.getValue();
                        }

                        primaryColumns.add(cv);
                    }
                }

                BirdRecord re = new BirdRecord();
                re.setColumns(cvl);
                re.setPrimaryKeys(primaryColumns);
                // 最后一条不加入
                if (i == (batchCount + 1)) {
                    // 如果是多主键，那么判定最后一条和最后第二条是否第一个主键一样，一样的话那么继续往下扫
                    if (pks.size() > 1) {
                        if (prevFirstPk != null && currFirstPk != null) {
                            if (prevFirstPk.equals(currFirstPk)) {
                                con = true;
                            } else {
                                con = false;
                            }
                        } else {
                            throw new IllegalArgumentException("pk is null!!table is "
                                                               + event.getRc().getMeta().getTable());
                        }
                    }
                } else {
                    // 不过滤才加入
                    results.add(re);
                }

                prevFirstPk = currFirstPk;
            }

            event.setResult(results);

            if (i < 1) {
                event.getRc().setLastBatch(true);
                event.getRc().resetBatchTime();
            } else if (pks.size() > 1) {
                if (con) {
                    event.setId(oriId);
                    event.getRc().setLastId(oriId);
                    event.getRc().incrementBatchTime();
                } else {
                    event.getRc().resetBatchTime();
                }
            }
        } finally {
            if (ps != null) {
                ps.close();
            }

            if (conn != null) {
                conn.close();
            }
        }
    }
}
