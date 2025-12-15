package admin.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public ArrayList<computer> deleteComputerbyip_address(String ip_address) {
        ArrayList<computer> computerList = new ArrayList<>();
        String sql = "DELETE FROM computers WHERE ip_address = ?";
        try (
            PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, ip_address);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return computerList;
    }
    
}
