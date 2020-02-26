package sqlite;

import others.Constants;
import others.Utils;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;


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
                connection.createStatement().execute(Constants.PARAM_JDBC_DB_ENABLE_FK);
                DatabaseMetaData meta = connection.getMetaData();
                // Print driver name
                Printer.printDriverName(meta.getDriverName());
                //Print database URL
                Printer.printDBConnection(meta.getURL());
            }

            connected = true;

        } catch (SQLException e) {
            // Print db connection exception
//            e.printStackTrace();
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
            Printer.printCloseDBMessage();
            // Set connection to null
            connection = null;
            disconnected = true;
        } catch (Exception e) {
            // Print db disconnection exception
//            e.printStackTrace();
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
            Printer.printTableAddedMessage(tableName);

        } catch (SQLException e) {
//            e.printStackTrace();
        }
        return result;
    }

    public static boolean addTriggerToTable(String tableName, String triggerName, String executeOn, String[] statements) {
        boolean result = false;
        try {
            connection.setAutoCommit(true);
            //
            StringBuilder triggerQuery = new StringBuilder(Constants.SQL_CREATE_TRIGGER
                    + triggerName
                    + executeOn
                    + tableName);
            triggerQuery.append(Constants.SQL_BEGIN);
            for (String statement : statements) {
                triggerQuery.append(statement);
            }
            triggerQuery.append(Constants.SQL_END);
            // Prepared statement for new table
            preparedStatement = connection.prepareStatement(triggerQuery.toString());
            preparedStatement.executeUpdate();
            System.out.println(triggerQuery.toString());
            // Close prepared statement
            preparedStatement.close();
            result = true;
            Printer.printTriggerAddedMessage(tableName, triggerName);
        } catch (SQLException e) {
//            e.printStackTrace();
        }
        return result;
    }

    ;

    /**
     * General dynamic insert for any table in database
     *
     * @param tableName name of the table to insert into
     * @param values    insertion objects, each type will be added according to instance (String, Long etc..)
     * @return ID of the inserted row
     */
    public static Object insertDataInTable(String tableName, Object[] values) {
        Object result = null;
        try {
            connection.setAutoCommit(true);
            // Construct sql statement : INSERT INTO <tablename> (?, ... )
            String insertQuery = Constants.SQL_INSERT_INTO_TABLE
                    + tableName
                    + Constants.SQL_VALUES
                    + Utils.valuesPlaceholderDynamicConstructor(values.length);
            // Prepared statement for new table
            preparedStatement = connection.prepareStatement(insertQuery);
            // Bind values to prepared statement
            System.out.println(insertQuery);
            DBUtils.bindValuesToPreparedStatement(preparedStatement, values);
            // Execute insert update
            preparedStatement.executeUpdate();
            // Get id of the row inserted
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getObject(1);
            }
            // Close result set
            resultSet.close();
            // Close prepared statement
            preparedStatement.close();

        } catch (SQLException e) {
//            e.printStackTrace();
            result = e.getLocalizedMessage().split("[()]")[1];
        }
        return result;
    }

    /**
     * Selects dynamically required results from a set of tables based on a set of conditions
     *
     * @param tableNames names of tables to get results from
     * @param selections names of selected columns from tables
     * @param conditions conditions of data retrieval
     * @param values     left hand side values of condition statements
     * @return 2d array of query results
     */
    public static ArrayList<Object[]> selectDataFromTable(String[] tableNames, String[] selections, String[] conditions, Object[] values) {
        ArrayList<Object[]> result = new ArrayList<>();
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
            // Prepared statement for new table
            preparedStatement = connection.prepareStatement(selectQuery.toString());
            // Bind values to prepared statement
            if (values != null && values.length > 0)
                DBUtils.bindValuesToPreparedStatement(preparedStatement, values);
            // Execute Query and get result
            ResultSet resultSet = preparedStatement.executeQuery();
            // Store result in 2d Array
            result = DBUtils.resultSetToArray(resultSet);
            // Close result set
            resultSet.close();
            // Close prepared statement
            preparedStatement.close();

        } catch (SQLException e) {
//            e.printStackTrace();
        }
        return result;
    }

    /**
     * Deletes row/s from a table based on specified conditions
     *
     * @param tableName  name of table to delete from
     * @param conditions conditions of data deletion
     * @param values     left hand side values of condition statements
     * @return deleted or not
     */
    static boolean deleteDataFromTable(String tableName, String[] conditions, Object[] values) {
        if (conditions == null || values == null) {
            return false;
        }
        boolean result = false;
        try {
            connection.setAutoCommit(true);
            // Construct sql statement : DELETE FROM <tablename> WHERE (?, ... )
            StringBuilder deleteQuery = new StringBuilder(
                    Constants.SQL_DELETE_FROM_TABLE
                            + tableName);
            // Check if there are any selection conditions
            if (conditions.length > 0) {
                deleteQuery.append(Constants.SQL_WHERE);
                for (String condition : conditions) {
                    deleteQuery.append(condition).append(" ");
                }
            }
            // Prepared statement for new table
            preparedStatement = connection.prepareStatement(deleteQuery.toString());
            // Bind values to prepared statement
            if (conditions.length > 0) DBUtils.bindValuesToPreparedStatement(preparedStatement, values);
            // Execute Query and get result
            preparedStatement.executeUpdate();
            // Store result in 2d Array
            result = true;
            // Close prepared statement
            preparedStatement.close();
        } catch (SQLException e) {
//            e.printStackTrace();
        }
        return result;
    }
}
