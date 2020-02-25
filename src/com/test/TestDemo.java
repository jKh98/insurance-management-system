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
        selectTestPolicyRecords();
        // 5. Close database
        SQLiteManager.disconnectAndCloseDB();
    }

    /**
     * Sets up all database tables based on designed schema
     */
    private static void setUpTables() {
        // 1. Policy table that stores all policies regardless what type
        SQLiteManager.addTableToDB(Constants.TABLE_NAME_POLICY, Constants.TABLE_COLUMN_VALUES_POLICY);
        // 2. Table that stores data related to travel policies
        SQLiteManager.addTableToDB(Constants.TABLE_NAME_TRAVEL, Constants.TABLE_COLUMN_VALUES_TRAVEL);
        // 3. Table that stores data related to motor policies
        SQLiteManager.addTableToDB(Constants.TABLE_NAME_MOTOR, Constants.TABLE_COLUMN_VALUES_MOTOR);
        // 4. Claims table
        SQLiteManager.addTableToDB(Constants.TABLE_NAME_CLAIM, Constants.TABLE_COLUMN_VALUES_CLAIM);
        // 5. Beneficiaries table
        SQLiteManager.addTableToDB(Constants.TABLE_NAME_BENEFICIARY, Constants.TABLE_COLUMN_VALUES_BENEFICIARY);
    }

    /**
     * Sets up all database triggers on tables based on design specifications
     */
    private static void setUpTableTriggers() {
        // 1. Travel premium trigger that computes travel policy premium
        SQLiteManager.addTriggerToTable(Constants.TABLE_NAME_TRAVEL, Constants.TRIGGER_TRAVEL_PREMIUM, Constants.SQL_AFTER_INSERT_ON, Constants.TRIGGER_STATEMENTS_TRAVEL_PREMIUM);
        // 2. Travel delete trigger that deletes travel policy from policy table
        SQLiteManager.addTriggerToTable(Constants.TABLE_NAME_TRAVEL, Constants.TRIGGER_TRAVEL_DELETE, Constants.SQL_AFTER_DELETE_ON, Constants.TRIGGER_STATEMENTS_TRAVEL_DELETE);
        // 3. Motor premium trigger that computes motor policy premium
        SQLiteManager.addTriggerToTable(Constants.TABLE_NAME_MOTOR, Constants.TRIGGER_MOTOR_PREMIUM, Constants.SQL_AFTER_INSERT_ON, Constants.TRIGGER_STATEMENTS_MOTOR_PREMIUM);
        // 4. Motor delete trigger that deletes motor policy from policy table
        SQLiteManager.addTriggerToTable(Constants.TABLE_NAME_MOTOR, Constants.TRIGGER_MOTOR_DELETE, Constants.SQL_AFTER_DELETE_ON, Constants.TRIGGER_STATEMENTS_MOTOR_DELETE);
        // 5. Medical premium trigger that computes medical policy premium
        SQLiteManager.addTriggerToTable(Constants.TABLE_NAME_BENEFICIARY, Constants.TRIGGER_MEDICAL_PREMIUM, Constants.SQL_AFTER_INSERT_ON, Constants.TRIGGER_STATEMENTS_MEDICAL_PREMIUM);
        // 6. Medical delete trigger that deletes travel policy from policy table
        SQLiteManager.addTriggerToTable(Constants.TABLE_NAME_BENEFICIARY, Constants.TRIGGER_MEDICAL_DELETE, Constants.SQL_AFTER_DELETE_ON, Constants.TRIGGER_STATEMENTS_MEDICAL_DELETE);
        // 7. Medical self trigger that makes sure there is one self per policy_no
        SQLiteManager.addTriggerToTable(Constants.TABLE_NAME_BENEFICIARY, Constants.TRIGGER_MEDICAL_ONE_SELF, Constants.SQL_BEFORE_INSERT_ON, Constants.TRIGGER_STATEMENTS_MEDICAL_SELF);
    }

    /**
     * Insert test records into database tables by calling specific insert methods
     */
    private static void insertTestPolicyRecords() {
        // 1. Insert Travel policy 1 (successful)
        Inserter.addTravelPolicy(Map.ofEntries(
                entry(Constants.TABLE_COLUMN_EFFECTIVE, TestValues.TRAVEL_1_EFFECTIVE),
                entry(Constants.TABLE_COLUMN_EXPIRY, TestValues.TRAVEL_1_EXPIRY),
                entry(Constants.TABLE_COLUMN_IS_VALID, TestValues.TRAVEL_1_IS_VALID),
                entry(Constants.TABLE_COLUMN_DEPARTURE, TestValues.TRAVEL_1_DEPARTURE),
                entry(Constants.TABLE_COLUMN_DESTINATION, TestValues.TRAVEL_1_DESTINATION),
                entry(Constants.TABLE_COLUMN_FAMILY, TestValues.TRAVEL_1_FAMILY)
        ));

        // 2. Insert Motor policy 1 (successful)
        Inserter.addMotorPolicy(Map.ofEntries(
                entry(Constants.TABLE_COLUMN_EFFECTIVE, TestValues.MOTOR_1_EFFECTIVE),
                entry(Constants.TABLE_COLUMN_EXPIRY, TestValues.MOTOR_1_EXPIRY),
                entry(Constants.TABLE_COLUMN_IS_VALID, TestValues.MOTOR_1_IS_VALID),
                entry(Constants.TABLE_COLUMN_VEHICLE_PRICE, TestValues.MOTOR_1_VEHICLE_PRICE)
        ));

        // 3. Insert Medical policy 1 (successful)
        Inserter.addMedicalPolicy(Map.ofEntries(
                entry(Constants.TABLE_COLUMN_EFFECTIVE, TestValues.MEDICAL_1_EFFECTIVE),
                entry(Constants.TABLE_COLUMN_EXPIRY, TestValues.MEDICAL_1_EXPIRY),
                entry(Constants.TABLE_COLUMN_IS_VALID, TestValues.MEDICAL_1_IS_VALID),
                Map.entry(Constants.TABLE_COLUMN_NAME, TestValues.MEDICAL_1_NAME),
                Map.entry(Constants.TABLE_COLUMN_GENDER, TestValues.MEDICAL_1_GENDER),
                Map.entry(Constants.TABLE_COLUMN_RELATION, TestValues.MEDICAL_1_RELATION),
                Map.entry(Constants.TABLE_COLUMN_BIRTH_DATE, TestValues.MEDICAL_1_BIRTH_DATE)));

        // 4. Insert Travel policy 2 (successful)
        Inserter.addTravelPolicy(Map.ofEntries(
                entry(Constants.TABLE_COLUMN_EFFECTIVE, TestValues.TRAVEL_2_EFFECTIVE),
                entry(Constants.TABLE_COLUMN_EXPIRY, TestValues.TRAVEL_2_EXPIRY),
                entry(Constants.TABLE_COLUMN_IS_VALID, TestValues.TRAVEL_2_IS_VALID),
                entry(Constants.TABLE_COLUMN_DEPARTURE, TestValues.TRAVEL_2_DEPARTURE),
                entry(Constants.TABLE_COLUMN_DESTINATION, TestValues.TRAVEL_2_DESTINATION),
                entry(Constants.TABLE_COLUMN_FAMILY, TestValues.TRAVEL_2_FAMILY)
        ));

        // 5. Insert Motor policy 2 (successful)
        Inserter.addMotorPolicy(Map.ofEntries(
                entry(Constants.TABLE_COLUMN_EFFECTIVE, TestValues.MOTOR_2_EFFECTIVE),
                entry(Constants.TABLE_COLUMN_EXPIRY, TestValues.MOTOR_2_EXPIRY),
                entry(Constants.TABLE_COLUMN_IS_VALID, TestValues.MOTOR_2_IS_VALID),
                entry(Constants.TABLE_COLUMN_VEHICLE_PRICE, TestValues.MOTOR_2_VEHICLE_PRICE)
        ));

        // 6. Insert Medical policy 2 (successful)
        Inserter.addMedicalPolicy(Map.ofEntries(
                entry(Constants.TABLE_COLUMN_EFFECTIVE, TestValues.MEDICAL_2_EFFECTIVE),
                entry(Constants.TABLE_COLUMN_EXPIRY, TestValues.MEDICAL_2_EXPIRY),
                entry(Constants.TABLE_COLUMN_IS_VALID, TestValues.MEDICAL_2_IS_VALID),
                Map.entry(Constants.TABLE_COLUMN_NAME, TestValues.MEDICAL_2_NAME),
                Map.entry(Constants.TABLE_COLUMN_GENDER, TestValues.MEDICAL_2_GENDER),
                Map.entry(Constants.TABLE_COLUMN_RELATION, TestValues.MEDICAL_2_RELATION),
                Map.entry(Constants.TABLE_COLUMN_BIRTH_DATE, TestValues.MEDICAL_2_BIRTH_DATE)));

        // 7. Insert Travel policy 3 (failure)
        Inserter.addTravelPolicy(Map.ofEntries(
                entry(Constants.TABLE_COLUMN_EFFECTIVE, TestValues.TRAVEL_3_EFFECTIVE),
                entry(Constants.TABLE_COLUMN_EXPIRY, TestValues.TRAVEL_3_EXPIRY),
                entry(Constants.TABLE_COLUMN_IS_VALID, TestValues.TRAVEL_3_IS_VALID),
                entry(Constants.TABLE_COLUMN_DEPARTURE, TestValues.TRAVEL_3_DEPARTURE),
                entry(Constants.TABLE_COLUMN_DESTINATION, TestValues.TRAVEL_3_DESTINATION),
                entry(Constants.TABLE_COLUMN_FAMILY, TestValues.TRAVEL_3_FAMILY)
        ));

        // 8. Insert Medical policy 3 (failure)
        Inserter.addMedicalPolicy(Map.ofEntries(
                entry(Constants.TABLE_COLUMN_EFFECTIVE, TestValues.MEDICAL_3_EFFECTIVE),
                entry(Constants.TABLE_COLUMN_EXPIRY, TestValues.MEDICAL_3_EXPIRY),
                entry(Constants.TABLE_COLUMN_IS_VALID, TestValues.MEDICAL_3_IS_VALID),
                Map.entry(Constants.TABLE_COLUMN_NAME, TestValues.MEDICAL_3_NAME),
                Map.entry(Constants.TABLE_COLUMN_GENDER, TestValues.MEDICAL_3_GENDER),
                Map.entry(Constants.TABLE_COLUMN_RELATION, TestValues.MEDICAL_3_RELATION),
                Map.entry(Constants.TABLE_COLUMN_BIRTH_DATE, TestValues.MEDICAL_3_BIRTH_DATE)));

    }

    /**
     * Selects and prints records related to policies based on requirements by calling policy-type specific selector methods
     */
    private static void selectTestPolicyRecords() {
        // 1. select and print all travel policies
        Selector.selectAllTravelPolicies();
        // 2. select and print all motor policies
        Selector.selectAllMotorPolicies();
        // 3. select and print all medical policies
        Selector.selectAllMedicalPolicies();
        // 4. select and print all policies with premium between 500 and 2000
        Selector.selectPoliciesPremiumRange(500,20000);
    }
}
