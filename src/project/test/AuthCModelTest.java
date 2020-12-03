package project.test;

import static org.junit.Assert.assertEquals;

import org.junit.*;

import project.arzeit.database.DBController;
import project.arzeit.database.DataSource;
import project.arzeit.model.AuthCModel;

/**
 * AuthCModel-認証処理をするプログラム　の単体テスト
 * 
 * @author Minoru Makino
 */
public class AuthCModelTest {
    @Test
    public void ログイン処理テスト_非ハッシュ() throws Exception {

        DataSource ds = new DataSource();
        AuthCModel model = new AuthCModel(ds);

        int expected1 = 0, 
            expected2 = 3,  //id無いよ
            expected3 = 10, //pass合わないよ

            result1, 
            result2, 
            result3;

        dc.setAccount("test2", "pass2");

        result1 = model.login("test2", "pass2");
        result2 = model.login("test3", "pass2");
        result3 = model.login("test2", "pass3");

        dc.deleteAccount("test2");

        assertEquals(expected1, result1);
        assertEquals(expected2, result2);
        assertEquals(expected3, result3);
    }

    @Test
    public void 登録処理テスト_非ハッシュ() throws Exception {

        DataSource ds = new DataSource();
        AuthCModel model = new AuthCModel(ds);

        int expected1 = 0, //成功
            expected2 = 1, //重複
            result1, result2;

        dc.setAccount("test3", "pass3"); //重複させるために余分にアカウント作る

        result1 = model.register("test2", "pass2");
        result2 = model.register("test3", "pass3");

        dc.deleteAccount("test2");
        dc.deleteAccount("test3");

        assertEquals(expected1, result1);
        assertEquals(expected2, result2);
    }
}