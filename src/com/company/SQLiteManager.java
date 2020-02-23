package com.company;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;


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
     * Usually more recommended than regular statement object
     * Less prone to SQL injection attacks
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
     * Closes SQLite database connection
     *
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

    /**
     * If the table does not exist, add a new table with the provided name and columns
     * Columns include column names, constraints and checks
     *
     * @param tableName name of table to be added
     * @param columns   columns of table including name, constraints and checks
     * @return
     */
    public static boolean addTableToDB(String tableName, String[] columns) {
        boolean result = false;
        try {
            connection.setAutoCommit(true);
            // Add column names by concatenation since they cannot be passed to the prepared statement
            String tableQuery = Constants.SQL_CREATE_TABLE
                    + tableName
                    + Utils.argumentsDynamicConstructor(columns, true);
            // Prepared statement for new table
            preparedStatement = connection.prepareStatement(tableQuery);
            preparedStatement.executeUpdate();
            // Close prepared statement
            preparedStatement.close();
            result = true;
            System.out.println(tableName + Constants.MESSAGE_TABLE_ADDED);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * General dynamic insert for any table in database
     *
     * @param tableName name of the table to insert into
     * @param values    insertion objects, each type will be added according to instance (String, Long etc..)
     * @return ID of the inserted row
     */
    public static Long insertDataInTable(String tableName, Object[] values) {
        long result = 0L;
        try {
            connection.setAutoCommit(true);
            // Construct sql statement : INSERT INTO <tablename> (?, ... )
            String insertQuery = Constants.SQL_INSERT_INTO_TABLE
                    + tableName
                    + Constants.SQL_VALUES
                    + Utils.valuesPlaceholderDynamicConstructor(values.length);
            // Prepared statement for new table
            preparedStatement = connection.prepareStatement(insertQuery);
            int i = 1;
            // Bind values dynamically based on type to '?' placeholders
            for (Object value : values) {
                if (value instanceof Date) {
                    preparedStatement.setTimestamp(i++, new Timestamp(((Date) value).getTime()));
                } else if (value instanceof Integer) {
                    preparedStatement.setInt(i++, (Integer) value);
                } else if (value instanceof Long) {
                    preparedStatement.setLong(i++, (Long) value);
                } else if (value instanceof Double) {
                    preparedStatement.setDouble(i++, (Double) value);
                } else {
                    preparedStatement.setString(i++, (String) value);
                }
            }
            preparedStatement.executeUpdate();
            // Get id of the row inserted
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getLong(1);
            }
            // Close result set
            resultSet.close();
            // Close prepared statement
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ResultSet selectDataFromTable(String[] tableNames, String[] selections, String[] conditions, Object[] values) {
        ResultSet result = null;
        // Check if selecting specific column or all table columns
        String selectionsString;
        if (selections == null || selections.length == 0) {
            selectionsString = Constants.SQL_ALL;
        } else {
            selectionsString = Utils.argumentsDynamicConstructor(selections, false);
        }
        try {
            connection.setAutoCommit(true);
            // Construct sql statement : SELECT <selection1, ...> FROM <tablename1, ...> WHERE (...)
            StringBuilder selectQuery = new StringBuilder(Constants.SQL_SELECT
                    + selectionsString
                    + Constants.SQL_FROM
                    + Utils.argumentsDynamicConstructor(tableNames, false));
            // Check if there are any selection conditions
            if (conditions != null && conditions.length > 0) {
                selectQuery.append(Constants.SQL_WHERE);
                for (String condition : conditions) {
                    selectQuery.append(condition).append(" ");
                }
            }
            System.out.println(selectQuery.toString());
            // Prepared statement for new table
            preparedStatement = connection.prepareStatement(selectQuery.toString());
            int i = 1;
            // Bind values dynamically based on type to '?' placeholders
            for (Object value : values) {
                if (value instanceof Date) {
                    preparedStatement.setTimestamp(i++, new Timestamp(((Date) value).getTime()));
                } else if (value instanceof Integer) {
                    preparedStatement.setInt(i++, (Integer) value);
                } else if (value instanceof Long) {
                    preparedStatement.setLong(i++, (Long) value);
                } else if (value instanceof Double) {
                    preparedStatement.setDouble(i++, (Double) value);
                } else {
                    preparedStatement.setString(i++, (String) value);
                }
            }
            result = preparedStatement.executeQuery();
            // Close result set
//            result.close();
            // Close prepared statement
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
