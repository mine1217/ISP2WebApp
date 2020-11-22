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
        int code = 0;

        try {

            con = dataSource.getConnection(); // コネクションをプールから取ってくる

            PreparedStatement pstm = con.prepareStatement(message); // 引数から命令のステートメント生成
            pstm.executeUpdate(); // 送信

        } catch (SQLException e) {

            code = e.getErrorCode(); // エラーコード取ってくる

        } finally {

            try { // 後処理
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                code = e.getErrorCode();
            }

            return code;

        }
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
    public int setSchedule(String id, HashMap<String, String> time, int saraly) {
        StringBuilder sBuilder = new StringBuilder("INSERT INTO schedule VALUES");

        for (Entry<String, String> entry : time.entrySet()) {
            sBuilder.append(String.format(" (%s, %s, %s, %d),",id ,entry.getKey() ,entry.getValue(), saraly)); //複数予定があればどんどん追加
        }

        sBuilder.setLength(sBuilder.length() - 1); //最後にカンマを消す

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
        ResultSet result = null;
        String pass = null;
        int code = 0;

        try {

            con = dataSource.getConnection(); // コネクションをプールから取ってくる
            PreparedStatement pstm = con.prepareStatement(String.format("SELECT pass FROM login WHERE id='%s'", id)); //id指定でパスを取ってくる
            result = pstm.executeQuery(); // 送信
            result.next();
            pass = result.getString("pass");
            
        } catch (SQLException e) {

            System.out.println(e);
            code = e.getErrorCode(); // エラーコード取ってくる

        } finally {

            try { // 後処理
                if (con != null) con.close();
                if (result != null)result.close();
            } catch (SQLException e) {
                code = e.getErrorCode();
            }

            return new SimpleEntry<String, Integer>(pass, code);

        }
    }

}
