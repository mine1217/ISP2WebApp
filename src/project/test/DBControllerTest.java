package project.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.AbstractMap.SimpleEntry;

import org.junit.*;

import project.arzeit.database.DBController;
import project.arzeit.database.DataSource;

/**
 * DB接続確認テスト 要実行！！
 * 
 * @author Minoru Makino
 */
public class DBControllerTest {
    @Test
    public void パスワードゲッターテスト() throws Exception {

        DataSource ds = new DataSource();
        DBController dcon = new DBController(ds);

        String expected = "password";
        String id = "test";

        SimpleEntry<String, Integer> result;
        result = dcon.getPass(id);
        if (result.getValue() != 0) {
            System.out.println("error code = " + result.getValue());
        } else {
            System.out.println(result.getKey());
        }
        assertEquals(expected, result.getKey());
    }

    @Test
    public void ニックネームゲッターテスト() throws Exception {

        DataSource ds = new DataSource();
        DBController dcon = new DBController(ds);

        String expected = "namae";
        String id = "test";

        SimpleEntry<String, Integer> result;
        result = dcon.getName(id);
        if (result.getValue() != 0) {
            System.out.println("error code = " + result.getValue());
        } else {
            System.out.println(result.getKey());
        }
        assertEquals(expected, result.getKey());
    }

    @Test
    public void スケジュールゲッターテスト() throws Exception {

        DataSource ds = new DataSource();
        DBController dcon = new DBController(ds);

        String month = "2020-11-01";
        String id = "test";

        ArrayList<String> expected = new ArrayList<>();
        expected.add("1,2020-11-24 00:00:00,2020-11-25 00:00:00,980");
        expected.add("2,2020-11-27 00:00:00,2020-11-28 00:00:00,980");

        SimpleEntry<ArrayList<String>, Integer> result;
        result = dcon.getScheduleAtMonth(id, month);
        if (result.getValue() != 0) {
            System.out.println("error code = " + result.getValue());
        } else {
            int count = 0;
            for (String s : result.getKey()) {
                System.out.println(s);
                assertEquals(expected.get(count), s);
                count++;
            }
        }

    }

    @Test
    public void アカウントセット削除テスト() throws Exception {

        DataSource ds = new DataSource();
        DBController dcon = new DBController(ds);

        String id = "testAccount";
        String pass = "testPass";
        String result;

        ArrayList<String> expected = new ArrayList<>();
        expected.add("testAccount");
        expected.add("testPass");

        int code = dcon.setAccount(id, pass);
        if (code != 0) {
            System.out.println("error code = " + code);
        } else {
            result = dcon.getPass(id).getKey();
            System.out.println(result);
            assertEquals(expected.get(1), result);
        }

        code = dcon.deleteAccount(id);

        SimpleEntry<String, Integer> result2 = dcon.getPass(id);
        assertEquals(null, result2.getKey());
        System.out.println(result2.getKey());
    }

