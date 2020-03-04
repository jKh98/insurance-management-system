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
        SQLiteManager manager = new SQLiteManager("test.db");
        TableAdder tableAdder = new TableAdder(manager);
        TriggerAdder triggerAdder = new TriggerAdder(manager);
        Inserter inserter = new Inserter(manager);
        Selector selector = new Selector(manager);
        // 2. Add tables if they do not exist
        setUpTables(tableAdder);
        // 3. Add corresponding table triggers if they do not exist
        setUpTableTriggers(manager);
        // 4. Insert test table values for travel, motor and medical policies
        insertTestPolicyRecords(inserter);
        // 5. Select all policy records and print
        selectTestPolicyRecords(selector);
        // 6. Insert test claim records
        insertTestClaimRecords(inserter);
        // 7. Select claim related data for each policy
        selector.selectPoliciesClaimsData();
        // 7. Close database
        manager.disconnectAndCloseDB();
    }

    /**
     * Sets up all database tables based on designed schema
     *
     * @param tableAdder
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
     * @param manager
     */
    private static void setUpTableTriggers(SQLiteManager manager) {

        // 1. Travel premium trigger that computes travel policy premium
        // CREATE TRIGGER IF NOT EXISTS travel_premium
        //      AFTER INSERT ON travel BEGIN
        //          UPDATE policy SET premium =
        //          CASE WHEN NEW.family = 1 THEN 10*(expiry - effective)/86400
        //               WHEN NEW.family = 0 THEN 5*(expiry - effective)/86400 END
        //          WHERE policy_no = NEW.policy_no;
        //      END
        manager.addTriggerToTable(TABLE_NAME_TRAVEL, TRIGGER_TRAVEL_PREMIUM, SQL_AFTER_INSERT_ON, TRIGGER_STATEMENTS_TRAVEL_PREMIUM);

        // 2. Travel validate trigger that checks if new policy is valid
        //
        manager.addTriggerToTable(TABLE_NAME_TRAVEL, TRIGGER_TRAVEL_VALIDATE, SQL_AFTER_INSERT_ON, TRIGGER_STATEMENTS_TRAVEL_VALIDATE);

        // 3. Travel delete trigger that deletes travel policy from policy table
        // CREATE TRIGGER IF NOT EXISTS travel_delete
        //      AFTER DELETE ON travel BEGIN
        //          DELETE FROM policy WHERE policy_no = OLD.policy_no;
        //      END
        manager.addTriggerToTable(TABLE_NAME_TRAVEL, TRIGGER_TRAVEL_DELETE, SQL_AFTER_DELETE_ON, TRIGGER_STATEMENTS_TRAVEL_DELETE);

        // 4. Motor premium trigger that computes motor policy premium
        // CREATE TRIGGER IF NOT EXISTS motor_premium
        //      AFTER INSERT ON motor BEGIN
        //          UPDATE policy SET premium = 0.2*NEW.vehicle_price WHERE policy_no = NEW.policy_no;
        //      END
        manager.addTriggerToTable(TABLE_NAME_MOTOR, TRIGGER_MOTOR_PREMIUM, SQL_AFTER_INSERT_ON, TRIGGER_STATEMENTS_MOTOR_PREMIUM);

        // 5. Motor validate trigger that checks if new policy is valid
        //
        manager.addTriggerToTable(TABLE_NAME_MOTOR, TRIGGER_MOTOR_VALIDATE, SQL_AFTER_INSERT_ON, TRIGGER_STATEMENTS_MOTOR_VALIDATE);

        // 6. Motor delete trigger that deletes motor policy from policy table
        // CREATE TRIGGER IF NOT EXISTS motor_delete
        //      AFTER DELETE ON motor BEGIN
        //          DELETE FROM policy WHERE policy_no = OLD.policy_no;
        //      END
        manager.addTriggerToTable(TABLE_NAME_MOTOR, TRIGGER_MOTOR_DELETE, SQL_AFTER_DELETE_ON, TRIGGER_STATEMENTS_MOTOR_DELETE);

        // 7. Medical premium trigger that computes medical policy premium
        // CREATE TRIGGER IF NOT EXISTS medical_premium
        //      AFTER INSERT ON beneficiary BEGIN
        //          UPDATE policy SET premium = (
        //              SELECT SUM(
        //                  CASE WHEN (STRFTIME('%Y','now') - STRFTIME('%Y',datetime(T.birth_date, 'unixepoch'))) < 10 THEN 15
        //                       WHEN (STRFTIME('%Y','now') - STRFTIME('%Y',datetime(T.birth_date, 'unixepoch'))) BETWEEN 11 AND 45 THEN 30
        //                       WHEN (STRFTIME('%Y','now') - STRFTIME('%Y',datetime(T.birth_date, 'unixepoch'))) > 45 THEN 45  END)
        //              FROM beneficiary AS T
        //              WHERE T.policy_no = NEW.policy_no)
        //          WHERE policy_no = NEW.policy_no;
        //      END
        manager.addTriggerToTable(TABLE_NAME_BENEFICIARY, TRIGGER_MEDICAL_PREMIUM, SQL_AFTER_INSERT_ON, TRIGGER_STATEMENTS_MEDICAL_PREMIUM);

        // 8. Medical validate trigger that checks if new policy is valid
        //
        manager.addTriggerToTable(TABLE_NAME_BENEFICIARY, TRIGGER_MEDICAL_VALIDATE, SQL_AFTER_INSERT_ON, TRIGGER_STATEMENTS_MEDICAL_VALIDATE);

        // 9. Medical delete trigger that deletes travel policy from policy table
        // CREATE TRIGGER IF NOT EXISTS medical_delete
        //      AFTER DELETE ON beneficiary BEGIN
        //          DELETE FROM policy WHERE policy_no = OLD.policy_no AND
        //              (SELECT COUNT(*) FROM beneficiary as T WHERE T.policy_no = OLD.policy_no) < 1;
        //      END
        manager.addTriggerToTable(TABLE_NAME_BENEFICIARY, TRIGGER_MEDICAL_DELETE, SQL_AFTER_DELETE_ON, TRIGGER_STATEMENTS_MEDICAL_DELETE);

        // 10. Medical self trigger that makes sure there is one self per policy_no
        // CREATE TRIGGER IF NOT EXISTS medical_self
        //      BEFORE INSERT ON beneficiary BEGIN
        //          SELECT
        //              CASE WHEN  NEW.relation = 'self' AND
        //                  (SELECT COUNT(*) FROM beneficiary WHERE beneficiary.policy_no = NEW.policy_no AND beneficiary.relation = 'self')>0
        //              THEN RAISE (ABORT, 'Can only have one beneficiary as self')
        //              END;
        //          END;
        //      END
        manager.addTriggerToTable(TABLE_NAME_BENEFICIARY, TRIGGER_MEDICAL_ONE_SELF, SQL_BEFORE_INSERT_ON, TRIGGER_STATEMENTS_MEDICAL_SELF);

        // 11. Claim abort trigger that makes sure incurred date is within expiry and effective and that policy number is valid
        // CREATE TRIGGER IF NOT EXISTS claim_abort
        //      BEFORE INSERT ON claim BEGIN
        //          SELECT CASE WHEN  NEW.incurred_date NOT BETWEEN (SELECT effective FROM policy WHERE policy_no = NEW.policy_no ) AND
        //                          (SELECT expiry FROM policy WHERE policy_no = NEW.policy_no )
        //                          THEN RAISE (ABORT, 'Claim is rejected because Policy# is inactive or expired!')
        //                      WHEN NOT EXISTS (SELECT policy_no FROM policy WHERE policy_no = NEW.policy_no)
        //                          THEN RAISE (ABORT, 'Cannot submit a claim for Policy#  because it does not exist!')
        //                  END;
        //           END;
        //      END
        manager.addTriggerToTable(TABLE_NAME_CLAIM, TRIGGER_CLAIM_ABORT, SQL_BEFORE_INSERT_ON, TRIGGER_STATEMENTS_CLAIM_ABORT);
    }

    /**
     * Insert test records into database tables by calling specific insert methods
     *
     * @param inserter
     */
    private static void insertTestPolicyRecords(Inserter inserter) {

        // 1. Insert Travel policy 1 (successful)
        inserter.addTravelPolicy(new HashMap<String, Object>() {
            {
                put(TABLE_COLUMN_EFFECTIVE, TestValues.TRAVEL_1_EFFECTIVE);
                put(TABLE_COLUMN_EXPIRY, TestValues.TRAVEL_1_EXPIRY);
                put(TABLE_COLUMN_DEPARTURE, TestValues.TRAVEL_1_DEPARTURE);
                put(TABLE_COLUMN_DESTINATION, TestValues.TRAVEL_1_DESTINATION);
                put(TABLE_COLUMN_FAMILY, TestValues.TRAVEL_1_FAMILY);
            }
        });

        // 2. Insert Motor policy 1 (successful)
        inserter.addMotorPolicy(new HashMap<String, Object>() {{
            put(TABLE_COLUMN_EFFECTIVE, TestValues.MOTOR_1_EFFECTIVE);
            put(TABLE_COLUMN_EXPIRY, TestValues.MOTOR_1_EXPIRY);
            put(TABLE_COLUMN_VEHICLE_PRICE, TestValues.MOTOR_1_VEHICLE_PRICE);
        }});

        // 3. Insert Medical policy 1 (successful)
        inserter.addMedicalPolicy(new HashMap<String, Object>() {{
            put(TABLE_COLUMN_EFFECTIVE, TestValues.MEDICAL_1_EFFECTIVE);
            put(TABLE_COLUMN_EXPIRY, TestValues.MEDICAL_1_EXPIRY);
            put(TABLE_COLUMN_NAME, TestValues.MEDICAL_1_NAME);
            put(TABLE_COLUMN_GENDER, TestValues.MEDICAL_1_GENDER);
            put(TABLE_COLUMN_RELATION, TestValues.MEDICAL_1_RELATION);
            put(TABLE_COLUMN_BIRTH_DATE, TestValues.MEDICAL_1_BIRTH_DATE);
        }});

        // 4. Insert Travel policy 2 (successful)
        inserter.addTravelPolicy(new HashMap<String, Object>() {{
            put(TABLE_COLUMN_EFFECTIVE, TestValues.TRAVEL_2_EFFECTIVE);
            put(TABLE_COLUMN_EXPIRY, TestValues.TRAVEL_2_EXPIRY);
            put(TABLE_COLUMN_DEPARTURE, TestValues.TRAVEL_2_DEPARTURE);
            put(TABLE_COLUMN_DESTINATION, TestValues.TRAVEL_2_DESTINATION);
            put(TABLE_COLUMN_FAMILY, TestValues.TRAVEL_2_FAMILY);
        }});

        // 5. Insert Motor policy 2 (successful)
        inserter.addMotorPolicy(new HashMap<String, Object>() {
            {
                put(TABLE_COLUMN_EFFECTIVE, TestValues.MOTOR_2_EFFECTIVE);
                put(TABLE_COLUMN_EXPIRY, TestValues.MOTOR_2_EXPIRY);
                put(TABLE_COLUMN_VEHICLE_PRICE, TestValues.MOTOR_2_VEHICLE_PRICE);
            }
        });

        // 6. Insert Medical policy 2 (successful)
        inserter.addMedicalPolicy(new HashMap<String, Object>() {{
            put(TABLE_COLUMN_EFFECTIVE, TestValues.MEDICAL_2_EFFECTIVE);
            put(TABLE_COLUMN_EXPIRY, TestValues.MEDICAL_2_EXPIRY);
            put(TABLE_COLUMN_NAME, TestValues.MEDICAL_2_NAME);
            put(TABLE_COLUMN_GENDER, TestValues.MEDICAL_2_GENDER);
            put(TABLE_COLUMN_RELATION, TestValues.MEDICAL_2_RELATION);
            put(TABLE_COLUMN_BIRTH_DATE, TestValues.MEDICAL_2_BIRTH_DATE);
        }});

        // 7. Insert Travel policy 3 (failure)
        inserter.addTravelPolicy(new HashMap<String, Object>() {
            {
                put(TABLE_COLUMN_EFFECTIVE, TestValues.TRAVEL_3_EFFECTIVE);
                put(TABLE_COLUMN_EXPIRY, TestValues.TRAVEL_3_EXPIRY);
                put(TABLE_COLUMN_DEPARTURE, TestValues.TRAVEL_3_DEPARTURE);
                put(TABLE_COLUMN_DESTINATION, TestValues.TRAVEL_3_DESTINATION);
                put(TABLE_COLUMN_FAMILY, TestValues.TRAVEL_3_FAMILY);
            }
        });

        // 8. Insert Medical policy 3 (failure)
        inserter.addMedicalPolicy(new HashMap<String, Object>() {{
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
     * @param selector
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
            inserter.addClaim(new HashMap<String, Object>() {{
                put(TABLE_COLUMN_POLICY_NO, TestValues.CLAIM_ALL_POLICY_NO[finalI]);
                put(TABLE_COLUMN_INCURRED_DATE, TestValues.CLAIM_ALL_INCURRED[finalI]);
                put(TABLE_COLUMN_CLAIMED_AMOUNT, TestValues.CLAIM_ALL_CLAIMED_AMOUNT[finalI]);
            }});
        }
    }
}
