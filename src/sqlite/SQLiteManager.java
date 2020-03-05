package sqlite;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

import static sqlite.Consts.*;

/**
 * Class to manage SQLite database
 * Contains  methods for accessing db
 */
public class SQLiteManager {


    /**
     * Database connection object
     */
    private Connection connection = null;

    /**
     * Prepared statement object for various SQL statements
     * Usually more recommended than regular statement object
     * Less prone to SQL injection attacks
     */
    private PreparedStatement preparedStatement = null;

    /**
     * Database name string
     */
    private String dbName;

    /**
     * Class constructor that sets the database name and sets up the connection to the database
     *
     * @param dbName name of the database
     */
    public SQLiteManager(String dbName) {
        this.dbName = dbName;
        setUpConnectionToDB();
    }

    /**
     * Opens sqlite database or starts a new database with provided name if it does not already exist
     * Database is under "%AbsoluteUserHomeDirectory%/sqlite"
     */
    private void setUpConnectionToDB() {
        // Locate DB on platform-independent user home path under the directory named "sqlite"
        // File.separator is used based on existing platform
        String dbPathURL =
                System.getProperty("user.home") +
                        File.separator +
                        SQLITE_DIR_NAME +
                        File.separator;

        // Make directory if does not exist
        File dbDirectory = new File(dbPathURL);
        if (!dbDirectory.exists()) {
            dbDirectory.mkdir();
        }
        try {
            // Try connecting to the database
            connection = DriverManager.getConnection(PARAM_JDBC_DB_PREFIX + dbPathURL + dbName);
            if (connection != null) {
                connection.createStatement().execute(PARAM_JDBC_DB_ENABLE_FK);
                DatabaseMetaData meta = connection.getMetaData();
                // Print driver name
                Printer.printDriverName(meta.getDriverName());
                //Print database URL
                Printer.printDBConnection(meta.getURL());
            }
        } catch (SQLException e) {
            // Print db connection exception
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * Closes SQLite database connection
     */
    public void disconnectAndCloseDB() {
        try {
            // Close database connection
            connection.close();
            // Print disconnection message
            Printer.printCloseDBMessage();
            // Set connection to null
            connection = null;
        } catch (Exception e) {
            // Print db disconnection exception
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * If the table does not exist, add a new table with the provided name and columns
     * Columns include column names, constraints and checks
     *
     * @param tableName name of table to be added
     * @param columns   columns of table including name, constraints and checks
     */
    void addTableToDB(String tableName, String[] columns) {
        try {
            connection.setAutoCommit(true);
            // Add column names by concatenation since they cannot be passed to the prepared statement
            String tableQuery = DBUtils.constructTableQuery(tableName, columns);
            // Prepared statement for new table
            preparedStatement = connection.prepareStatement(tableQuery);
            preparedStatement.executeUpdate();
            // Close prepared statement
            preparedStatement.close();
            Printer.printTableAddedMessage(tableName);

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * If trigger does not exist, add trigger to specified table
     *
     * @param tableName   name of table to add trigger on
     * @param triggerName name of the trigger
     * @param executeOn   could be : 'before insert on', 'before update on' ...
     * @param statements  SQL statements to be executed with the trigger
     */
    void addTriggerToTable(String tableName, String triggerName, String executeOn, String[] statements) {
        try {
            connection.setAutoCommit(true);
            //
            StringBuilder triggerQuery = new StringBuilder(SQL_CREATE_TRIGGER
                    + triggerName
                    + executeOn
                    + tableName);
            triggerQuery.append(SQL_BEGIN);
            for (String statement : statements) {
                triggerQuery.append(statement);
            }
            triggerQuery.append(SQL_END);
            System.out.println(triggerQuery.toString());
            // Prepared statement for new table
            preparedStatement = connection.prepareStatement(triggerQuery.toString());
            preparedStatement.executeUpdate();
            // Close prepared statement
            preparedStatement.close();
            Printer.printTriggerAddedMessage(tableName, triggerName);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * General dynamic insert for any table in database
     *
     * @param tableName name of the table to insert into
     * @param values    insertion objects, each type will be added according to instance (String, Long etc..)
     * @return ID of the inserted row, or database raised error
     */
    Object insertDataInTable(String tableName, Object[] values) {
        Object result = null;
        try {
            connection.setAutoCommit(true);
            // Construct sql statement : INSERT INTO <tablename> (?, ... )
            String insertQuery = DBUtils.constructInsertQuery(tableName, values.length);
            // Prepared statement for new table
            preparedStatement = connection.prepareStatement(insertQuery);
            // Bind values to prepared statement
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
            // On fail to insert, return failure reason
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
    ArrayList<Object[]> selectDataFromTable(String[] tableNames, String[] selections, String[] conditions, Object[] values) {
        ArrayList<Object[]> result = new ArrayList<>();
        // Check if selecting specific column or all table columns
        try {
            connection.setAutoCommit(true);
            // Construct sql statement : SELECT <selection1, ...> FROM <tablename1, ...> WHERE (...)
            String selectQuery = DBUtils.constructSelectQuery(tableNames, selections, conditions);
            // Prepared statement for new table
            preparedStatement = connection.prepareStatement(selectQuery);
            // Bind values to prepared statement
            if (values != null && values.length > 0)
                DBUtils.bindValuesToPreparedStatement(preparedStatement, values);
            // Execute Query and get result
            System.out.println(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Store result in 2d Array
            result = DBUtils.resultSetToArray(resultSet);
            // Close result set
            resultSet.close();
            // Close prepared statement
            preparedStatement.close();

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * Deletes row/s from a table based on specified conditions
     *
     * @param tableName  name of table to delete from
     * @param conditions conditions of data deletion
     * @param values     left hand side values of condition statements
     */
    void deleteDataFromTable(String tableName, String[] conditions, Object[] values) {
        if (conditions == null) {
            return;
        }
        try {
            connection.setAutoCommit(true);
            // Construct sql statement : DELETE FROM <tablename> WHERE (?, ... )
            String deleteQuery = DBUtils.constructDeleteQuery(tableName, conditions);
            // Prepared statement for new table
            preparedStatement = connection.prepareStatement(deleteQuery);
            // Bind values to prepared statement
            if (conditions.length > 0) DBUtils.bindValuesToPreparedStatement(preparedStatement, values);
            // Execute Query and get result
            preparedStatement.executeUpdate();
            // Close prepared statement
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
