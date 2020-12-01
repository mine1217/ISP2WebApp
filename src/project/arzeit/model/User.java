package project.arzeit.model;

/**
 * Userのモデル セッションスコープにぶちこむ
 * 後の拡張を考えて新しくクラス作った
 * ゲッターとセッターだけ
 */
public class User {
    private String id;
    private String name;
    
    public User (String id) {
        this.id = id; //名前は後からでもいい。
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
