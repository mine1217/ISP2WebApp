package project.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.*;

import project.arzeit.database.DBController;
import project.arzeit.database.DataSource;
import project.arzeit.model.AuthCModel;

/**
 * AuthCModel-認証処理をするプログラム の単体テスト
 * 
 * @author Minoru Makino
 */
public class ScheduleModelTest {
    @Test
    public void スケジュール取得テスト_JSON() throws Exception {
        DataSource ds = new DataSource();
        DBController dcon = new DBController(ds);
        ScheduleModel model = new ScheduleModel(ds);

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
        String saraly = "1000";

        System.out.println(dcon.setAccount(id, pass)); // アカウントを作成
        System.out.println(dcon.setSchedule(id, start, end, saraly)); // 予定をセット

        

    }

}