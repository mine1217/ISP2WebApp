package project.arzeit;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.*;

/**
 * HikariCPを使ったデータプールのソース　DB接続の根っこ
 * @author Minoru Makino
 *  */
public class DataSource {

    private HikariConfig config = new HikariConfig(); //データプール設定
    private HikariDataSource ds; //データプールのインスタンス

    /**
     * Connectionのゲッター
     * @return SQLConnection
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return ds.getConnection(); 
    }

    /**
     * 主に初期設定
     */
    public DataSource() {
        config.setJdbcUrl("jdbc:mysql://IPアドレス/arzeit_1?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        config.setUsername("ユーザー"); //ユーザ　変えない
        config.setPassword("パスワ"); //パス　変えない

        config.addDataSourceProperty("cachePrepStmts", "true");       //ステートメント(SQLの命令)をキャッシュするか
        config.addDataSourceProperty("prepStmtCacheSize", "250");     //キャッシュの確保数
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");//最大文字数

        ds = new HikariDataSource(config);
    }

}
