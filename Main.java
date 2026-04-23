package ecommerce;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) {

        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            System.out.println("Everything is working!");
        } else {
            System.out.println("Database connection failed.");
        }

        // Open Login Page
        new LoginPage().setVisible(true);

        DBConnection.closeConnection();
    }
}