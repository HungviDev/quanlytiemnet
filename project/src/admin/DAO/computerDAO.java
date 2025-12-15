package admin.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Connection.DatabaseConnection;
import admin.Model.computer;

public class computerDAO {
    private Connection connection;
    public computerDAO(Connection connection) {
        this.connection = connection;
    }
    public ArrayList<computer> getAllComputer() {
        ArrayList<computer> computerList = new ArrayList<>();
        String sql = "SELECT computer_id, name, ip_address, status FROM computers";

        try (
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
        ) {

            while (rs.next()) {
                computer c = new computer(
                    rs.getString("computer_id"),
                    rs.getString("name"),
                    rs.getString("ip_address"),
                    rs.getString("status")
                );
                computerList.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return computerList;
    }
    public boolean deleteComputerByIp(String ipAddress) {
    String sql = "DELETE FROM computers WHERE ip_address = ?";
    try (
        PreparedStatement statement = connection.prepareStatement(sql);
    ) {
        statement.setString(1, ipAddress);
        int rowsDeleted = statement.executeUpdate();
        return rowsDeleted > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    public boolean insertComputer(computer computer) {
   
        String sql = "INSERT INTO computers (ip_address, computer_name, status) VALUES (?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, computer.getIpadress());
            statement.setString(2, computer.getName()); 
            statement.setString(3, computer.getStatus()); 
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
            
        } catch (SQLException e) {
            System.err.println("LỖI SQL khi thêm máy tính:");
            System.err.println("Chi tiết: " + e.getMessage());
            return false;
        }
    }
    
}
