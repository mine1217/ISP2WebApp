package project.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
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

        String expected = "password";
        String id = "test";

        SimpleEntry<String, Integer> result;
        result = dcon.getPass(id);
        if(result.getValue() != 0) {
            System.out.println("error code = " + result.getValue());
        } else {
            System.out.println(result.getKey());
        }
        assertEquals(expected, result.getKey());
    }

    @Test
    public void ニックネームゲッターテスト() throws Exception {

        DataSource ds = new DataSource();
        DBController  dcon = new DBController(ds);

        String expected = "namae";
        String id = "test";

        SimpleEntry<String, Integer> result;
        result = dcon.getName(id);
        if(result.getValue() != 0) {
            System.out.println("error code = " + result.getValue());
        } else {
            System.out.println(result.getKey());
        }
        assertEquals(expected, result.getKey());
    }

    @Test
    public void スケジュールゲッターテスト() throws Exception {

        DataSource ds = new DataSource();
        DBController  dcon = new DBController(ds);

        String month = "2020-11-01";
        String id = "test";

        ArrayList<String> expected = new ArrayList<>();
        expected.add("1,2020-11-24 00:00:00,2020-11-25 00:00:00,980");
        expected.add("2,2020-11-27 00:00:00,2020-11-28 00:00:00,980");

        SimpleEntry<ArrayList<String>, Integer> result;
        result = dcon.getScheduleAtMonth(id, month);
        if(result.getValue() != 0) {
            System.out.println("error code = " + result.getValue());
        } else {
            int count = 0;
            for(String s : result.getKey()) {
                System.out.println(s);
                assertEquals(expected.get(count), s);
                count++;
            }
        }
        
    }

    @Test
    public void アカウントセット削除テスト() throws Exception {

        DataSource ds = new DataSource();
        DBController  dcon = new DBController(ds);

        String id = "testAccount";
        String pass = "testPass";
        String result;

        ArrayList<String> expected = new ArrayList<>();
        expected.add("testAccount");
        expected.add("testPass");

        int code = dcon.setAccount(id, pass);
        if(code != 0) {
            System.out.println("error code = " + code);
        } else {
            result = dcon.getPass(id).getKey();
            System.out.println(result);
            assertEquals(expected.get(1), result);
        }

        code = dcon.deleteAccount(id);
        if(code != 0) {
            System.out.println("error code = " + code);
        } else {
            assertEquals(null, dcon.getPass(id).getKey());
        }
    }
}