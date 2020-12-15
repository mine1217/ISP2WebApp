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

        config.setMaximumPoolSize(10); //最大プールサイズ
        config.setMinimumIdle(1); //最小プールサイズ
        config.setMaxLifetime(580000); //プールにおいておく接続のライフタイム

        ds = new HikariDataSource(config);
    }

}
