package org.LibraryWithCRUDInMind.DBRepo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * With this we defined the connection to DB.
 */
public abstract class ConnectionToDb {
    private static String DB_NAME = "library_db";
    private static String DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;
    private static String DB_USER = "root";
    private static String DB_PASSWORD = "xxxxxxxxxxxx";
    private static Connection connection;

    /**
     * Open the connection and set the autocommit to false in order to be able to group several queries
     * into a transaction and validate results in order to be able to commit or rollback.
     */
    public static void open() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            connection.setAutoCommit(false);

        } catch (SQLException e) {
            System.out.printf("%sERROR - %s", " ".repeat(2), e.getMessage());
        }
    }

    /**
     * If connection is still active we set the autocommit back to true and close the connection after.
     */
    public static void close() {
        try {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        } catch (SQLException e) {
            System.out.printf("%sERROR - %s", " ".repeat(2), e.getMessage());
        }
    }

    /**
     * Open the connection and then return the connection.
     */
    public static Connection getAlreadyOpenConnection() {
        open();
        return connection;
    }
}
