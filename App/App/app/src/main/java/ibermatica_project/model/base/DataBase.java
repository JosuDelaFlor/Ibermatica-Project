package ibermatica_project.model.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ibermatica_project.model.Machine;
import ibermatica_project.model.Reservation;
import ibermatica_project.model.Role;
import ibermatica_project.model.SimpleUser;

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

    public ArrayList<Reservation> searchAllReservationWithId(int data) {
        ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
        String sql = "SELECT * FROM reservation_machines WHERE reservation_id = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setInt(1, data);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString("user_id");
                String serialNumber = rs.getString("serial_num");
                LocalDate startDate = rs.getTimestamp("start_date").toLocalDateTime().toLocalDate();
                LocalDate endDate = rs.getTimestamp("end_date").toLocalDateTime().toLocalDate();
                int reservation_id = rs.getInt("reservation_id");

                Reservation reservation = new Reservation(userId, serialNumber, startDate, endDate, reservation_id);
                reservationList.add(reservation);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reservationList;
    }

    public Reservation searchSpecificReservationWithId(int reservationId) {
        String sql = "SELECT * FROM reservation_machines WHERE reservation_id = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setInt(1, reservationId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString("user_id");
                String serialNumber = rs.getString("serial_num");
                LocalDate startDate = rs.getTimestamp("start_date").toLocalDateTime().toLocalDate();
                LocalDate endDate = rs.getTimestamp("end_date").toLocalDateTime().toLocalDate();
                int reservation_id = rs.getInt("reservation_id");

                Reservation reservation = new Reservation(userId, serialNumber, startDate, endDate, reservation_id);
                return reservation;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Reservation> searchSpecificReservationWithUserId(String data) {
        ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
        String sql = "SELECT * FROM reservation_machines WHERE user_id = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, data);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString("user_id");
                String serialNumber = rs.getString("serial_num");
                LocalDate startDate = rs.getTimestamp("start_date").toLocalDateTime().toLocalDate();
                LocalDate endDate = rs.getTimestamp("end_date").toLocalDateTime().toLocalDate();
                int reservation_id = rs.getInt("reservation_id");

                Reservation reservation = new Reservation(userId, serialNumber, startDate, endDate, reservation_id);
                reservationList.add(reservation);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reservationList;
    }

    public ArrayList<Reservation> searchSpecificReservationWithSerialNumer(String data) {
        ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
        String sql = "SELECT * FROM reservation_machines WHERE serial_num = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, data);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString("user_id");
                String serialNumber = rs.getString("serial_num");
                LocalDate startDate = rs.getTimestamp("start_date").toLocalDateTime().toLocalDate();
                LocalDate endDate = rs.getTimestamp("end_date").toLocalDateTime().toLocalDate();
                int reservation_id = rs.getInt("reservation_id");

                Reservation reservation = new Reservation(userId, serialNumber, startDate, endDate, reservation_id);
                reservationList.add(reservation);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reservationList;
    }

    public ArrayList<Reservation> searchSpecificReservationWithStartDate(String data) {
        ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
        String sql = "SELECT * FROM reservation_machines WHERE start_date = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            LocalDate startDateL = LocalDate.parse(data);
            pstmt.setTimestamp(1, Timestamp.valueOf(startDateL.atStartOfDay()));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString("user_id");
                String serialNumber = rs.getString("serial_num");
                LocalDate startDate = rs.getTimestamp("start_date").toLocalDateTime().toLocalDate();
                LocalDate endDate = rs.getTimestamp("end_date").toLocalDateTime().toLocalDate();
                int reservation_id = rs.getInt("reservation_id");

                Reservation reservation = new Reservation(userId, serialNumber, startDate, endDate, reservation_id);
                reservationList.add(reservation);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reservationList;
    }

    public ArrayList<Reservation> searchSpecificReservationWithEndDate(String data) {
        ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
        String sql = "SELECT * FROM reservation_machines WHERE end_date = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            LocalDate endDateL = LocalDate.parse(data);
            pstmt.setTimestamp(1, Timestamp.valueOf(endDateL.atStartOfDay()));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString("user_id");
                String serialNumber = rs.getString("serial_num");
                LocalDate startDate = rs.getTimestamp("start_date").toLocalDateTime().toLocalDate();
                LocalDate endDate = rs.getTimestamp("end_date").toLocalDateTime().toLocalDate();
                int reservation_id = rs.getInt("reservation_id");

                Reservation reservation = new Reservation(userId, serialNumber, startDate, endDate, reservation_id);
                reservationList.add(reservation);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reservationList;
    }

    public boolean updateReservation(Reservation reservation) {
        String sql = "UPDATE reservation_machines SET user_id = ?, serial_num = ?, start_date = ?, end_date = ? WHERE reservation_id = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
                
            pstmt.setString(1, reservation.getUserId());
            pstmt.setString(2, reservation.getSerialNumber());
            pstmt.setTimestamp(3, Timestamp.valueOf(reservation.getStartDate().atStartOfDay()));
            pstmt.setTimestamp(4, Timestamp.valueOf(reservation.getEndDate().atStartOfDay()));
            pstmt.setInt(5, reservation.getReservationId());
            boolean rowUpdated = pstmt.execute();
            if (rowUpdated == false) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public ArrayList<Machine> searchAllMachines() {
        ArrayList<Machine> machineList = new ArrayList<Machine>();
        String sql = "SELECT * FROM machines";  
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String serialNumber = rs.getString("serial_num"), name = rs.getString("name");
                LocalDate adquisitionDate = rs.getTimestamp("adquisition_date").toLocalDateTime().toLocalDate();
                String type = rs.getString("type"), status = rs.getString("status");
                    
                Machine machine = new Machine(serialNumber, name, adquisitionDate, type, status);
                machineList.add(machine);   
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return machineList;
    }

    public ArrayList <Machine> searchMachineWithSerialNumber(String serialNumberInput) {
        ArrayList<Machine> machineList = new ArrayList<Machine>();
        String sql = "SELECT * FROM machines WHERE serial_num = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, serialNumberInput);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String serialNumber = rs.getString("serial_num"), name = rs.getString("name");
                LocalDate adquisitionDate = rs.getTimestamp("adquisition_date").toLocalDateTime().toLocalDate();
                String type = rs.getString("type"), status = rs.getString("status");
                
                Machine machine = new Machine(serialNumber, name, adquisitionDate, type, status);
                machineList.add(machine);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return machineList;
    }

    public ArrayList <Machine> searchMachineWithName(String nameInput) {
        ArrayList<Machine> machineList = new ArrayList<Machine>();
        String sql = "SELECT * FROM machines WHERE name = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, nameInput);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String serialNumber = rs.getString("serial_num"), name = rs.getString("name");
                LocalDate adquisitionDate = rs.getTimestamp("adquisition_date").toLocalDateTime().toLocalDate();
                String type = rs.getString("type"), status = rs.getString("status");
                
                Machine machine = new Machine(serialNumber, name, adquisitionDate, type, status);
                machineList.add(machine);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return machineList;
    }

    public ArrayList <Machine> searchMachineWithAdquisitonDate(LocalDate dateInput) {
        ArrayList<Machine> machineList = new ArrayList<Machine>();
        String sql = "SELECT * FROM machines WHERE adquisition_date = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, Timestamp.valueOf(dateInput.atStartOfDay()));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String serialNumber = rs.getString("serial_num"), name = rs.getString("name");
                LocalDate adquisitionDate = rs.getTimestamp("adquisition_date").toLocalDateTime().toLocalDate();
                String type = rs.getString("type"), status = rs.getString("status");
                
                Machine machine = new Machine(serialNumber, name, adquisitionDate, type, status);
                machineList.add(machine);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return machineList;
    }

    public ArrayList <Machine> searchMachineWithType(String typeInput) {
        ArrayList<Machine> machineList = new ArrayList<Machine>();
        String sql = "SELECT * FROM machines WHERE type = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, typeInput);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String serialNumber = rs.getString("serial_num"), name = rs.getString("name");
                LocalDate adquisitionDate = rs.getTimestamp("adquisition_date").toLocalDateTime().toLocalDate();
                String type = rs.getString("type"), status = rs.getString("status");
                
                Machine machine = new Machine(serialNumber, name, adquisitionDate, type, status);
                machineList.add(machine);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return machineList;
    }

    public ArrayList <Machine> searchMachineWithStatus(String statusInput) {
        ArrayList<Machine> machineList = new ArrayList<Machine>();
        String sql = "SELECT * FROM machines WHERE status = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, statusInput);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String serialNumber = rs.getString("serial_num"), name = rs.getString("name");
                LocalDate adquisitionDate = rs.getTimestamp("adquisition_date").toLocalDateTime().toLocalDate();
                String type = rs.getString("type"), status = rs.getString("status");
                
                Machine machine = new Machine(serialNumber, name, adquisitionDate, type, status);
                machineList.add(machine);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return machineList;
    }

    public Machine searchSpecificMachine(String serialNumberInput) {
        Machine machine;
        String sql = "SELECT * FROM machines WHERE serial_num = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, serialNumberInput);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String serialNumber = rs.getString("serial_num"), name = rs.getString("name");
                LocalDate adquisitionDate = rs.getTimestamp("adquisition_date").toLocalDateTime().toLocalDate();
                String type = rs.getString("type"), status = rs.getString("status");
                
                machine = new Machine(serialNumber, name, adquisitionDate, type, status);
                return machine;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<SimpleUser> searchAllSimpleUsers() {
        ArrayList<SimpleUser> simpleUsersList = new ArrayList<SimpleUser>();
        String sql = "SELECT * FROM users";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString("user_id"), name = rs.getString("name"),
                    surname = rs.getString("surname"), email = rs.getString("email");
                int tlfNumber = rs.getInt("tlf_num");
                String username = rs.getString("username");
                int type = rs.getInt("type");

                SimpleUser simpleUser = new SimpleUser(userId, name, surname, email, tlfNumber, username, type);
                simpleUsersList.add(simpleUser);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return simpleUsersList;
    }

    public ArrayList<SimpleUser> searchSimpleUserWithId(String data) {
        ArrayList<SimpleUser> simpleUserList = new ArrayList<SimpleUser>();
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, data);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString("user_id"), name = rs.getString("name"),
                    surname = rs.getString("surname"), email = rs.getString("email");
                int tlfNumber = rs.getInt("tlf_num");
                String username = rs.getString("username");
                int type = rs.getInt("type");

                SimpleUser simpleUser = new SimpleUser(userId, name, surname, email, tlfNumber, username, type);
                simpleUserList.add(simpleUser);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return simpleUserList;
    }

    public ArrayList<SimpleUser> searchSimpleUserWithName(String data) {
        ArrayList<SimpleUser> simpleUserList = new ArrayList<SimpleUser>();
        String sql = "SELECT * FROM users WHERE name = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, data);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString("user_id"), name = rs.getString("name"),
                    surname = rs.getString("surname"), email = rs.getString("email");
                int tlfNumber = rs.getInt("tlf_num");
                String username = rs.getString("username");
                int type = rs.getInt("type");

                SimpleUser simpleUser = new SimpleUser(userId, name, surname, email, tlfNumber, username, type);
                simpleUserList.add(simpleUser);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return simpleUserList;
    }

    public ArrayList<SimpleUser> searchSimpleUserWithSurname(String data) {
        ArrayList<SimpleUser> simpleUserList = new ArrayList<SimpleUser>();
        String sql = "SELECT * FROM users WHERE surname = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, data);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString("user_id"), name = rs.getString("name"),
                    surname = rs.getString("surname"), email = rs.getString("email");
                int tlfNumber = rs.getInt("tlf_num");
                String username = rs.getString("username");
                int type = rs.getInt("type");

                SimpleUser simpleUser = new SimpleUser(userId, name, surname, email, tlfNumber, username, type);
                simpleUserList.add(simpleUser);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return simpleUserList;
    }

    public ArrayList<SimpleUser> searchSimpleUserWithTlfNumber(int data) {
        ArrayList<SimpleUser> simpleUserList = new ArrayList<SimpleUser>();
        String sql = "SELECT * FROM users WHERE tlf_num = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setInt(1, data);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString("user_id"), name = rs.getString("name"),
                    surname = rs.getString("surname"), email = rs.getString("email");
                int tlfNumber = rs.getInt("tlf_num");
                String username = rs.getString("username");
                int type = rs.getInt("type");

                SimpleUser simpleUser = new SimpleUser(userId, name, surname, email, tlfNumber, username, type);
                simpleUserList.add(simpleUser);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return simpleUserList;
    }

    public int updatePassword(String password, String userId) {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
                
            pstmt.setString(1, password);
            pstmt.setString(2, userId);
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

    public boolean deleteUser(String userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
                
            pstmt.setString(1, userId);
            boolean rowUpdated = pstmt.execute();
            if (rowUpdated == false) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            
        }
        return true;
    }

    public boolean deleteReservation(int reservationId) {
        String sql = "DELETE FROM reservation_machines WHERE reservation_id = ?";
        try (Connection connection = connect();
        PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setInt(1, reservationId);
            boolean rowUpdated = pstmt.execute();
            if (rowUpdated == false) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public boolean addReservation(Reservation reservation) {
        String sql = "INSERT INTO reservation_machines (user_id, serial_num, start_date, end_date)" +
                "VALUES(?, ?, ?, ?)";
        try (Connection connection = connect();
        PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, reservation.getUserId());
            pstmt.setString(2, reservation.getSerialNumber());
            pstmt.setTimestamp(3, Timestamp.valueOf(reservation.getStartDate().atStartOfDay()));
            pstmt.setTimestamp(4, Timestamp.valueOf(reservation.getEndDate().atStartOfDay()));
            boolean rowUpdated = pstmt.execute();
            if (rowUpdated == false) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean deleteMachine(String serialNumber) {
        String sql = "DELETE FROM machines WHERE serial_num = ?";
        try (Connection connection = connect();
        PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, serialNumber);
            boolean rowUpdated = pstmt.execute();
            if (rowUpdated == false) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public boolean deleteAllReservations(String userId) {
        String sql = "DELETE FROM reservation_machines WHERE user_id = ?";
        try (Connection connection = connect();
        PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            boolean rowUpdated = pstmt.execute();
            if (rowUpdated == false) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
    
    public ArrayList<Reservation> searchReservationWithUserId(String userID) {
        ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
        String sql = "SELECT * FROM reservation_machines WHERE user_id = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, userID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String serialNumber = rs.getString("serial_num");
                LocalDate startDate = rs.getTimestamp("start_date").toLocalDateTime().toLocalDate(),
                    endDate = rs.getTimestamp("end_date").toLocalDateTime().toLocalDate();
                int reservationId = rs.getInt("reservation_id");

                Reservation reservation = new Reservation(userID, serialNumber, startDate, endDate, reservationId);
                reservationList.add(reservation);
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reservationList;
    }

    public boolean addMachine(Machine machine) {
        String sql = "INSERT INTO machines (serial_num, name, adquisition_date, type, status)" +
                "VALUES(?,?,?,?,?)";
        try (Connection connection = connect();
        PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, machine.getSerialNumber());
            pstmt.setString(2, machine.getName());
            pstmt.setTimestamp(3, Timestamp.valueOf(LocalDate.now().atStartOfDay()));
            pstmt.setString(4, machine.getType());
            pstmt.setString(5, machine.getStatus());
            boolean rowUpdated = pstmt.execute();
            if (rowUpdated == false) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean updateMachine(Machine machine) {
        String sql = "UPDATE machines SET name = ?, adquisition_date = ?, type = ?, status = ? WHERE serial_num = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
                
            pstmt.setString(1, machine.getName());
            pstmt.setTimestamp(2, Timestamp.valueOf(machine.getAcquisitionDate().atStartOfDay()));
            pstmt.setString(3, machine.getType());
            pstmt.setString(4, machine.getStatus());
            pstmt.setString(5, machine.getSerialNumber());
            boolean rowUpdated = pstmt.execute();
            if (rowUpdated == false) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean updateReservationUserId(String assignUserId, String dessignUserId) {
        String sql = "UPDATE reservation_machines SET user_id = ? WHERE user_id = ?";
        try (Connection connection = connect();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
                
            pstmt.setString(1, assignUserId);
            pstmt.setString(2, dessignUserId);
            boolean rowUpdated = pstmt.execute();
            if (rowUpdated == false) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean deleteReservationWithSerialNumber(String serialNumber) {
        String sql = "DELETE FROM reservation_machines WHERE serial_num = ?";
        try (Connection connection = connect();
        PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, serialNumber);
            boolean rowUpdated = pstmt.execute();
            if (rowUpdated == false) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
}

