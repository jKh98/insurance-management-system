package com.sqlite;

import java.sql.*;
import java.util.ArrayList;

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
}
