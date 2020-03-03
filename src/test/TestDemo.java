package test;

import sqlite.Consts;
import sqlite.Inserter;
import sqlite.SQLiteManager;
import sqlite.Selector;

import java.util.HashMap;


/**
 * Contains main methods that sets up database and tests against several inputs based on requirements
 * Contains methods that call inserter and Selector classes to handle inserting and selecting records
 */
public class TestDemo {

    public static void main(String[] args) {
        // 1. Connect to database
        SQLiteManager manager = new SQLiteManager("test.db");
        Inserter inserter = new Inserter(manager);
        Selector selector = new Selector(manager);
        // 2. Add tables if they do not exist
        setUpTables(manager);
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
     * @param manager
     */
    private static void setUpTables(SQLiteManager manager) {

        // 1. Policy table that stores all policies regardless what type
        // CREATE TABLE IF NOT EXISTS policy(
        //      id INTEGER PRIMARY KEY AUTOINCREMENT,
        //      effective BIGINT NOT NULL,
        //      expiry BIGINT NOT NULL CHECK (expiry > effective),
        //      premium DECIMAL NOT NULL,
        //      policy_type TEXT NOT NULL CHECK ((LOWER(policy_type) = 'travel' AND ((expiry - effective)/86400 <= 30)) OR LOWER(policy_type) = 'motor' OR LOWER(policy_type) = 'medical'),
        //      policy_no TEXT UNIQUE NOT NULL GENERATED ALWAYS AS ( STRFTIME('%Y',datetime(effective, 'unixepoch')) || '-' || policy_type || '-' || id ) STORED)
        manager.addTableToDB(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_VALUES_POLICY);

        // 2. Table that stores data related to travel policies
        // CREATE TABLE IF NOT EXISTS travel(
        //      id INTEGER PRIMARY KEY AUTOINCREMENT,
        //      policy_no TEXT NOT NULL UNIQUE CHECK ( policy_no LIKE '%travel%'),
        //      departure TEXT NOT NULL,destination TEXT NOT NULL,
        //      family INTEGER NOT NULL CHECK (family IN (0,1)),
        //      CONSTRAINT fk_policy_no FOREIGN KEY  (policy_no) REFERENCES policy(policy_no) ON DELETE CASCADE)
        manager.addTableToDB(Consts.TABLE_NAME_TRAVEL, Consts.TABLE_COLUMN_VALUES_TRAVEL);

        // 3. Table that stores data related to motor policies
        // CREATE TABLE IF NOT EXISTS motor(
        //      id INTEGER PRIMARY KEY AUTOINCREMENT,
        //      policy_no TEXT NOT NULL UNIQUE CHECK ( policy_no LIKE '%motor%') ,
        //      vehicle_price DECIMAL NOT NULL,
        //      CONSTRAINT fk_policy_no FOREIGN KEY  (policy_no) REFERENCES policy(policy_no) ON DELETE CASCADE)
        manager.addTableToDB(Consts.TABLE_NAME_MOTOR, Consts.TABLE_COLUMN_VALUES_MOTOR);

        // 4. Claims table
        // CREATE TABLE IF NOT EXISTS claim(
        //      id INTEGER PRIMARY KEY AUTOINCREMENT,
        //      policy_no TEXT NOT NULL,
        //      incurred_date BIGINT NOT NULL,
        //      claimed_amount DECIMAL NOT NULL,
        //      CONSTRAINT fk_policy_no FOREIGN KEY  (policy_no) REFERENCES policy(policy_no) ON DELETE CASCADE)
        manager.addTableToDB(Consts.TABLE_NAME_CLAIM, Consts.TABLE_COLUMN_VALUES_CLAIM);

        // 5. Beneficiaries table
        // CREATE TABLE IF NOT EXISTS beneficiary(
        //      id INTEGER PRIMARY KEY AUTOINCREMENT,
        //      name TEXT NOT NULL,
        //      relation TEXT NOT NULL CHECK (LOWER(relation) IN ('self','spouse','son','daughter')),
        //      gender TEXT NOT NULL CHECK (LOWER(gender) IN ('male','female')),
        //      birth_date BIGINT NOT NULL,policy_no TEXT NOT NULL CHECK ( policy_no LIKE '%medical%'),
        //      CONSTRAINT fk_policy_no FOREIGN KEY  (policy_no) REFERENCES policy(policy_no) ON DELETE CASCADE)
        manager.addTableToDB(Consts.TABLE_NAME_BENEFICIARY, Consts.TABLE_COLUMN_VALUES_BENEFICIARY);
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
        manager.addTriggerToTable(Consts.TABLE_NAME_TRAVEL, Consts.TRIGGER_TRAVEL_PREMIUM, Consts.SQL_AFTER_INSERT_ON, Consts.TRIGGER_STATEMENTS_TRAVEL_PREMIUM);

        // 2. Travel delete trigger that deletes travel policy from policy table
        // CREATE TRIGGER IF NOT EXISTS travel_delete
        //      AFTER DELETE ON travel BEGIN
        //          DELETE FROM policy WHERE policy_no = OLD.policy_no;
        //      END
        manager.addTriggerToTable(Consts.TABLE_NAME_TRAVEL, Consts.TRIGGER_TRAVEL_DELETE, Consts.SQL_AFTER_DELETE_ON, Consts.TRIGGER_STATEMENTS_TRAVEL_DELETE);

        // 3. Motor premium trigger that computes motor policy premium
        // CREATE TRIGGER IF NOT EXISTS motor_premium
        //      AFTER INSERT ON motor BEGIN
        //          UPDATE policy SET premium = 0.2*NEW.vehicle_price WHERE policy_no = NEW.policy_no;
        //      END
        manager.addTriggerToTable(Consts.TABLE_NAME_MOTOR, Consts.TRIGGER_MOTOR_PREMIUM, Consts.SQL_AFTER_INSERT_ON, Consts.TRIGGER_STATEMENTS_MOTOR_PREMIUM);

        // 4. Motor delete trigger that deletes motor policy from policy table
        // CREATE TRIGGER IF NOT EXISTS motor_delete
        //      AFTER DELETE ON motor BEGIN
        //          DELETE FROM policy WHERE policy_no = OLD.policy_no;
        //      END
        manager.addTriggerToTable(Consts.TABLE_NAME_MOTOR, Consts.TRIGGER_MOTOR_DELETE, Consts.SQL_AFTER_DELETE_ON, Consts.TRIGGER_STATEMENTS_MOTOR_DELETE);

        // 5. Medical premium trigger that computes medical policy premium
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
        manager.addTriggerToTable(Consts.TABLE_NAME_BENEFICIARY, Consts.TRIGGER_MEDICAL_PREMIUM, Consts.SQL_AFTER_INSERT_ON, Consts.TRIGGER_STATEMENTS_MEDICAL_PREMIUM);

        // 6. Medical delete trigger that deletes travel policy from policy table
        // CREATE TRIGGER IF NOT EXISTS medical_delete
        //      AFTER DELETE ON beneficiary BEGIN
        //          DELETE FROM policy WHERE policy_no = OLD.policy_no AND
        //              (SELECT COUNT(*) FROM beneficiary as T WHERE T.policy_no = OLD.policy_no) < 1;
        //      END
        manager.addTriggerToTable(Consts.TABLE_NAME_BENEFICIARY, Consts.TRIGGER_MEDICAL_DELETE, Consts.SQL_AFTER_DELETE_ON, Consts.TRIGGER_STATEMENTS_MEDICAL_DELETE);

        // 7. Medical self trigger that makes sure there is one self per policy_no
        // CREATE TRIGGER IF NOT EXISTS medical_self
        //      BEFORE INSERT ON beneficiary BEGIN
        //          SELECT
        //              CASE WHEN  NEW.relation = 'self' AND
        //                  (SELECT COUNT(*) FROM beneficiary WHERE beneficiary.policy_no = NEW.policy_no AND beneficiary.relation = 'self')>0
        //              THEN RAISE (ABORT, 'Can only have one beneficiary as self')
        //              END;
        //          END;
        //      END
        manager.addTriggerToTable(Consts.TABLE_NAME_BENEFICIARY, Consts.TRIGGER_MEDICAL_ONE_SELF, Consts.SQL_BEFORE_INSERT_ON, Consts.TRIGGER_STATEMENTS_MEDICAL_SELF);

        // 8. Claim abort trigger that makes sure incurred date is within expiry and effective and that policy number is valid
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
        manager.addTriggerToTable(Consts.TABLE_NAME_CLAIM, Consts.TRIGGER_CLAIM_ABORT, Consts.SQL_BEFORE_INSERT_ON, Consts.TRIGGER_STATEMENTS_CLAIM_ABORT);
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
                put(Consts.TABLE_COLUMN_EFFECTIVE, TestValues.TRAVEL_1_EFFECTIVE);
                put(Consts.TABLE_COLUMN_EXPIRY, TestValues.TRAVEL_1_EXPIRY);
                put(Consts.TABLE_COLUMN_DEPARTURE, TestValues.TRAVEL_1_DEPARTURE);
                put(Consts.TABLE_COLUMN_DESTINATION, TestValues.TRAVEL_1_DESTINATION);
                put(Consts.TABLE_COLUMN_FAMILY, TestValues.TRAVEL_1_FAMILY);
            }
        });

        // 2. Insert Motor policy 1 (successful)
        inserter.addMotorPolicy(new HashMap<String, Object>() {{
            put(Consts.TABLE_COLUMN_EFFECTIVE, TestValues.MOTOR_1_EFFECTIVE);
            put(Consts.TABLE_COLUMN_EXPIRY, TestValues.MOTOR_1_EXPIRY);
            put(Consts.TABLE_COLUMN_VEHICLE_PRICE, TestValues.MOTOR_1_VEHICLE_PRICE);
        }});

        // 3. Insert Medical policy 1 (successful)
        inserter.addMedicalPolicy(new HashMap<String, Object>() {{
            put(Consts.TABLE_COLUMN_EFFECTIVE, TestValues.MEDICAL_1_EFFECTIVE);
            put(Consts.TABLE_COLUMN_EXPIRY, TestValues.MEDICAL_1_EXPIRY);
            put(Consts.TABLE_COLUMN_NAME, TestValues.MEDICAL_1_NAME);
            put(Consts.TABLE_COLUMN_GENDER, TestValues.MEDICAL_1_GENDER);
            put(Consts.TABLE_COLUMN_RELATION, TestValues.MEDICAL_1_RELATION);
            put(Consts.TABLE_COLUMN_BIRTH_DATE, TestValues.MEDICAL_1_BIRTH_DATE);
        }});

        // 4. Insert Travel policy 2 (successful)
        inserter.addTravelPolicy(new HashMap<String, Object>() {{
            put(Consts.TABLE_COLUMN_EFFECTIVE, TestValues.TRAVEL_2_EFFECTIVE);
            put(Consts.TABLE_COLUMN_EXPIRY, TestValues.TRAVEL_2_EXPIRY);
            put(Consts.TABLE_COLUMN_DEPARTURE, TestValues.TRAVEL_2_DEPARTURE);
            put(Consts.TABLE_COLUMN_DESTINATION, TestValues.TRAVEL_2_DESTINATION);
            put(Consts.TABLE_COLUMN_FAMILY, TestValues.TRAVEL_2_FAMILY);
        }});

        // 5. Insert Motor policy 2 (successful)
        inserter.addMotorPolicy(new HashMap<String, Object>() {
            {
                put(Consts.TABLE_COLUMN_EFFECTIVE, TestValues.MOTOR_2_EFFECTIVE);
                put(Consts.TABLE_COLUMN_EXPIRY, TestValues.MOTOR_2_EXPIRY);
                put(Consts.TABLE_COLUMN_VEHICLE_PRICE, TestValues.MOTOR_2_VEHICLE_PRICE);
            }
        });

        // 6. Insert Medical policy 2 (successful)
        inserter.addMedicalPolicy(new HashMap<String, Object>() {{
            put(Consts.TABLE_COLUMN_EFFECTIVE, TestValues.MEDICAL_2_EFFECTIVE);
            put(Consts.TABLE_COLUMN_EXPIRY, TestValues.MEDICAL_2_EXPIRY);
            put(Consts.TABLE_COLUMN_NAME, TestValues.MEDICAL_2_NAME);
            put(Consts.TABLE_COLUMN_GENDER, TestValues.MEDICAL_2_GENDER);
            put(Consts.TABLE_COLUMN_RELATION, TestValues.MEDICAL_2_RELATION);
            put(Consts.TABLE_COLUMN_BIRTH_DATE, TestValues.MEDICAL_2_BIRTH_DATE);
        }});

        // 7. Insert Travel policy 3 (failure)
        inserter.addTravelPolicy(new HashMap<String, Object>() {
            {
                put(Consts.TABLE_COLUMN_EFFECTIVE, TestValues.TRAVEL_3_EFFECTIVE);
                put(Consts.TABLE_COLUMN_EXPIRY, TestValues.TRAVEL_3_EXPIRY);
                put(Consts.TABLE_COLUMN_DEPARTURE, TestValues.TRAVEL_3_DEPARTURE);
                put(Consts.TABLE_COLUMN_DESTINATION, TestValues.TRAVEL_3_DESTINATION);
                put(Consts.TABLE_COLUMN_FAMILY, TestValues.TRAVEL_3_FAMILY);
            }
        });

        // 8. Insert Medical policy 3 (failure)
        inserter.addMedicalPolicy(new HashMap<String, Object>() {{
            put(Consts.TABLE_COLUMN_EFFECTIVE, TestValues.MEDICAL_3_EFFECTIVE);
            put(Consts.TABLE_COLUMN_EXPIRY, TestValues.MEDICAL_3_EXPIRY);
            put(Consts.TABLE_COLUMN_NAME, TestValues.MEDICAL_3_NAME);
            put(Consts.TABLE_COLUMN_GENDER, TestValues.MEDICAL_3_GENDER);
            put(Consts.TABLE_COLUMN_RELATION, TestValues.MEDICAL_3_RELATION);
            put(Consts.TABLE_COLUMN_BIRTH_DATE, TestValues.MEDICAL_3_BIRTH_DATE);
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
                put(Consts.TABLE_COLUMN_POLICY_NO, TestValues.CLAIM_ALL_POLICY_NO[finalI]);
                put(Consts.TABLE_COLUMN_INCURRED_DATE, TestValues.CLAIM_ALL_INCURRED[finalI]);
                put(Consts.TABLE_COLUMN_CLAIMED_AMOUNT, TestValues.CLAIM_ALL_CLAIMED_AMOUNT[finalI]);
            }});
        }
    }
}
