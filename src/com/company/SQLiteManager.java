package com.company;

import java.io.File;
import java.sql.*;


/**
 * Class to manage SQLite database
 * Contains static methods for accessing db
 */
public class SQLiteManager {


    /**
     * Database connection object
     */
    private static Connection connection = null;

    /**
     * Prepared statement object for various SQL statements
     * Usually more recommended than regular statement
     */
    private static PreparedStatement preparedStatement = null;

    /**
     * Opens sqlite database or starts a new database with provided name if it does not already exist
     * Database is under "%AbsoluteUserHomeDirectory%/sqlite"
     *
     * @param dbName name of database to be used
     * @return if connected or not
     */
    public static boolean setUpConnectionToDB(String dbName) {

        boolean connected = false;

        // Locate DB on platform-independent user home path under the directory named "sqlite"
        // File.separator is used based on existing platform
        String dbPathURL =
                System.getProperty("user.home") +
                        File.separator +
                        Constants.SQLITE_DIR_NAME +
                        File.separator;

        // Make directory if does not exist
        File dbDirectory = new File(dbPathURL);
        if (!dbDirectory.exists()) {
            dbDirectory.mkdir();
        }

        try {
            // Try connecting to the database
            connection = DriverManager.getConnection(Constants.PARAM_JDBC_DB_PREFIX + dbPathURL + dbName);
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
                // Print driver name
                System.out.println(Constants.MESSAGE_DB_DRIVER + meta.getDriverName());
                //Print database URL
                System.out.println(Constants.MESSAGE_OPENED_DB + meta.getURL());
            }

            connected = true;

        } catch (SQLException e) {
            // Print db connection exception
            System.out.println(e.getMessage());
        }

        return connected;
    }


    /**
     * @return if disconnected or not
     */
    public static boolean disconnectAndCloseDB() {
        boolean disconnected = false;
        try {
            // Close database connection
            connection.close();
            // Print disconnection message
            System.out.println(Constants.MESSAGE_CLOSED_DB);
            // Set connection to null
            connection = null;
            disconnected = true;
        } catch (Exception e) {
            // Print db disconnection exception
            System.out.println(e.getMessage());
        }
        return disconnected;
    }

    public static boolean addTableToDB(String tableName, String[] columnNames) {
        boolean result = false;
        try {
            connection.setAutoCommit(true);
            // Add column names by concatenation since they cannot be passed to the prepared statement
            StringBuilder tableQuery = new StringBuilder(Constants.SQL_CREATE_TABLE + tableName);
            // Populate values
            tableQuery.append("(");
            for (String columnName : columnNames) {
                tableQuery.append(columnName);
                tableQuery.append(",");
            }
            tableQuery.delete(tableQuery.length() - 1, tableQuery.length());
            tableQuery.append(")");
            // Prepared statement for new table
            preparedStatement = connection.prepareStatement(tableQuery.toString());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            result = true;
            System.out.println(Constants.MESSAGE_TABLE_ADDED);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
