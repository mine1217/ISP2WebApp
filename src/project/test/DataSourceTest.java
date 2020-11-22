package project.test;

import static org.junit.Assert.assertEquals;

import java.sql.*;

import org.junit.*;

import project.arzeit.database.DataSource;

/**
 * DB接続確認テスト　要実行！！
 * @author Minoru Makino
 */
public class DataSourceTest {
    @Test
    public void DB接続テスト() throws Exception {

        DataSource DS = new DataSource();
        ResultSet result;

        Connection con = DS.getConnection();

        //set　入れて get 取って fin 消す
        PreparedStatement set = con.prepareStatement("INSERT INTO login VALUES ('test', 'pass');");
        PreparedStatement get = con.prepareStatement("SELECT * FROM login");
        PreparedStatement fin = con.prepareStatement("DELETE FROM login");

        set.executeUpdate();
        result = get.executeQuery();
        fin.executeUpdate();

        String expected = "test"+"pass";
        System.out.println(expected);

        result.next();
        assertEquals(expected, (result.getString("id")+result.getString("pass")));

        con.close();
        result.close();
    }
}