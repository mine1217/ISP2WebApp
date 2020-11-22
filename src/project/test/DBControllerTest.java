package project.test;

import static org.junit.Assert.assertEquals;

import java.util.AbstractMap.SimpleEntry;

import org.junit.*;

import project.arzeit.database.DBController;
import project.arzeit.database.DataSource;

/**
 * DB接続確認テスト　要実行！！
 * @author Minoru Makino
 */
public class DBControllerTest {
    @Test
    public void パスワードゲッターテスト() throws Exception {

        DataSource ds = new DataSource();
        DBController  dcon = new DBController(ds);

        String expected = "pass";
        String id = "test";

        SimpleEntry<String, Integer> result;
        result = dcon.getPass(id);
        if(result.getValue() != 0) {
            System.out.println("error code = " + result.getValue());
        } else {
            System.out.println(result.getKey());
            assertEquals(expected, result.getKey());
        }
    }
}