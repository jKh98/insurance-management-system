package test;

import sqlite.*;

import static sqlite.Consts.*;

import java.util.HashMap;


/**
 * Contains main methods that sets up database and tests against several inputs based on requirements
 * Contains methods that call inserter and Selector classes to handle inserting and selecting records
 */
public class TestDemo {

    public static void main(String[] args) {
        // 1. Connect to database
        System.out.println("\n##### Connecting to database #####\n");
        SQLiteManager manager = new SQLiteManager("test.db");
        TableAdder tableAdder = new TableAdder(manager);
        TriggerAdder triggerAdder = new TriggerAdder(manager);
        Inserter inserter = new Inserter(manager);
        Selector selector = new Selector(manager);
        // 2. Add tables if they do not exist
        System.out.println("\n##### Setting up tables #####\n");
        setUpTables(tableAdder);
        // 3. Add corresponding table triggers if they do not exist
        System.out.println("\n##### Adding table triggers #####\n");
        setUpTableTriggers(triggerAdder);
        // 4. Insert test table values for travel, motor and medical policies
        System.out.println("\n##### Inserting test travel, motor and medical policies records #####\n");
        insertTestPolicyRecords(inserter);
        // 5. Select all policy records and print
        System.out.println("\n##### Printing test travel, motor and medical policies records #####\n");
        selectTestPolicyRecords(selector);
        // 6. Insert test claim records
        System.out.println("\n##### Inserting test claims records #####\n");
        insertTestClaimRecords(inserter);
        // 7. Select claim related data for each policy
        System.out.println("\n##### Printing claims data per policy #####\n");
        selector.selectPoliciesClaimsData();
        // 7. Close database
        System.out.println("\n##### Disconnecting to database #####\n");
        manager.disconnectAndCloseDB();
    }

    /**
     * Sets up all database tables based on designed schema
     *
     * @param tableAdder table adder helper
     */
    private static void setUpTables(TableAdder tableAdder) {

        // 1. Policy table that stores all policies regardless what type
        tableAdder.addPolicyTable();
        // 2. Table that stores data related to travel policies
        tableAdder.addTravelTable();
        // 3. Table that stores data related to motor policies
        tableAdder.addMotorTable();
        // 4. Beneficiaries table
        tableAdder.addBeneficiaryTable();
        // 5. Claims table
        tableAdder.addClaimTable();
    }

    /**
     * Sets up all database triggers on tables based on design specifications
     *
     * @param triggerAdder trigger adder helper
     */
    private static void setUpTableTriggers(TriggerAdder triggerAdder) {

        // 1. Travel premium trigger that computes travel policy premium
        triggerAdder.addTravelPremiumTrigger();
        // 2. Travel validate trigger that checks if new policy is valid
        triggerAdder.addTravelValidationTrigger();
        // 3. Travel delete trigger that deletes travel policy from policy table
        triggerAdder.addTravelDeletionTrigger();
        // 4. Motor premium trigger that computes motor policy premium
        triggerAdder.addMotorPremiumTrigger();
        // 5. Motor validate trigger that checks if new policy is valid
        triggerAdder.addMotorValidationTrigger();
        // 6. Motor delete trigger that deletes motor policy from policy table
        triggerAdder.addMotorDeletionTrigger();
        // 7. Medical premium trigger that computes medical policy premium
        triggerAdder.addMedicalPremiumTrigger();
        // 8. Medical validate trigger that checks if new policy is valid
        triggerAdder.addMedicalValidationTrigger();
        // 9. Medical delete trigger that deletes travel policy from policy table
        triggerAdder.addMedicalDeletionTrigger();
        // 10. Medical self trigger that makes sure there is one self per policy_no
        triggerAdder.addMedicalOneSelfTrigger();
        // 11. Claim abort trigger that makes sure incurred date is within expiry and effective and that policy number is valid
        triggerAdder.addClaimAbortTrigger();
    }