    @Test
    public void スケジュールセット削除テスト() throws Exception {

        DataSource ds = new DataSource();
        DBController dcon = new DBController(ds);

        // セットする予定
        String id = "testAccount";
        String pass = "testPass";
        ArrayList<String> start = new ArrayList<>();
        start.add("2020-11-24 00:00:00");
        start.add("2020-11-25 12:30:00");
        start.add("2020-11-26 12:30:00");
        ArrayList<String> end = new ArrayList<>();
        end.add("2020-11-24 17:00:00");
        end.add("2020-11-25 16:00:00");
        end.add("2020-11-26 17:00:00");
        int saraly = 1000;

        // 期待する文字列
        ArrayList<String> expected1 = new ArrayList<>();
        expected1.add("2020-11-24 00:00:00,2020-11-24 17:00:00,1000");
        expected1.add("2020-11-25 12:30:00,2020-11-25 16:00:00,1000");
        expected1.add("2020-11-26 12:30:00,2020-11-26 17:00:00,1000");
        ArrayList<String> expected2 = new ArrayList<>();
        expected2.add("2020-11-24 00:00:00,2020-11-24 17:00:00,1000");
        expected2.add("2020-11-26 12:30:00,2020-11-26 17:00:00,1000");

        // 結果を入れる変数
        SimpleEntry<ArrayList<String>, Integer> result1;
        SimpleEntry<ArrayList<String>, Integer> result2;

        System.out.println(dcon.setAccount(id, pass)); // アカウントを作成
        System.out.println(dcon.setSchedule(id, start, end, saraly)); // 予定をセット

        result1 = dcon.getScheduleAtMonth(id, "2020-11-01"); // 予定をゲット
        System.out.println(result1.getKey());

        ArrayList<String> deleteIndex = new ArrayList<>();
        deleteIndex.add(result1.getKey().get(1).split(",")[0]); // 一個目の予定のs_idを取ってくる

        System.out.println(dcon.deleteSchedule(deleteIndex)); // 一個目の予定を削除

        result2 = dcon.getScheduleAtMonth(id, "2020-11-01"); // 予定をゲット
        System.out.println(result2.getKey());

        System.out.println(dcon.deleteAccount(id)); // アカウントを消す
        dcon.resetAutoincrement(); // AutoIncrementをリセット

        // 結果検証
        String[] lExpected;
        String[] lResult;

        // 3つの予定を追加できるかテスト
        for (int i = 0; i < 3; i++) {
            lExpected = expected1.get(i).split(",");
            lResult = result1.getKey().get(i).split(",");
            for (int l = 0; l < 3; l++) {
                assertEquals(lExpected[l], lResult[l + 1]); // Result側はインデックスがくっついてくるので2個目の要素から比較する
            }
        }

        // 追加した予定から一個消せてるかテスト
        for (int i = 0; i < 2; i++) {
            lExpected = expected2.get(i).split(",");
            lResult = result2.getKey().get(i).split(",");
            for (int l = 0; l < 3; l++) {
                assertEquals(lExpected[l], lResult[l + 1]);
            }
        }
    }

    @Test
    public void アカウントアップデートテスト() throws Exception {

        DataSource ds = new DataSource();
        DBController dcon = new DBController(ds);

        // 設定するアカウント
        String id = "testAccount";
        String pass = "testPass";
        SimpleEntry<String, Integer> result;

        // 更新するアカウント
        String updateId = "testAccount2";
        String updatePass = "testPass2";

        // 期待する文字列
        String expectedID = "testAccount2";
        String expectedPass = "testPass2";

        System.out.println(dcon.setAccount(id, pass));
        System.out.println(dcon.updateAccount(id, updateId, updatePass));

        result = dcon.getPass(expectedID);
        System.out.println(result.getKey());
        System.out.println(result.getValue());

        System.out.println(dcon.deleteAccount(expectedID));

        // 結果検証
        assertEquals(expectedPass, result.getKey());
    }

    @Test
    public void スケジュールアップデートテスト() throws Exception {

        DataSource ds = new DataSource();
        DBController dcon = new DBController(ds);

        // 設定するアカウント
        String id = "testAccount";
        String pass = "testPass";
        SimpleEntry<String, Integer> result;

        // 設定する予定
        ArrayList<String> start = new ArrayList<>();
        start.add("2020-11-24 00:00:00");
        ArrayList<String> end = new ArrayList<>();
        end.add("2020-11-24 17:00:00");
        int saraly = 1000;

        // 更新する予定
        SimpleEntry<String, String> updateTime = new SimpleEntry<>("2020-11-24 12:00:00", "2020-11-24 16:30:00");
        int updateSaraly = 980;

        // 期待する文字列
        String expected = "2020-11-24 12:00:00,2020-11-24 16:30:00,980";

        System.out.println(dcon.setAccount(id, pass));
        System.out.println(dcon.setSchedule(id, start, end, saraly));
        SimpleEntry<ArrayList<String>, Integer> getSchdule;
        getSchdule = dcon.getScheduleAtMonth(id, "2020-11-01");
        System.out.println(getSchdule.getKey());
        System.out.println(getSchdule.getValue());
        String getScheduleIndex = getSchdule.getKey().get(0).split(",")[0];
        System.out.println(dcon.updateSchedule(getScheduleIndex, updateTime, updateSaraly));

        result = dcon.getScheduleAtIndex(getScheduleIndex);
        System.out.println(result.getKey());
        System.out.println(result.getValue());
        expected = getScheduleIndex + "," + expected;

        System.out.println(dcon.deleteAccount(id));
        System.out.println(dcon.resetAutoincrement());

        // 結果検証
        assertEquals(expected, result.getKey());
    }

