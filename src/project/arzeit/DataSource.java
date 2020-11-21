package project.arzeit;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.*;

public class DataSource {

    private HikariConfig config = new HikariConfig();
    private HikariDataSource ds;

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public DataSource() {
        config.setJdbcUrl("jdbc:mysql://localhost/arzeit_1?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        config.setUsername("online");
        config.setPassword("password");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }

    public static void main(String[] args) {
        DataSource DS = new DataSource();
        ResultSet result;

        try {

        Connection con = DS.getConnection();
        PreparedStatement set = con.prepareStatement("INSERT login VALUES (\"test\", \"pass\")");
        PreparedStatement get = con.prepareStatement("SELECT * FROM login");
        PreparedStatement fin = con.prepareStatement("DELETE FROM login");

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
