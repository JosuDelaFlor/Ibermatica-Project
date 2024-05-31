package ibermatica_project.model;

public class SimpleUser {
    String userId, name, surname, email;
    int tlfNum;
    String username;
    int type;

    public SimpleUser(String userId, String name, String surname, String email, int tlfNum, String username, int type) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.tlfNum = tlfNum;
        this.username = username;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public int getTlfNum() {
        return tlfNum;
    }

    public String getUsername() {
        return username;
    }

    public int getType() {
        return type;
    }
}