    /**
     * Insert test records into database tables by calling specific insert methods
     *
     * @param inserter insert helper class
     */
    private static void insertTestPolicyRecords(Inserter inserter) {

        // 1. Insert Travel policy 1 (successful)
        inserter.addTravelPolicy(new HashMap<>() {
            {
                put(TABLE_COLUMN_EFFECTIVE, TestValues.TRAVEL_1_EFFECTIVE);
                put(TABLE_COLUMN_EXPIRY, TestValues.TRAVEL_1_EXPIRY);
                put(TABLE_COLUMN_DEPARTURE, TestValues.TRAVEL_1_DEPARTURE);
                put(TABLE_COLUMN_DESTINATION, TestValues.TRAVEL_1_DESTINATION);
                put(TABLE_COLUMN_FAMILY, TestValues.TRAVEL_1_FAMILY);
            }
        });

        // 2. Insert Motor policy 1 (successful)
        inserter.addMotorPolicy(new HashMap<>() {{
            put(TABLE_COLUMN_EFFECTIVE, TestValues.MOTOR_1_EFFECTIVE);
            put(TABLE_COLUMN_EXPIRY, TestValues.MOTOR_1_EXPIRY);
            put(TABLE_COLUMN_VEHICLE_PRICE, TestValues.MOTOR_1_VEHICLE_PRICE);
        }});

        // 3. Insert Medical policy 1 (successful)
        inserter.addMedicalPolicy(new HashMap<>() {{
            put(TABLE_COLUMN_EFFECTIVE, TestValues.MEDICAL_1_EFFECTIVE);
            put(TABLE_COLUMN_EXPIRY, TestValues.MEDICAL_1_EXPIRY);
            put(TABLE_COLUMN_NAME, TestValues.MEDICAL_1_NAME);
            put(TABLE_COLUMN_GENDER, TestValues.MEDICAL_1_GENDER);
            put(TABLE_COLUMN_RELATION, TestValues.MEDICAL_1_RELATION);
            put(TABLE_COLUMN_BIRTH_DATE, TestValues.MEDICAL_1_BIRTH_DATE);
        }});

        // 4. Insert Travel policy 2 (successful)
        inserter.addTravelPolicy(new HashMap<>() {{
            put(TABLE_COLUMN_EFFECTIVE, TestValues.TRAVEL_2_EFFECTIVE);
            put(TABLE_COLUMN_EXPIRY, TestValues.TRAVEL_2_EXPIRY);
            put(TABLE_COLUMN_DEPARTURE, TestValues.TRAVEL_2_DEPARTURE);
            put(TABLE_COLUMN_DESTINATION, TestValues.TRAVEL_2_DESTINATION);
            put(TABLE_COLUMN_FAMILY, TestValues.TRAVEL_2_FAMILY);
        }});

        // 5. Insert Motor policy 2 (successful)
        inserter.addMotorPolicy(new HashMap<>() {
            {
                put(TABLE_COLUMN_EFFECTIVE, TestValues.MOTOR_2_EFFECTIVE);
                put(TABLE_COLUMN_EXPIRY, TestValues.MOTOR_2_EXPIRY);
                put(TABLE_COLUMN_VEHICLE_PRICE, TestValues.MOTOR_2_VEHICLE_PRICE);
            }
        });

        // 6. Insert Medical policy 2 (successful)
        inserter.addMedicalPolicy(new HashMap<>() {{
            put(TABLE_COLUMN_EFFECTIVE, TestValues.MEDICAL_2_EFFECTIVE);
            put(TABLE_COLUMN_EXPIRY, TestValues.MEDICAL_2_EXPIRY);
            put(TABLE_COLUMN_NAME, TestValues.MEDICAL_2_NAME);
            put(TABLE_COLUMN_GENDER, TestValues.MEDICAL_2_GENDER);
            put(TABLE_COLUMN_RELATION, TestValues.MEDICAL_2_RELATION);
            put(TABLE_COLUMN_BIRTH_DATE, TestValues.MEDICAL_2_BIRTH_DATE);
        }});

        // 7. Insert Travel policy 3 (failure)
        inserter.addTravelPolicy(new HashMap<>() {
            {
                put(TABLE_COLUMN_EFFECTIVE, TestValues.TRAVEL_3_EFFECTIVE);
                put(TABLE_COLUMN_EXPIRY, TestValues.TRAVEL_3_EXPIRY);
                put(TABLE_COLUMN_DEPARTURE, TestValues.TRAVEL_3_DEPARTURE);
                put(TABLE_COLUMN_DESTINATION, TestValues.TRAVEL_3_DESTINATION);
                put(TABLE_COLUMN_FAMILY, TestValues.TRAVEL_3_FAMILY);
            }
        });

        // 8. Insert Medical policy 3 (failure)
        inserter.addMedicalPolicy(new HashMap<>() {{
            put(TABLE_COLUMN_EFFECTIVE, TestValues.MEDICAL_3_EFFECTIVE);
            put(TABLE_COLUMN_EXPIRY, TestValues.MEDICAL_3_EXPIRY);
            put(TABLE_COLUMN_NAME, TestValues.MEDICAL_3_NAME);
            put(TABLE_COLUMN_GENDER, TestValues.MEDICAL_3_GENDER);
            put(TABLE_COLUMN_RELATION, TestValues.MEDICAL_3_RELATION);
            put(TABLE_COLUMN_BIRTH_DATE, TestValues.MEDICAL_3_BIRTH_DATE);
        }});

    }

    /**
     * Selects and prints records related to policies based on requirements by calling policy-type specific selector methods
     *
     * @param selector select helper class
     */
    private static void selectTestPolicyRecords(Selector selector) {
        // 1. select and print all travel policies
        selector.selectAllTravelPolicies();
        // 2. select and print all motor policies
        selector.selectAllMotorPolicies();
        // 3. select and print all medical policies
        selector.selectAllMedicalPolicies();
        // 4. select and print all policies with premium between 500 and 2000
        selector.selectPoliciesPremiumRange(500, 20000);
    }

    /**
     * Inserts test claim records into claim table
     */
    private static void insertTestClaimRecords(Inserter inserter) {
        // Insert 15 claims claim
        for (int i = 0; i < 15; i++) {
            int finalI = i;
            inserter.addClaim(new HashMap<>() {{
                put(TABLE_COLUMN_POLICY_NO, TestValues.CLAIM_ALL_POLICY_NO[finalI]);
                put(TABLE_COLUMN_INCURRED_DATE, TestValues.CLAIM_ALL_INCURRED[finalI]);
                put(TABLE_COLUMN_CLAIMED_AMOUNT, TestValues.CLAIM_ALL_CLAIMED_AMOUNT[finalI]);
            }});
        }
    }
}
