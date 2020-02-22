package com.company;

import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

public class Main {

    public static void main(String[] args) {
        // 1. Connect to database
        SQLiteManager.setUpConnectionToDB("test.db");
        // 2. Add tables if they do not exist
        setUpTables();
        // 3. Add corresponding table triggers if they do not exist
        setUpTableTriggers();
        // 4. Insert table values
        InsertToTable.addTravelPolicy(Map.ofEntries(
                entry(Constants.TABLE_COLUMN_EFFECTIVE, System.currentTimeMillis() / 1000L),
                entry(Constants.TABLE_COLUMN_EXPIRY, System.currentTimeMillis() / 1000L + 100),
                entry(Constants.TABLE_COLUMN_PREMIUM, 0.0),
                entry(Constants.TABLE_COLUMN_IS_VALID, 1),
                entry(Constants.TABLE_COLUMN_POLICY_TYPE, "travel"),
                entry(Constants.TABLE_COLUMN_DEPARTURE, "Beirut"),
                entry(Constants.TABLE_COLUMN_DESTINATION, "Paris"),
                entry(Constants.TABLE_COLUMN_FAMILY, 1)
        ));
        // 5. Close database
        SQLiteManager.disconnectAndCloseDB();
    }

    /**
     * Sets up all database tables based on designed schema
     */
    private static void setUpTables() {

        // Policy table that stores all policies regardless what type
        SQLiteManager.addTableToDB(Constants.TABLE_NAME_POLICY, Constants.TABLE_COLUMN_VALUES_POLICY);
        // Table that stores data related to travel policies
        SQLiteManager.addTableToDB(Constants.TABLE_NAME_TRAVEL, Constants.TABLE_COLUMN_VALUES_TRAVEL);
        // Table that stores data related to motor policies
        SQLiteManager.addTableToDB(Constants.TABLE_NAME_MOTOR, Constants.TABLE_COLUMN_VALUES_MOTOR);
        // Claims table
        SQLiteManager.addTableToDB(Constants.TABLE_NAME_CLAIM, Constants.TABLE_COLUMN_VALUES_CLAIM);
        // Beneficiaries table
        SQLiteManager.addTableToDB(Constants.TABLE_NAME_BENEFICIARY, Constants.TABLE_COLUMN_VALUES_BENEFICIARY);
    }

    public static void setUpTableTriggers() {

    }
}
