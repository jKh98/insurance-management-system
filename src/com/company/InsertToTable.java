package com.company;

import java.util.Map;

/**
 * Class to handle table-specific insertions
 */
public class InsertToTable {
    public static void addTravelPolicy(Map<String, Object> values) {
        SQLiteManager.insertDataInTable(Constants.TABLE_NAME_POLICY, new Object[]{
                null,
                values.get(Constants.TABLE_COLUMN_EFFECTIVE),
                values.get(Constants.TABLE_COLUMN_EXPIRY),
                values.get(Constants.TABLE_COLUMN_PREMIUM),
                values.get(Constants.TABLE_COLUMN_IS_VALID),
                values.get(Constants.TABLE_COLUMN_POLICY_TYPE),
        });

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
