package project.arzeit.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

/**
 * HikariCPを使ったデータプールのソース DB接続の根っこ
 * 
 * @author Minoru Makino
 */
public class DBController {

    private DataSource dataSource; // SQLのコネクタ

    /**
     * コネクタをServletContxtからもらってくる
     * 
     * @param ds SQLのコネクタ(DataSource)
     */
    public DBController(DataSource ds) {
        this.dataSource = ds;
    }

    /**
     * 例外処理めんどくさいので検索以外の命令はここにぶん投げる
     * 
     * @param message 検索以外の命令
     * @return エラーコード
     */
    public int update(String message) {

        Connection con = null; // SQLのコネクタ
        PreparedStatement pstm = null;
        int code = 0; //エラーコード入る

        try {

            con = dataSource.getConnection(); // コネクションをプールから取ってくる

            pstm = con.prepareStatement(message); // 引数から命令のステートメント生成
            pstm.executeUpdate(); // 送信

        } catch (SQLException e) { 

            code = e.getErrorCode(); // エラーコード取ってくる
            System.out.println(e);

        } finally {

            try { // 後処理
                pstm.close();
                if (con != null) con.close(); //nullになる時があるので
            } catch (SQLException e) {
                code = e.getErrorCode(); //エラーコード上書き
            }

        }

        return code; 
    }

    /**
     * INSERT,DELETE,UPDATEを複数文行う場合に使う
     * @param messageList メッセージのリスト
     * @return エラーコード
     */
    public int update(ArrayList<String> messageList) {

        Connection con = null; // SQLのコネクタ
        Statement stmt = null;
        int code = 0; //エラーコード入る

        try {

            con = dataSource.getConnection(); // コネクションをプールから取ってくる

            stmt = con.createStatement();
            for(String s : messageList) {
                stmt.addBatch(s);
            }
            stmt.executeBatch();

        } catch (SQLException e) { 

            code = e.getErrorCode(); // エラーコード取ってくる
            System.out.println(e);

        } finally {

            try { // 後処理
                stmt.close();
                if (con != null) con.close(); //nullになる時があるので
            } catch (SQLException e) {
                code = e.getErrorCode(); //エラーコード上書き
            }

        }

        return code; 
    }

    /**
     * アカウントを登録する
     * 
     * @param id
     * @param pass
     * @return :int エラーコード
     */
    public int setAccount(String id, String pass) {
        String message = String.format("INSERT INTO login VALUES ('%s', '%s')", id, pass);
        int code = update(message);
        return code;
    }
    
    /**
     * プロフィールを登録する
     * 
     * @param id
     * @param name
     * @return :int エラーコード
     */
    public int setProfile(String id, String name) {
        String message = String.format("INSERT INTO profile VALUES ('%s', '%s')", id, name);
        int code = update(message);
        return code;
    }

    /**
     * パスワードを変更する
     * 
     * @param id
     * @param pass
     * @return :int エラーコード
     */
    public int setPass(String id, String pass) {
        String message = String.format("UPDATE login set pass='%s' WHERE id='%s'", pass, id);
        int code = update(message);
        return code;
    }

    /**
     * 予定の設定
     * @param id 
     * @param time :HashMap<開始時間: String, 終了時間: String> 一個でもいいし複数でもいい
     * @param saraly 給料
     * @return エラーコード
     */
    public int setSchedule(String id, ArrayList<String> start, ArrayList<String> end, int saraly) {
        StringBuilder sBuilder = new StringBuilder("INSERT INTO schedule VALUES");

        for (int i = 0; i < start.size(); i++) {
            sBuilder.append(" ('").append(id).append("', ")
            .append("NULL, '")
            .append(start.get(i)).append("', '")
            .append(end.get(i)).append("', ")
            .append(saraly).append("),");
        }

        sBuilder.setLength(sBuilder.length() - 1); //最後にカンマを消す

        int code = update(sBuilder.toString());//命令送る
        return code;
    }

    /**
     * scheduleのs_idのAUTOINCREMENTを欠番を詰めて最大値にリセットする
     * @return エラーコード
     */
    public int resetAutoincrement() {
        String message = "CALL reset_schedule_autoincrement()";
        int code = update(message);
        return code;
    }

    /**
     * indexで予定を消す
     * @param s_idList indexのリスト
     * @return エラーコード
     */
    public int deleteSchedule(ArrayList<String> s_idList) {
        StringBuilder sBuilder = new StringBuilder("DELETE FROM schedule WHERE s_id IN (");

        for (String s : s_idList) {
            sBuilder.append(" ");
            sBuilder.append(s); //複数予定があればどんどん追加
            sBuilder.append(",");
        }

        sBuilder.setLength(sBuilder.length() - 1); //最後にカンマを消す
        sBuilder.append(");");

        int code = update(sBuilder.toString());//命令送る
        return code;
    }

