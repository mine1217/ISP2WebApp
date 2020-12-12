package project.arzeit.database;

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
        config.setJdbcUrl("jdbc:mysql://119.25.128.10/arzeit_1?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        config.setUsername("online"); //ユーザ　変えない
        config.setPassword("password"); //パス　変えない

        config.addDataSourceProperty("cachePrepStmts", "true");       //ステートメント(SQLの命令)をキャッシュするか
        config.addDataSourceProperty("prepStmtCacheSize", "250");     //キャッシュの確保数
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");//最大文字数
        config.addDataSourceProperty("maxLifetime","590000");

        ds = new HikariDataSource(config);
    }

}
