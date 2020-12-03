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
        int code = checkDuplicate(id); //重複チェック

        if (code == 0) return db.setAccount(id, pass);

        return code;
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

    /**
     * idを更新する
     * @param id
     * @param updateId
     * @return ステータスコード
     */
    public int setId(String id, String updateId) {
        int code = checkDuplicate(id); //重複チェック
        if(code == 0) code = db.updateId(id, updateId);
        
        return code;
    }

    /**
     * id重複検査
     * @param id
     * @return エラーコード　被ってたら 2返す
     */
    public int checkDuplicate(String id) {
        return db.checkDuplicate(id);
    }

    /**
     * passを更新する
     * 前のパスを認証してから実行するように修正必要
     * @param id
     * @param updateId
     * @return ステータスコード
     */
    public int setPass(String id, String pass) {
        return db.setPass(id, pass);
    }

}