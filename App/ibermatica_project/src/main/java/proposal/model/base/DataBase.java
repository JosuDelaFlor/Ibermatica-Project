package proposal.model.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import proposal.model.Role;

public class DataBase {
    @SuppressWarnings("unused")
    private String server = "localhost", db = "ibermatica_db",  table = null, user = "root", pass = null;

    public DataBase(String server, String db, String table, String user, String pass) {
        this.server = server;
        this.db = db;
        this.table = table;
        this.user = user;
        this.pass = pass;
    }

    public Connection connect() {
        String url = "jdbc:mariadb://" + server + "/" + db;
        Connection conection = null;
        try {
            conection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            System.out.println(e.getErrorCode() + "-" + e.getMessage());
        }
        return conection;
    }

    /**
     * Searches all the users in the database and saves them 
     * along with their information in an arraylist
     * @return
     */

    public ArrayList<User> searchAllUsers() {
        ArrayList<User> userList = new ArrayList<User>();
        String sql = "SELECT * FROM users";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString("user_id"), name = rs.getString("name"),
                        surname = rs.getString("surname"), email = rs.getString("email");
                int tlfNumber = rs.getInt("tlf_num");
                String username = rs.getString("username"), password = rs.getString("password");
                LocalDate registerDate = rs.getTimestamp("register_date").toLocalDateTime().toLocalDate();
                int type = rs.getInt("type");

                User user = new User(userId, name, surname, email, tlfNumber, username, password, registerDate, type);
                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return userList;
    }

    /**
     * Searches all the roles in the database and saves them 
     * along with their information in an list
     * @return
     */

    public List<Role> searchAllRoles() {
        List<Role> roleList = new ArrayList<Role>();
        String sql = "SELECT * FROM role";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int roleId = rs.getInt("role_id");
                String name = rs.getString("name");

                Role role = new Role(roleId, name);
                roleList.add(role);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return roleList;
    }
}


