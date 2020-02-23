package com.company;

import java.sql.*;
import java.util.ArrayList;

class Utils {

    /**
     * Dynamically builds "values(?, ... )" string of placeholders
     * needed for an SQL prepared statement based on number of values
     *
     * @param size number of values characters
     * @return "values(?, ... )" string with proper number of values
     */
    static String valuesPlaceholderDynamicConstructor(int size) {

        // Use string builder for higher performance when appending
        StringBuilder valuesBuilder = new StringBuilder("(");
        for (int i = 0; i < size; i++) {
            valuesBuilder.append("?");

            // Do not add "," after last "?"
            if (i != size - 1) {
                valuesBuilder.append(",");
            }
        }
        String values = valuesBuilder.toString();
        values += ")";
        return values;
    }

    /**
     * Dynamically builds (args1,arg2, ...) string of arguments
     * Needed for SQL table columns for new tables
     *
     * @param args list of string arguments
     * @return (arg1, arg2, ...) string with all arguments
     */
    static String argumentsDynamicConstructor(String[] args, boolean addParenthesis) {

        // Use string builder for higher performance when appending
        StringBuilder arguments = new StringBuilder();
        if (addParenthesis) arguments.append("(");
        // Append values
        for (String arg : args) {
            arguments.append(arg);
            arguments.append(",");
        }
        // Remove last comma
        arguments.delete(arguments.length() - 1, arguments.length());
        if (addParenthesis) arguments.append(")");
        return arguments.toString();
    }


    /**
     * Extracts result set data after execution of a query into a 2D array
     *
     * @param resultSet query results
     * @return 2d array containing query results
     * @throws SQLException
     */
    static ArrayList<Object[]> resultSetToArray(ResultSet resultSet) throws SQLException {
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
    static void bindValuesToPreparedStatement(PreparedStatement preparedStatement, Object[] values) throws SQLException {
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
}
