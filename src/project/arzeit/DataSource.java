package project.arzeit;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.*;

public class HikariCPDataSource {

    private HikariConfig config = new HikariConfig();
    private HikariDataSource ds;

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public HikariCPDataSource(){
        config.setJdbcUrl("jdbc:mysql://119.25.128.10/test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        config.setUsername("online");
        config.setPassword("password");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }
}
