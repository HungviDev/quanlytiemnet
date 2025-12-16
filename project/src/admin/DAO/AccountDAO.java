package admin.DAO;

import admin.Model.AccountModel;
import user.DAO.ServiceDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Connection.DatabaseConnection;

public class AccountDAO {

    private final Connection conn;

    public AccountDAO(Connection conn) {
        this.conn = conn;
    }

    // =========================
    // READ - Lấy toàn bộ user
    // =========================
    public ArrayList<AccountModel> getAllAccounts() {
        ArrayList<AccountModel> list = new ArrayList<>();
        String sql = "SELECT user_id, username, password, balance, created_at FROM users";

        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AccountModel acc = new AccountModel(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getLong("balance"),
                        rs.getString("created_at"));
                list.add(acc);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertAccount(AccountModel a) {
        String sql = "INSERT INTO users (username, password, balance, created_at) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getUsername());
            ps.setString(2, a.getPassword());
            ps.setLong(3, a.getBalance());
            ps.setString(4, a.getCreatedAt());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUsername(int userId, String newUsername) {
        String sql = "UPDATE users SET username = ? WHERE user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newUsername);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // UPDATE password (theo user_id)
    // =========================
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // UPDATE balance
    // =========================
    public boolean updateBalance(int userId, long newBalance) {
        String sql = "UPDATE users SET balance = ? WHERE user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, newBalance);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // ADD balance (cộng tiền)
    // =========================
    public boolean addBalance(int userId, long amount) {
        String sql = "UPDATE users SET balance = balance + ? WHERE user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, amount);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // DELETE user (theo user_id)
    // =========================
    public boolean deleteById(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkStatusByUserId(int userId) {
        String sql = "SELECT cp.status " +
                "FROM users us " +
                "JOIN computers cp ON us.user_id = cp.current_user_id " +
                "WHERE us.user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String status = rs.getString("status");
                return "Hoạt động".equalsIgnoreCase(status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            AccountDAO acd = new AccountDAO(connection);
            System.out.println(acd.getAllAccounts().toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
