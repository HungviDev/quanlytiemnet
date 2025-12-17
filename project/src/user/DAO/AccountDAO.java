package user.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import user.Model.AccountModel;

public class AccountDAO {

    private Connection connection;

    public AccountDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Lấy thông tin người dùng khi đăng nhập
     * 
     * @param username
     * @param password
     * @return AccountModel hoặc null nếu sai tài khoản
     */
    public AccountModel getInfoByAccAndPass(String username, String password) {
        AccountModel account = null;
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int id = rs.getInt("user_id");
                // account.set(rs.getInt("id"));
                String users = rs.getString("username");
                String passwords = rs.getString("password");
                // account.setFullname(rs.getString("fullname"));
                Double balance = rs.getDouble("balance");
                String date = rs.getDate("created_at").toString();
                account = new AccountModel(id, users, passwords, balance, date);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return account;
    }

    public boolean checkLogin(String username, String password) {
        String sql = "SELECT 1 FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            return rs.next(); // có dữ liệu → true
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
        public boolean updateiduserbyip(String ip, int iduser) {
        String sql = "UPDATE computers SET current_user_id = ? WHERE ip_address = ?";
        try (
            PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setInt(1, iduser);
            statement.setString(2, ip);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
