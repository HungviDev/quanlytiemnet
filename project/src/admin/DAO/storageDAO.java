package admin.DAO;

import admin.Model.Storage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class storageDAO {

    private Connection connection;

    public storageDAO(Connection connection) {
        this.connection = connection;
    }

    // ================= GET ALL =================
    public ArrayList<Storage> getAllStorage() {
        ArrayList<Storage> list = new ArrayList<>();

        String sql = "SELECT service_id, name, quantity_unit, price, description, category FROM services";

        try (
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                Storage s = new Storage(
                        rs.getString("service_id"),
                        rs.getString("name"),
                        rs.getString("quantity_unit"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getString("category"));
                list.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách dịch vụ");
            e.printStackTrace();
        }
        return list;
    }

    // ================= INSERT =================
    public boolean insertStorage(Storage s) {
        String sql = """
                    INSERT INTO services (name, quantity_unit, price, description, category)
                    VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getQuantity_unit());
            ps.setDouble(3, s.getPrice());
            ps.setString(4, s.getDescription());
            ps.setString(5, s.getCategory());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm dịch vụ");
            e.printStackTrace();
            return false;
        }
    }

    // ================= UPDATE =================
    public boolean updateStorage(Storage s) {
        String sql = """
                    UPDATE services
                    SET name = ?, quantity_unit = ?, price = ?, description = ?, category = ?
                    WHERE service_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getQuantity_unit());
            ps.setDouble(3, s.getPrice());
            ps.setString(4, s.getDescription());
            ps.setString(5, s.getCategory());
            ps.setString(6, s.getService_id());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật dịch vụ");
            e.printStackTrace();
            return false;
        }
    }

    // ================= DELETE =================
    public boolean deleteStorageById(String serviceId) {
        String sqlCart = "DELETE FROM cart WHERE service_id = ?";
        String sqlOrders = "DELETE FROM orders WHERE service_id = ?";
        String sqlServices = "DELETE FROM services WHERE service_id = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement ps1 = connection.prepareStatement(sqlCart);
                    PreparedStatement ps2 = connection.prepareStatement(sqlOrders);
                    PreparedStatement ps3 = connection.prepareStatement(sqlServices)) {

                ps1.setString(1, serviceId);
                ps1.executeUpdate();

                ps2.setString(1, serviceId);
                ps2.executeUpdate();

                ps3.setString(1, serviceId);
                int affected = ps3.executeUpdate();

                connection.commit();
                return affected > 0;
            }

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
            }
        }
    }

}
