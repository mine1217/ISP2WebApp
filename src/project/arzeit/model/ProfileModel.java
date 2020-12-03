package project.arzeit.model;

import java.util.AbstractMap.SimpleEntry;

import project.arzeit.database.DBController;
import project.arzeit.database.DataSource;

/**
 * 認証とか登録処理を行うプログラム モデル
 * @author Minoru Makino
 */
public class ProfileModel {

    private DBController db;

    public ProfileModel(DataSource ds) {
        db = new DBController(ds);
    }
}