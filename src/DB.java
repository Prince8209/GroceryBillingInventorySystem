import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DB {
    static final String URL = "jdbc:postgresql://localhost:5432/grocery";
    static final String USER = "prince";          // replace with your username
    static final String PASS = "prince123";     // replace with your password

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
