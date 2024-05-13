package proposal.model;

import java.time.LocalDate;

public class User {
    String userId, name, surname, email;
    int tlfNum;
    String username, password;
    LocalDate registerdate;
    int type;

    public User(String userId, String name, String surname, String email, int tlfNum, String username, String password,
            LocalDate registerdate, int type) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.tlfNum = tlfNum;
        this.username = username;
        this.password = password;
        this.registerdate = registerdate;
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

    public String getPassword() {
        return password;
    }

    public LocalDate getRegisterdate() {
        return registerdate;
    }

    public int getType() {
        return type;
    }
}