    @Test
    public void 複数スケジュールアップデートテスト() throws Exception {

        DataSource ds = new DataSource();
        DBController dcon = new DBController(ds);

        // 設定するアカウント
        String id = "testAccount";
        String pass = "testPass";

        // 設定する予定
        ArrayList<String> start = new ArrayList<>();
        start.add("2020-11-24 00:00:00");
        start.add("2020-11-25 12:30:00");
        start.add("2020-11-26 12:30:00");
        ArrayList<String> end = new ArrayList<>();
        end.add("2020-11-24 17:00:00");
        end.add("2020-11-25 16:00:00");
        end.add("2020-11-26 17:00:00");
        int saraly = 1000;

        // 更新する予定
        ArrayList<String> indexList = new ArrayList<>();
        ArrayList<String> updateStart = new ArrayList<>();
        updateStart.add("2020-11-25 00:00:00");
        updateStart.add("2020-11-26 12:30:00");
        updateStart.add("2020-11-27 12:30:00");
        ArrayList<String> updateEnd = new ArrayList<>();
        updateEnd.add("2020-11-25 17:00:00");
        updateEnd.add("2020-11-26 16:00:00");
        updateEnd.add("2020-11-27 17:00:00");
        ArrayList<Integer> updateSaraly = new ArrayList<>();
        updateSaraly.add(980);
        updateSaraly.add(980);
        updateSaraly.add(980);

        // 期待する文字列
        ArrayList<String> expected = new ArrayList<>();
        expected.add("2020-11-25 00:00:00,2020-11-25 17:00:00,980");
        expected.add("2020-11-26 12:30:00,2020-11-26 16:00:00,980");
        expected.add("2020-11-27 12:30:00,2020-11-27 17:00:00,980");

        System.out.println(dcon.setAccount(id, pass));// アカウント作る
        System.out.println(dcon.setSchedule(id, start, end, saraly));// スケジュール作る

        SimpleEntry<ArrayList<String>, Integer> result;
        result = dcon.getScheduleAtMonth(id, "2020-11-01"); // 設定した予定取ってくる。
        System.out.println(result.getKey());
        System.out.println(result.getValue());

        for (String s : result.getKey()) {
            indexList.add(s.split(",")[0]); // 更新するインデックス取ってくる
        }

        System.out.println(dcon.updateSchedule(indexList, updateStart, updateEnd, updateSaraly));// アップデートする

        result = dcon.getScheduleAtMonth(id, "2020-11-01"); // 更新した予定取ってくる。
        System.out.println(result.getKey());
        System.out.println(result.getValue());

        System.out.println(dcon.deleteAccount(id));// アカウント消す
        System.out.println(dcon.resetAutoincrement());// AutoIncrementリセット

        // 結果検証
        String[] lExpected;
        String[] lResult;
        // 追加した予定から一個消せてるかテスト
        for (int i = 0; i < 3; i++) {
            lExpected = expected.get(i).split(",");
            lResult = result.getKey().get(i).split(",");
            for (int l = 0; l < 3; l++) {
                assertEquals(lExpected[l], lResult[l + 1]);
            }
        }
    }

    @Test
    public void プロフィールアップデートテスト() throws Exception {

        DataSource ds = new DataSource();
        DBController dcon = new DBController(ds);

        // 設定するアカウント
        String id = "testAccount";
        String pass = "testPass";
        String name = "namae";
        SimpleEntry<String, Integer> result;

        // 更新するアカウント
        String expected = "namae2";


        System.out.println(dcon.setAccount(id, pass));
        System.out.println(dcon.setProfile(id, name));

        result = dcon.getName(id);
        System.out.println(result.getKey());
        System.out.println(result.getValue());

        System.out.println(dcon.updateProfile(id, expected));

        result = dcon.getName(id);
        System.out.println(result.getKey());
        System.out.println(result.getValue());

        System.out.println(dcon.deleteAccount(id));

        // 結果検証
        assertEquals(expected, result.getKey());
    }

}