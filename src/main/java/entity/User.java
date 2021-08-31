package entity;

public class User {

    private int id;
    private String username;
    private String password;
    private String level;
    private String create_date;

    public User() {
    }

    public User(int id, String username, String password, String level, String create_date) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.level = level;
        this.create_date = create_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

}
