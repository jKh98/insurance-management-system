package com.company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Class to handle table-specific insertions
 */
public class InsertToTable {
    static void addTravelPolicy(Map<String, Object> values) {
        Long id;
        id = SQLiteManager.insertDataInTable(Constants.TABLE_NAME_POLICY, new Object[]{
                null,
                values.get(Constants.TABLE_COLUMN_EFFECTIVE),
                values.get(Constants.TABLE_COLUMN_EXPIRY),
                values.get(Constants.TABLE_COLUMN_PREMIUM),
                values.get(Constants.TABLE_COLUMN_IS_VALID),
                values.get(Constants.TABLE_COLUMN_POLICY_TYPE),
        });
        ResultSet rs = SQLiteManager.selectDataFromTable(
                new String[]{Constants.TABLE_NAME_POLICY,},
                new String[]{Constants.TABLE_COLUMN_POLICY_NO,},
                new String[]{Constants.TABLE_COLUMN_ID + " = ?",},
                new Object[]{id}
                );
        try {
            System.out.println(rs.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void addMotorPolicy(Map<String, Object> values) {

    }

    public static void addMedicalPolicy(Map<String, Object> values) {

    }

    public static void addBeneficiary(Map<String, Object> values) {

    }

    public static void addClaim(Map<String, Object> values) {

    }
}