    /**
     * id指定でアカウントを消す
     * @param id 
     * @return エラーコード
     */
    public int deleteAccount(String id) {
        ArrayList<String> messageList = new ArrayList<>();

        messageList.add("DELETE from profile WHERE id='" + id + "'; "); //プロフィールとスケジュール消してから
        messageList.add("DELETE from schedule WHERE id='" + id + "'; ");
        messageList.add("DELETE from login WHERE id='" + id + "'; "); //アカウントを消さないと外部キーのエラーが出る

        int code = update(messageList);//命令送る
        return code;
    }

    /**
     * ID指定してアカウント情報を更新する
     * @param id 更新元id
     * @param updateId 更新語id
     * @param updatePass 更新後パス
     * @return エラーコード
     */
    public int updateAccount(String id, String updateId, String updatePass) {
        
        StringBuilder sBuilder = new StringBuilder("UPDATE login SET id = '");
        sBuilder.append(updateId)
        .append("', pass= '").append(updatePass)
        .append("' WHERE id = '").append(id)
        .append("';");

        int code = update(sBuilder.toString());//命令送る
        return code;
    }

    /**
     * インデックス指定でスケジュールを更新する(一個)
     * @param s_id 更新するindex
     * @param updateTime 開始、終了時間のマップ
     * @param updateSaraly 更新する給料
     * @return
     */
    public int updateSchedule(String s_id, String start, String end,  int updateSaraly) {
        StringBuilder sBuilder = new StringBuilder("UPDATE schedule SET start = '");

        sBuilder.append(start)
        .append("', end= '").append(end)
        .append("', saraly= '").append(updateSaraly)
        .append("' WHERE s_id = '").append(s_id)
        .append("';");

        int code = update(sBuilder.toString());//命令送る
        return code;
    }
    
    /**
     * インデックス指定でスケジュールを更新する(複数)
     * @param index 更新するindex
     * @param updateTime 開始、終了時間のマップ
     * @param updateSaraly 更新する給料
     * @return エラーコード
     */
    public int updateSchedule(ArrayList<String> s_idList, ArrayList<String> start, ArrayList<String> end, ArrayList<Integer> saraly) {
        StringBuilder sBuilder = new StringBuilder("UPDATE schedule SET start = CASE");

        if(!(s_idList.size() == start.size() &&  s_idList.size() == saraly.size())) return -1; //sizeが違っていたら-1返して強制終了

        for (int i = 0; i < s_idList.size(); i++) { //開始時間の設定
            sBuilder.append(" WHEN s_id = ").append(s_idList.get(i))
            .append(" THEN '").append(start.get(i)).append("'");
        }

        sBuilder.append(" END, end = CASE");

        for (int i = 0; i < s_idList.size(); i++) { //終了時間の設定
            sBuilder.append(" WHEN s_id = ").append(s_idList.get(i))
            .append(" THEN '").append(end.get(i)).append("'");
        }

        sBuilder.append(" END, saraly = CASE");

        for (int i = 0; i < s_idList.size(); i++) { //給料の設定
            sBuilder.append(" WHEN s_id = ").append(s_idList.get(i))
            .append(" THEN '").append(saraly.get(i)).append("'");
        }

        sBuilder.append(" END;");

        int code = update(sBuilder.toString());//命令送る
        return code;
    }
    
    /**
     * プロフィールを更新する
     * @param id
     * @param name
     * @return エラーコード
     */
    public int updateProfile(String id, String name) {
        
        StringBuilder sBuilder = new StringBuilder("UPDATE profile SET name = '");
        sBuilder.append(name)
        .append("' WHERE id = '").append(id)
        .append("';");

        int code = update(sBuilder.toString());//命令送る
        return code;
        
    }

    /**
     * パスワード取ってくる
     * @param id
     * @return 結果とエラーコードのマップ
     */
    public SimpleEntry<String, Integer> getPass(String id) {

        Connection con = null; // SQLのコネクタ
        PreparedStatement pstm = null;
        String pass = null;
        int code = 0;

        try {

            con = dataSource.getConnection(); // コネクションをプールから取ってくる
            pstm = con.prepareStatement(String.format("SELECT pass FROM login WHERE id='%s'", id)); //id指定でパスを取ってくる
            ResultSet result = pstm.executeQuery(); // 送信
            if(result.next()) pass = result.getString("pass");
            else code = 1;
            
        } catch (SQLException e) {

            System.out.println(e);
            code = e.getErrorCode(); // エラーコード取ってくる

        } finally {

            try { // 後処理
                pstm.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                code = e.getErrorCode();
            }

        }

        return new SimpleEntry<String, Integer>(pass, code);

    }

