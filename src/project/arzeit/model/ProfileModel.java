package project.arzeit.model;

import java.util.AbstractMap.SimpleEntry;

import project.arzeit.database.DBController;
import project.arzeit.database.DataSource;

/**
 * プロフィールを更新したり設定したりするモデル
 * いまのところニックネームだけ
 * @author Minoru Makino
 */
public class ProfileModel {

    private DBController db;

    public ProfileModel(DataSource ds) {
        db = new DBController(ds);
    }

    public int setName(String id, String name) {
        return db.updateProfile(id, name);
    }

    public SimpleEntry<String, Integer> getName(String id) {
        return db.getName(id);
    }

}