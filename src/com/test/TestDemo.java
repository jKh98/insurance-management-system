package com.test;

import com.others.Constants;
import com.sqlite.Inserter;
import com.sqlite.SQLiteManager;
import com.sqlite.Selector;

import java.util.Map;

import static java.util.Map.entry;

public class TestDemo {

    public static void main(String[] args) {
        // 1. Connect to database
        SQLiteManager.setUpConnectionToDB("test.db");
        // 2. Add tables if they do not exist
        setUpTables();
        // 3. Add corresponding table triggers if they do not exist
        setUpTableTriggers();
        // 4. Insert test table values for travel, motor and medical policies
        insertTestPolicyRecords();
        // 5. Select all policy records and print
        selectAllTestPolicyRecords();
        // 5. Close database
//        SQLiteManager.disconnectAndCloseDB();
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

    /**
     * Sets up all database triggers on tables based on design specifications
     */
    private static void setUpTableTriggers() {
        // Travel premium trigger that computes travel policy premium
        SQLiteManager.addTriggerToTable(Constants.TABLE_NAME_TRAVEL, Constants.TRIGGER_TRAVEL_PREMIUM, Constants.SQL_AFTER_INSERT_ON, Constants.TRIGGER_STATEMENTS_TRAVEL_PREMIUM);
        SQLiteManager.addTriggerToTable(Constants.TABLE_NAME_TRAVEL, Constants.TRIGGER_TRAVEL_DELETE, Constants.SQL_AFTER_DELETE_ON, Constants.TRIGGER_STATEMENTS_TRAVEL_DELETE);
        SQLiteManager.addTriggerToTable(Constants.TABLE_NAME_MOTOR, Constants.TRIGGER_MOTOR_PREMIUM, Constants.SQL_AFTER_INSERT_ON, Constants.TRIGGER_STATEMENTS_MOTOR_PREMIUM);
        SQLiteManager.addTriggerToTable(Constants.TABLE_NAME_MOTOR, Constants.TRIGGER_MOTOR_DELETE, Constants.SQL_AFTER_DELETE_ON, Constants.TRIGGER_STATEMENTS_MOTOR_DELETE);
        SQLiteManager.addTriggerToTable(Constants.TABLE_NAME_BENEFICIARY, Constants.TRIGGER_MEDICAL_PREMIUM, Constants.SQL_AFTER_INSERT_ON, Constants.TRIGGER_STATEMENTS_MEDICAL_PREMIUM);
        SQLiteManager.addTriggerToTable(Constants.TABLE_NAME_BENEFICIARY, Constants.TRIGGER_MEDICAL_DELETE, Constants.SQL_AFTER_DELETE_ON, Constants.TRIGGER_STATEMENTS_MEDICAL_DELETE);
    }

    /**
     * Insert test records into database tables by calling specific insert methods
     */
    private static void insertTestPolicyRecords() {
        Inserter.addTravelPolicy(Map.ofEntries(
                entry(Constants.TABLE_COLUMN_EFFECTIVE, System.currentTimeMillis() / 1000L),
                entry(Constants.TABLE_COLUMN_EXPIRY, System.currentTimeMillis() / 1000L + 3 * 86400),
                entry(Constants.TABLE_COLUMN_IS_VALID, 1),
                entry(Constants.TABLE_COLUMN_POLICY_TYPE, "travel"),
                entry(Constants.TABLE_COLUMN_DEPARTURE, "Beirut"),
                entry(Constants.TABLE_COLUMN_DESTINATION, "Paris"),
                entry(Constants.TABLE_COLUMN_FAMILY, 0)
        ));
        Inserter.addMotorPolicy(Map.ofEntries(
                entry(Constants.TABLE_COLUMN_EFFECTIVE, System.currentTimeMillis() / 1000L),
                entry(Constants.TABLE_COLUMN_EXPIRY, System.currentTimeMillis() / 1000L + 100),
                entry(Constants.TABLE_COLUMN_IS_VALID, 1),
                entry(Constants.TABLE_COLUMN_POLICY_TYPE, "motor"),
                entry(Constants.TABLE_COLUMN_VEHICLE_PRICE, 10.0)
        ));
        Inserter.addMedicalPolicy(Map.ofEntries(
                entry(Constants.TABLE_COLUMN_EFFECTIVE, System.currentTimeMillis() / 1000L),
                entry(Constants.TABLE_COLUMN_EXPIRY, System.currentTimeMillis() / 1000L + 100),
                entry(Constants.TABLE_COLUMN_IS_VALID, 1),
                entry(Constants.TABLE_COLUMN_POLICY_TYPE, "medical"),
                Map.entry(Constants.TABLE_COLUMN_NAME, "JIHAD"),
                Map.entry(Constants.TABLE_COLUMN_GENDER, "male"),
                Map.entry(Constants.TABLE_COLUMN_RELATION, "self"),
                Map.entry(Constants.TABLE_COLUMN_BIRTH_DATE, System.currentTimeMillis() / 1000L)));
    }

    /**
     * Selects and prints all records related to policies by calling policy-type specific selector methods
     */
    private static void selectAllTestPolicyRecords() {
        Selector.selectAllTravelPolicies();
        Selector.selectAllMotorPolicies();
        Selector.selectAllMedicalPolicies();
    }
}