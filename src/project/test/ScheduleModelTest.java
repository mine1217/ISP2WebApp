package project.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

import org.junit.*;
import org.json.*;

import project.arzeit.database.DBController;
import project.arzeit.database.DataSource;

import project.arzeit.model.ScheduleModel;



/**
 * ScheduleModelの単体？テスト　
 * アップデートとかはまんま委譲してるので、スケジュールを取ってきてJSON形式にするテストだけ

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

        ArrayList<String> jsontext;
        JSONArray ja = new JSONArray();

        SimpleEntry<ArrayList<String>, Integer> result = model.getScheduleAtMonth(id, "2020-11-01");
        if(result.getValue() == 0) {
            jsontext = model.scheduleToJSON(result.getKey());
            for(String s : jsontext) {
                ja.put(new JSONObject(s));
            }
        } else {
            System.out.println(result.getValue());
        }

        ArrayList<String> delete = new ArrayList<>();
        delete.add("1");
        System.out.println(model.deleteSchedule(delete));
        System.out.println(model.updateSchedule("2", "2020-11-12 20:00:00", "2020-11-12 20:00:00", "980"));
        
        dcon.deleteAccount(id);
        dcon.resetAutoincrement();

        System.out.println(ja.toString());

        for(int i = 0; i < ja.length(); i++) {
            assertEquals(start.get(i), ja.getJSONObject(i).getString("start"));
            assertEquals(end.get(i), ja.getJSONObject(i).getString("end"));
            assertEquals(saraly, ja.getJSONObject(i).getString("saraly"));
        }
    }

}