    /**
     * 名前取ってくる
     * @param id
     * @return 結果とエラーコードのマップ
     */
    public SimpleEntry<String, Integer> getName(String id) {

        Connection con = null; // SQLのコネクタ
        PreparedStatement pstm = null;
        String name = null;
        int code = 0;

        try {

            con = dataSource.getConnection(); // コネクションをプールから取ってくる
            pstm = con.prepareStatement(String.format("SELECT name FROM profile WHERE id='%s'", id)); //id指定でパスを取ってくる
            ResultSet result = pstm.executeQuery(); // 送信
            result.next();
            name = result.getString("name");
            
        } catch (SQLException e) {

            System.out.println(e);
            code = e.getErrorCode(); // エラーコード取ってくる

        } finally {

            try { // 後処理
                pstm.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                code = e.getErrorCode();
            }

        }

        return new SimpleEntry<String, Integer>(name, code);
        
    }

    /**
     * 月指定で予定取ってくる
     * @param id
     * @return 結果とエラーコードのマップ
     */
    public SimpleEntry<ArrayList<String>, Integer> getScheduleAtMonth(String id, String month) {

        Connection con = null; // SQLのコネクタ
        PreparedStatement pstm = null;
        ArrayList<String> schedules = new ArrayList<>();
        int code = 0;

        try {

            con = dataSource.getConnection(); // コネクションをプールから取ってくる
            pstm = con.prepareStatement
                (String.format("SELECT s_id, start, end, saraly FROM schedule WHERE id='%s' AND start BETWEEN '%s' AND (SELECT LAST_DAY('%s')) ORDER BY start;" , id, month, month)); //id,月指定で予定一式を取ってくる
            ResultSet result = pstm.executeQuery(); // 送信
            
            while(result.next()) {//複数行帰ってくる可能性があるのでぐるぐる回す
                schedules.add(result.getString("s_id")+","+result.getString("start")+","+result.getString("end")+","+result.getString("saraly")); //今のところcsv形式でStringにして返す
            }
            
        } catch (SQLException e) {

            System.out.println(e);
            code = e.getErrorCode(); // エラーコード取ってくる

        } finally {

            try { // 後処理
                pstm.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                code = e.getErrorCode();
            }

        }

        return new SimpleEntry<>(schedules, code);
        
    }

    /**
     * indexで予定取ってくる
     * @param index
     * @return 結果とエラーコードのマップ
     */
    public SimpleEntry<String, Integer> getScheduleAtIndex(String index) {

        Connection con = null; // SQLのコネクタ
        PreparedStatement pstm = null;
        String schedule = null;
        int code = 0;

        try {

            con = dataSource.getConnection(); // コネクションをプールから取ってくる
            pstm = con.prepareStatement
                (String.format("SELECT s_id, start, end, saraly FROM schedule WHERE s_id=%s;" , index)); //id,月指定で予定一式を取ってくる
            ResultSet result = pstm.executeQuery(); // 送信
            
            result.next();
            schedule = result.getString("s_id")+","+result.getString("start")+","+result.getString("end")+","+result.getString("saraly"); //今のところcsv形式でStringにして返す
            
            
        } catch (SQLException e) {

            System.out.println(e);
            code = e.getErrorCode(); // エラーコード取ってくる

        } finally {

            try { // 後処理
                pstm.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                code = e.getErrorCode();
            }

        }

        return new SimpleEntry<>(schedule, code);
        
    }

    /**
     * idの重複を見る
     * @param index
     * @return 0で無し　1で重複あり
     */
    public int checkDuplicate(String id) {

        Connection con = null; // SQLのコネクタ
        PreparedStatement pstm = null;
        int code = 0;

        try {

            con = dataSource.getConnection(); // コネクションをプールから取ってくる
            pstm = con.prepareStatement
                (String.format("SELECT * FROM login WHERE id='%s'" , id)); //id,月指定で予定一式を取ってくる
            ResultSet result = pstm.executeQuery(); // 送信
 
            if(result.next()) code = 1; //もし次があれば重複のステータスコードを返すようにする

        } catch (SQLException e) {

            System.out.println(e);
            code = e.getErrorCode(); // エラーコード取ってくる

        } finally {
            
            try { // 後処理
                pstm.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                code = e.getErrorCode();
            }

        }

        return code;
        
    }

}
