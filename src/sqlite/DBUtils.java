package sqlite;

import java.sql.*;
import java.util.ArrayList;
import static sqlite.Consts.*;


public class DBUtils {
    /**
     * Extracts result set data after execution of a query into a 2D array
     *
     * @param resultSet query results
     * @return 2d array containing query results
     * @throws SQLException
     */
    public static ArrayList<Object[]> resultSetToArray(ResultSet resultSet) throws SQLException {
        ArrayList<Object[]> table = new ArrayList<>();
        while (resultSet.next()) {
            Object[] row = new Object[resultSet.getMetaData().getColumnCount()];
            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                row[i] = resultSet.getObject(i + 1);
            }
            table.add(row);
        }
        return table;
    }

    /**
     * Dynamically binds query values to preprated statement place holders '?'
     *
     * @param preparedStatement prepared statement to bind values to
     * @param values            values to be bind
     * @throws SQLException
     */
    public static void bindValuesToPreparedStatement(PreparedStatement preparedStatement, Object[] values) throws SQLException {
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
            } else if (value instanceof Float) {
                preparedStatement.setFloat(i++, (Float) value);
            } else {
                preparedStatement.setString(i++, (String) value);
            }
        }
    }

    /**
     * Construct an SQL query for adding a new table to database
     *
     * @param tableName name of table to add
     * @param columns   array of column names to add
     * @return SQL create query
     */
    public static String constructTableQuery(String tableName, String[] columns) {
        return SQL_CREATE_TABLE
                + tableName
                + parenthesise(argumentsComaSeparator(columns));
    }

    /**
     * Construct an SQL Query string for inserting in a table and dynamically append ? placeholders
     * based on number of insertion columns
     *
     * @param tableName    name of table to insert to
     * @param numOfColumns number of columns to insert
     * @return SQL insert query
     */
    public static String constructInsertQuery(String tableName, int numOfColumns) {
        return SQL_INSERT_INTO_TABLE
                + tableName
                + SQL_VALUES
                + placeholderConstructor(numOfColumns);
    }

    /**
     * Constructs an SQL Query string for selecting a number of columns from a number of tables based
     * on a number of conditions.
     *
     * @param tableNames an array of table names
     * @param selections an array of column names for selection
     * @param conditions an array of conditions for selections, possibly having binding parameters ?
     * @return SQL select Query
     */
    public static String constructSelectQuery(String[] tableNames, String[] selections, String[] conditions) {
        String selectionsString = argumentsComaSeparator(selections);
        // New string builder
        StringBuilder selectQuery = new StringBuilder();
        // Add select word
        selectQuery.append(SQL_SELECT);
        // Append list of selections
        selectQuery.append(selectionsString);
        // Append from
        selectQuery.append(SQL_FROM);
        // Append list of tables
        selectQuery.append(argumentsComaSeparator(tableNames));

        // Check if there are any selection conditions
        if (conditions != null && conditions.length > 0) {
            // Append where
            selectQuery.append(SQL_WHERE);
            // for each condition, append it then pad with space
            for (String condition : conditions) {
                selectQuery.append(condition).append(" ");
            }
        }
        return selectQuery.toString();
    }

    /**
     * Constructs an SQL delete query based on table name and conditions
     *
     * @param tableName  name of table to delete from
     * @param conditions deletion conditions
     * @return SQL delete query
     */
    public static String constructDeleteQuery(String tableName, String[] conditions) {
        StringBuilder deleteQuery = new StringBuilder(
                SQL_DELETE_FROM_TABLE
                        + tableName);
        // Check if there are any selection conditions
        if (conditions.length > 0) {
            deleteQuery.append(SQL_WHERE);
            for (String condition : conditions) {
                deleteQuery.append(condition).append(" ");
            }
        }
        return deleteQuery.toString();
    }

    /**
     * Dynamically builds "values(?, ... )" string of placeholders
     * needed for an SQL prepared statement based on number of values
     *
     * @param size number of values characters
     * @return "values(?, ... )" string with proper number of values
     */
    private static String placeholderConstructor(int size) {

        // Use string builder for higher performance when appending
        StringBuilder valuesBuilder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            valuesBuilder.append("?");

            // Do not add "," after last "?"
            if (i != size - 1) {
                valuesBuilder.append(",");
            }
        }
        String values = valuesBuilder.toString();
        return parenthesise(values);
    }

    /**
     * Dynamically builds (args1,arg2, ...) string of arguments
     * Needed for SQL table columns for new tables
     *
     * @param args list of string arguments
     * @return (arg1, arg2, ...) string with all arguments
     */
    public static String argumentsComaSeparator(String[] args) {

        // Use string builder for higher performance when appending
        StringBuilder arguments = new StringBuilder();
        // Append values
        for (String arg : args) {
            arguments.append(arg);
            arguments.append(",");
        }
        // Remove last comma
        arguments.delete(arguments.length() - 1, arguments.length());
        return arguments.toString();
    }

    /**
     * Encloses String with parenthesis
     *
     * @param arg string to enclose
     * @return enclosed string
     */
    public static String parenthesise(String arg) {
        return "(" + arg + ")";
    }


    /**
     * Separates two strings with a dot
     * Used to achieve <tableName>.<columnName>
     *
     * @param tableName  table name
     * @param columnName column name
     * @return tableName.columnName
     */
    public static String dot(String tableName, String columnName) {
        return tableName + "." + columnName;
    }
}
