package project.arzeit.model;

import java.util.AbstractMap.SimpleEntry;

import project.arzeit.database.DBController;
import project.arzeit.database.DataSource;

/**
 * 認証とか登録処理を行うプログラム モデル
 * @author Minoru Makino
 */
public class AuthCModel {

    private DBController db;

    public AuthCModel(DataSource ds) {
        db = new DBController(ds);
    }

    /**
     * 登録処理
     * @param id
     * @param pass
     * @return 今のところエラーは直で書いててそれ帰ってくる 2:id重複
     */
    public int register(String id, String pass) {
        int code = db.checkDuplicate(id); //重複チェック　合わなかったら2帰ってくる

        if (code == 0) {
            return db.setAccount(id, pass);
        } else {
            return code;
        }
    }

    /**
     * ログイン処理
     * @param id
     * @param pass
     * @return ステータスコード 10:パスワード合わない 3:idが存在しない
     */
    public int login(String id, String pass) {
        SimpleEntry<String, Integer> result = db.getPass(id);
        String expected = result.getKey(); // 後でハッシュ化する予定なのでそれを入れる変数
        String actual = pass;

        if (result.getValue() == 0) { //エラーコードが0だったら成功
            if (expected != null) { //EmptySetが帰ってきた場合はnullが入ってる
                if (expected.equals(actual)) return 0;
                else return 10; //後で修正
            } else { //nullだった場合は3返す
                return 3; //後で修正
            }
        } else {
            return result.getValue(); 
        }
    }

}