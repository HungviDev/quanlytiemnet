package Connection;
import java.sql.Connection;
import java.sql.DriverManager;
public class DatabaseConnection {

    private static final String URL =
        "jdbc:sqlserver://localhost:1433;" +
        "databaseName=Cypher;" +
        "encrypt=false;" +
        "trustServerCertificate=true";

    private static final String USER = "sa";
    private static final String PASSWORD = "123";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
