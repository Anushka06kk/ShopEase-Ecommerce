package  ecommerce;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection.java
 * 
 * Singleton class responsible for managing the MySQL database connection.
 * Uses JDBC to connect Java with MySQL.
 * 
 * JDBC Steps followed:
 *  1. Load the driver
 *  2. Establish connection
 *  3. Return connection object (used by other classes to prepare statements)
 *  4. Provide a close utility
 */
public class DBConnection {

    // ── Connection settings ──────────────────────────────────────────────────
    private static final String HOST     = "localhost";
    private static final String PORT     = "3306";
    private static final String DATABASE = "ecommerce";
    private static final String USERNAME = "root";       // your MySQL username
    private static final String PASSWORD = "Anushka@123";           // your MySQL password

    // Full JDBC URL
    private static final String URL =
        "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
        + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    // Singleton connection instance
    private static Connection connection = null;

    // ── Private constructor — no one should instantiate this class ───────────
    private DBConnection() {}

    /**
     * Returns a single shared Connection object.
     * Creates it on first call (lazy initialization).
     *
     * @return Connection object
     */
    public static Connection getConnection() {
        try {
            // STEP 1: Load the MySQL JDBC Driver
            // (Not strictly needed for modern JDBC 4.0+, but good practice)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // STEP 2: Establish the connection if not already open
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("✅ Database connected successfully!");
            }

        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL Driver not found. Add mysql-connector-j.jar to your build path.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Connection failed. Check your MySQL credentials and make sure MySQL is running.");
            e.printStackTrace();
        }

        return connection;
    }

    /**
     * Closes the connection when the application shuts down.
     * Call this in your Main.java on exit.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("🔒 Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
