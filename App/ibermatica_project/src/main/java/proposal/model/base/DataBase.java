package proposal.model.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import proposal.model.Machine;
import proposal.model.Reservation;
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

    public int addNewUser(User user) {
        String sql = "INSERT INTO users(user_id, name, surname, email, tlf_num, username, password, register_date, type) "+
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getSurname());
            pstmt.setString(4, user.getEmail());
            pstmt.setInt(5, user.getTlfNum());
            pstmt.setString(6, user.getUsername());
            pstmt.setString(7, user.getPassword());
            pstmt.setTimestamp(8, Timestamp.valueOf(user.getRegisterdate().atStartOfDay()));
            pstmt.setInt(9, user.getType());

            int rowUpdated = pstmt.executeUpdate();
            if (rowUpdated > 0) {
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public User searchSpecificUser(String data) {
        User user;
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, data);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String userId = rs.getString("user_id"), name = rs.getString("name"),
                        surname = rs.getString("surname"), email = rs.getString("email");
                int tlfNumber = rs.getInt("tlf_num");
                String username = rs.getString("username"), password = rs.getString("password");
                LocalDate registerDate = rs.getTimestamp("register_date").toLocalDateTime().toLocalDate();
                int type = rs.getInt("type");

                user = new User(userId, name, surname, email, tlfNumber, username, password, registerDate, type);
                
                return user;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public int updateUser(User user) {
        String sql = "UPDATE users SET user_id = ?, name = ?, surname = ?, email = ?, tlf_num = ?, username = ?, type = ? WHERE user_id = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getSurname());
            pstmt.setString(4, user.getEmail());
            pstmt.setInt(5, user.getTlfNum());
            pstmt.setString(6, user.getUsername());
            pstmt.setInt(7, user.getType());
            pstmt.setString(8, user.getUserId());
            int rowUpdated = pstmt.executeUpdate();
            if (rowUpdated > 0) {
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public ArrayList<Reservation> searchAllReservations() {
        ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
        String sql = "SELECT * FROM reservation_machines";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString("user_id"), serialNumber = rs.getString("serial_num");
                LocalDate starDate = rs.getTimestamp("start_date").toLocalDateTime().toLocalDate();
                LocalDate endDate = rs.getTimestamp("end_date").toLocalDateTime().toLocalDate();
                int reservationId = rs.getInt("reservation_id");

                Reservation reservation = new Reservation(userId, serialNumber, starDate, endDate, reservationId);
                reservationList.add(reservation);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reservationList;
    }

    public Reservation searchSpecificReservation(int data) {
        Reservation reservation;
        String sql = "SELECT * FROM reservation_machines WHERE reservation_id = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setInt(1, data);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String userId = rs.getString("user_id");
                String serialNumber = rs.getString("serial_num");
                LocalDate startDate = rs.getTimestamp("start_date").toLocalDateTime().toLocalDate();
                LocalDate endDate = rs.getTimestamp("end_date").toLocalDateTime().toLocalDate();
                int reservation_id = rs.getInt("reservation_id");

                reservation = new Reservation(userId, serialNumber, startDate, endDate, reservation_id);
            
                return reservation;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public int updateReservation(Reservation reservation) {
        String sql = "UPDATE reservation_machines SET user_id = ?, serial_num = ?, start_date = ?, end_date = ? WHERE reservation_id = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, reservation.getUserId());
            pstmt.setString(2, reservation.getSerialNumber());
            pstmt.setTimestamp(3, Timestamp.valueOf(reservation.getStartDate().atStartOfDay()));
            pstmt.setTimestamp(4, Timestamp.valueOf(reservation.getEndDate().atStartOfDay()));
            pstmt.setInt(5, reservation.getReservationId());
            int rowUpdated = pstmt.executeUpdate();
            if (rowUpdated > 0) {
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public ArrayList<Machine> searchAllMachines() {
        ArrayList<Machine> machineList = new ArrayList<Machine>();
        String sql = "SELECT * FROM machines";  
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            ResultSet rs = pstmt.executeQuery();
            String serialNumber = rs.getString("serial_num"), name = rs.getString("name");
            LocalDate adquisitionDate = rs.getTimestamp("adquisition_date").toLocalDateTime().toLocalDate();
            String type = rs.getString("type"), status = rs.getString("status");

            Machine machine = new Machine(serialNumber, name, adquisitionDate, type, status);
            machineList.add(machine);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return machineList;
    }
}


