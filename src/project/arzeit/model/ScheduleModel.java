package project.arzeit.model;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import project.arzeit.database.DBController;
import project.arzeit.database.DataSource;

/**
 * スケジュールの登録更新をするモデル
 * @author Minoru Makino
 */
public class ScheduleModel {

    private DBController db;
    public final String update = "update",
                        delete = "delete";

    public ScheduleModel(DataSource ds) {
        db = new DBController(ds);
    }

    /**
     * そんままかえすだけ　今のところ
     * @param id
     * @param month
     * @return ステータスコードとスケジュールのリスト(csv形式)
     */
    public SimpleEntry<ArrayList<String>, Integer> getScheduleAtMonth(String id, String month) {
        return db.getScheduleAtMonth(id, month);
    }

    /**
     * そんままかえすだけ　今のところ
     * @param s_id インデックス
     * @return ステータスコードとスケジュールのリスト(csv形式)
     */
    public SimpleEntry<String, Integer> getScheduleAtIndex(String s_id) {
        return db.getScheduleAtIndex(s_id);
    }

    /**
     * スケジュール一個をアップデートする
     * @param s_id
     * @param start
     * @param end
     * @param saraly
     * @return　ステータスコード
     */
    public int updateSchedule (String s_id, String start, String end, String saraly) {
        return db.updateSchedule(s_id, start, end, saraly);
    }

        /**
     * スケジュール複数をアップデートする
     * @param s_id
     * @param start
     * @param end
     * @param saraly
     * @return　ステータスコード
     */
    public int updateSchedule (ArrayList<String> s_id, ArrayList<String> start, ArrayList<String> end, ArrayList<String> saraly) {
        return db.updateSchedule(s_id, start, end, saraly);
    }

    /**
     * スケジュール消すだけ
     * @param s_idList 消したいリスト
     * @return 
     */
    public int deleteSchedule (ArrayList<String> s_idList) {
        return db.deleteSchedule(s_idList);
    }


    /**
     * スケジュールをセットする
     * @param id
     * @param start リスト
     * @param end リスト
     * @param saraly
     * @return エラーコード
     */
    public int setSchedule (String id, ArrayList<String> start, ArrayList<String> end, String saraly) {
        return db.setSchedule(id, start, end, saraly);
    }


    /**
     * DBから取り立てほやほやのscheduleの文字列(csv形式)をjson形式にして返す
     * @param scheduleList
     * @return jsonに整形した文字列リスト
     */
    public ArrayList<String> scheduleToJSON(ArrayList<String> scheduleList) {
        ArrayList<String> jsonList = new ArrayList<>();
        StringBuilder sb = new StringBuilder("");

        String[] slist;
        for(String s: scheduleList) {
            slist = s.split(",");
            sb.append("{\"s_id\": \"").append(slist[0])
            .append("\", \"start\": \"").append(slist[1])
            .append("\", \"end\": \"").append(slist[2])
            .append("\", \"saraly\": \"").append(slist[3])
            .append("\"}");
            jsonList.add(sb.toString());
            sb.setLength(0); //sbクリア
        }

        return jsonList;
    }

}