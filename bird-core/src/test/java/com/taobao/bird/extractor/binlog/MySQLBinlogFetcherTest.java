package com.taobao.bird.extractor.binlog;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.junit.Assert;
import org.junit.Test;

/**
 * @desc
 * @author junyu
 * @version
 **/
public class MySQLBinlogFetcherTest {

    @Test
    public void testMySQLBinlogFetcher() {
        DirectLogFetcher f = null;
        LogDecoder d = new LogDecoder();
        LogPosition p = new LogPosition("mysql-bin.000002", 844);
        LogContext c = new LogContext();
        c.setLogPosition(p);
        Connection conn;
        try {
            f = new DirectLogFetcher();
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/binlog_test", "bucket", "74123963");
            Statement statement = conn.createStatement();
            statement.executeQuery("set @master_binlog_checksum= '@@global.binlog_checksum'");
            f.open(conn, p.getFileName(), p.getPosition(), 7);
            while (!Thread.interrupted()) {
                f.fetch();
                LogEvent e = d.decode(f, c);
                System.out.println(LogEvent.getTypeName(e.getHeader().getType()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        } finally {
            try {
                f.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
