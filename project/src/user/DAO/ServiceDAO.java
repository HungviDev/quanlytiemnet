package user.DAO;

import user.Model.ServiceModel;
import java.sql.*;
import java.util.ArrayList;

import Connection.DatabaseConnection;

// import java.math.BigDecimal; // Không cần thiết nếu bạn dùng Double

public class ServiceDAO {

    // ... (Phần ServiceDAO constructor và getAllServices giữ nguyên như bạn đã gửi)
    // ...

    private Connection connection;

    public ServiceDAO(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<ServiceModel> getAllServices() {
        ArrayList<ServiceModel> services = new ArrayList<>();
        String sql = "SELECT service_id, name, quantity_unit, price, description, category FROM dbo.services";

        try (
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rs = statement.executeQuery();) {

            while (rs.next()) {
                int serviceId = rs.getInt("service_id");
                String name = rs.getString("name");
                String quantityUnit = rs.getString("quantity_unit");
                Double price = rs.getDouble("price"); // Đã dùng Double
                String description = rs.getString("description");
                String category = rs.getString("category");
                // Chú ý: Đảm bảo constructor của lớp Service nhận 6 tham số đúng kiểu dữ liệu
                services.add(new ServiceModel(serviceId, name, quantityUnit, price, description, category));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

    public ArrayList<ServiceModel> getServicesByCategory(String category) {
        ArrayList<ServiceModel> services = new ArrayList<>();
        // Sử dụng câu lệnh SELECT * để lấy tất cả các cột cần thiết
        String sql = "SELECT service_id, name, quantity_unit, price, description, category FROM dbo.services WHERE category = ?";

        // Sử dụng try-with-resources cho PreparedStatement
        try (
                PreparedStatement statement = connection.prepareStatement(sql);) {
            // Bước 1: Gán giá trị tham số (an toàn chống SQL Injection)
            // Tham số đầu tiên (index 1) là 'category'
            statement.setString(1, category);

            // Bước 2: Thực thi truy vấn và lấy ResultSet
            try (ResultSet rs = statement.executeQuery()) {

                // Bước 3: Lặp qua các dòng kết quả và tạo đối tượng Model
                while (rs.next()) {
                    int serviceId = rs.getInt("service_id");
                    String name = rs.getString("name");
                    String quantityUnit = rs.getString("quantity_unit");
                    Double price = rs.getDouble("price"); // Lấy giá trị Double
                    String description = rs.getString("description");
                    String foundCategory = rs.getString("category");

                    // Tạo đối tượng Model (đảm bảo constructor ServiceModel có 6 tham số)
                    services.add(new ServiceModel(serviceId, name, quantityUnit, price, description, foundCategory));
                }
            } // ResultSet đóng tự động

        } catch (SQLException e) {
            System.err.println("LỖI SQL: Không thể tải dịch vụ theo danh mục [" + category + "].");
            e.printStackTrace();
        }
        return services;
    }

}