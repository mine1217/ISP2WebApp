package project.arzeit.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    public int register(String id, String pass, String name) {
        int code = checkDuplicate(id); //重複チェック

        if (code == 0) {
            if((code = db.setAccount(id, toEncryptedHashValue(pass))) == 0) code = db.setProfile(id);
            if(code == 0) code = db.updateProfile(id, name);
        } 

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
        String expected = result.getKey(); 
        String actual = toEncryptedHashValue(pass);

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
        int code = checkDuplicate(updateId); //重複チェック
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
        return db.updatePass(id, toEncryptedHashValue(pass));
    }

    /**
     * ソルトを実装する SHA-256
     * @param pass
     * @return ハッシュ値
     */
    private String toEncryptedHashValue(String pass) {
        MessageDigest md = null;
        StringBuilder sb = null;
        String value = "unnnunnkannnunnnanntokakanntoka" + pass;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(value.getBytes());
        sb = new StringBuilder();
        for (byte b : md.digest()) {
            String hex = String.format("%02x", b);
            sb.append(hex);
        }
        return sb.toString();
    }

}