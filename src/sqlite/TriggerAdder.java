package sqlite;

import static sqlite.Consts.*;

/**
 * Class to handle adding SQLite table-specific triggers
 */
public class TriggerAdder {
    /**
     * SQLiteManager object to access database methods
     */
    SQLiteManager manager;

    /**
     * Constructor that sets up the SQLiteManager object
     *
     * @param manager SQLite manager object
     */
    public TriggerAdder(SQLiteManager manager) {
        this.manager = manager;
    }

    private String tableName;
    private String triggerName;
    private String executeOn;
    private String[] statements;

    /**
     * Adds trigger that calculates premium for travel policies
     * <p>
     * Statement:
     * CREATE TRIGGER IF NOT EXISTS travel_premium
     * AFTER INSERT
     * ON travel
     * BEGIN
     * UPDATE policy
     * SET premium = CASE
     * WHEN NEW.family = 1 THEN 10 * (expiry - effective) / 86400
     * WHEN NEW.family = 0 THEN 5 * (expiry - effective) / 86400 END
     * WHERE policy_no = NEW.policy_no;
     * END;
     */
    public void addTravelPremiumTrigger() {
        tableName = TABLE_NAME_TRAVEL;
        triggerName = TRIGGER_TRAVEL_PREMIUM;
        executeOn = SQL_AFTER_INSERT_ON;
        statements = new String[]{
                "UPDATE " + TABLE_NAME_POLICY + " SET " + TABLE_COLUMN_PREMIUM + " = ",
                "CASE ",
                "WHEN " + DBUtils.dot("NEW", TABLE_COLUMN_FAMILY) + " = 1 THEN 10*(" + TABLE_COLUMN_EXPIRY + " - " + TABLE_COLUMN_EFFECTIVE + ")/86400 ",
                "WHEN " + DBUtils.dot("NEW", TABLE_COLUMN_FAMILY) + " = 0 THEN 5*(" + TABLE_COLUMN_EXPIRY + " - " + TABLE_COLUMN_EFFECTIVE + ")/86400 ",
                "END ",
                "WHERE " + TABLE_COLUMN_ID + " = " + DBUtils.dot("NEW", TABLE_COLUMN_POLICY_ID) + ";",
        };
        manager.addTriggerToTable(tableName, triggerName, executeOn, statements);
    }

    /**
     * Adds trigger that checks validation of travel policies
     * <p>
     * Statement:
     * CREATE TRIGGER IF NOT EXISTS travel_validate
     * AFTER INSERT
     * ON travel
     * BEGIN
     * UPDATE policy
     * SET is_valid= (case
     * when policy.effective > policy.expiry or (policy.expiry - policy.effective) / 86400 > 30 then 0
     * else 1 end)
     * where NEW.policy_no = policy.policy_no;
     * END;
     */
    public void addTravelValidationTrigger() {
        tableName = TABLE_NAME_TRAVEL;
        triggerName = TRIGGER_TRAVEL_VALIDATE;
        executeOn = SQL_AFTER_INSERT_ON;
        statements = new String[]{
                "UPDATE " + TABLE_NAME_POLICY + " SET " + TABLE_COLUMN_IS_VALID,
                "= (case when " + DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_EFFECTIVE) + " > " + DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_EXPIRY) + " or ",
                " (" + DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_EXPIRY) + " - " + DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_EFFECTIVE) + ")/86400 > 30 then 0",
                " else 1 end)",
                " where " + DBUtils.dot("NEW", TABLE_COLUMN_POLICY_ID) + " = " + DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_ID) + ";",
        };
        manager.addTriggerToTable(tableName, triggerName, executeOn, statements);

    }

    /**
     * Adds trigger that deletes travel policy data when the policy in the policy table is deleted
     * <p>
     * Statement:
     * CREATE TRIGGER IF NOT EXISTS travel_delete
     * AFTER DELETE
     * ON travel
     * BEGIN
     * DELETE FROM policy WHERE policy_no = OLD.policy_no;
     * END;
     */
    public void addTravelDeletionTrigger() {
        tableName = TABLE_NAME_TRAVEL;
        triggerName = TRIGGER_TRAVEL_DELETE;
        executeOn = SQL_AFTER_DELETE_ON;
        statements = new String[]{
                DBUtils.constructDeleteQuery(TABLE_NAME_POLICY, new String[]{TABLE_COLUMN_ID + " = " + DBUtils.dot("OLD", TABLE_COLUMN_POLICY_ID) + ";"})
        };
        manager.addTriggerToTable(tableName, triggerName, executeOn, statements);

    }

    /**
     * Adds trigger that calculates premium for motor policies
     * <p>
     * Statement:
     * CREATE TRIGGER IF NOT EXISTS motor_premium
     * AFTER INSERT
     * ON motor
     * BEGIN
     * UPDATE policy SET premium = 0.2 * NEW.vehicle_price WHERE policy_no = NEW.policy_no;
     * END;
     */
    public void addMotorPremiumTrigger() {
        tableName = TABLE_NAME_MOTOR;
        triggerName = TRIGGER_MOTOR_PREMIUM;
        executeOn = SQL_AFTER_INSERT_ON;
        statements = new String[]{
                "UPDATE " + TABLE_NAME_POLICY,
                " SET " + TABLE_COLUMN_PREMIUM + " = 0.2*" + DBUtils.dot("NEW", TABLE_COLUMN_VEHICLE_PRICE),
                " WHERE " + TABLE_COLUMN_ID + " = " + DBUtils.dot("NEW", TABLE_COLUMN_POLICY_ID) + ";",
        };
        manager.addTriggerToTable(tableName, triggerName, executeOn, statements);
    }

    /**
     * Adds trigger that checks validation of motor- policies
     * <p>
     * Statement:
     * CREATE TRIGGER IF NOT EXISTS motor_validate
     * AFTER INSERT
     * ON motor
     * BEGIN
     * UPDATE policy
     * SET is_valid= (case when policy.effective > policy.expiry then 0 else 1 end)
     * where NEW.policy_no = policy.policy_no;
     * END;
     */
    public void addMotorValidationTrigger() {
        tableName = TABLE_NAME_MOTOR;
        triggerName = TRIGGER_MOTOR_VALIDATE;
        executeOn = SQL_AFTER_INSERT_ON;
        statements = new String[]{
                "UPDATE " + TABLE_NAME_POLICY + " SET " + TABLE_COLUMN_IS_VALID,
                "= (case when " + DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_EFFECTIVE) + " > " +
                        DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_EXPIRY) + " then 0",
                " else 1 end)",
                " where " + DBUtils.dot("NEW", TABLE_COLUMN_POLICY_ID) + " = "
                        + DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_ID) + ";",
        };
        manager.addTriggerToTable(tableName, triggerName, executeOn, statements);
    }

    /**
     * Adds trigger that deletes motor policy data when the policy in the policy table is deleted
     * <p>
     * Statement:
     * CREATE TRIGGER IF NOT EXISTS motor_delete
     * AFTER DELETE
     * ON motor
     * BEGIN
     * DELETE FROM policy WHERE policy_no = OLD.policy_no;
     * END;
     */
    public void addMotorDeletionTrigger() {
        tableName = TABLE_NAME_MOTOR;
        triggerName = TRIGGER_MOTOR_DELETE;
        executeOn = SQL_AFTER_DELETE_ON;
        statements = new String[]{
                DBUtils.constructDeleteQuery(TABLE_NAME_POLICY, new String[]{TABLE_COLUMN_ID + " = " + DBUtils.dot("OLD", TABLE_COLUMN_POLICY_ID) + ";"})
        };
        manager.addTriggerToTable(tableName, triggerName, executeOn, statements);
    }

    /**
     * Adds trigger that calculates premium for medical policies
     * <p>
     * Statement:
     * CREATE TRIGGER IF NOT EXISTS medical_premium
     * AFTER INSERT
     * ON beneficiary
     * BEGIN
     * UPDATE policy
     * SET premium = (SELECT SUM(CASE
     * WHEN (STRFTIME('%Y', 'now') - STRFTIME('%Y', datetime(T.birth_date, 'unixepoch'))) <
     * 10 THEN 15
     * WHEN (STRFTIME('%Y', 'now') -
     * STRFTIME('%Y', datetime(T.birth_date, 'unixepoch'))) BETWEEN 11 AND 45 THEN 30
     * WHEN (STRFTIME('%Y', 'now') - STRFTIME('%Y', datetime(T.birth_date, 'unixepoch'))) >
     * 45 THEN 45 END)
     * FROM beneficiary AS T
     * WHERE T.policy_no = NEW.policy_no)
     * WHERE policy_no = NEW.policy_no;
     * END;
     */
    public void addMedicalPremiumTrigger() {
        tableName = TABLE_NAME_BENEFICIARY;
        triggerName = TRIGGER_MEDICAL_PREMIUM;
        executeOn = SQL_AFTER_INSERT_ON;
        statements = new String[]{
                "UPDATE " + TABLE_NAME_POLICY + " SET " + TABLE_COLUMN_PREMIUM + " = ",
                DBUtils.parenthesise(DBUtils.constructSelectQuery(
                        new String[]{TABLE_NAME_BENEFICIARY + " AS T "},
                        new String[]{
                                " SUM( CASE WHEN (STRFTIME('%Y','now') - STRFTIME('%Y',datetime(" + DBUtils.dot("T", TABLE_COLUMN_BIRTH_DATE) + ", 'unixepoch'))) < 10 THEN 15 " +
                                        " WHEN (STRFTIME('%Y','now') - STRFTIME('%Y',datetime(" + DBUtils.dot("T", TABLE_COLUMN_BIRTH_DATE) + ", 'unixepoch'))) BETWEEN 11 AND 45 THEN 30 " +
                                        " WHEN (STRFTIME('%Y','now') - STRFTIME('%Y',datetime(" + DBUtils.dot("T", TABLE_COLUMN_BIRTH_DATE) + ", 'unixepoch'))) > 45 THEN 45 " +
                                        " END)"},
                        new String[]{
                                DBUtils.dot("T", TABLE_COLUMN_POLICY_ID)
                                        + " = " + DBUtils.dot("NEW", TABLE_COLUMN_POLICY_ID)})),
                " WHERE " + TABLE_COLUMN_ID + " = " + "NEW." + TABLE_COLUMN_POLICY_ID + ";",
        };
        manager.addTriggerToTable(tableName, triggerName, executeOn, statements);
    }

    /**
     * Adds trigger that checks validation of medical policies
     * <p>
     * Statement:
     * CREATE TRIGGER IF NOT EXISTS medical_validate
     * AFTER INSERT
     * ON beneficiary
     * BEGIN
     * UPDATE policy
     * set is_valid = (case
     * when policy.effective > policy.expiry or
     * (SELECT count(*) FROM beneficiary WHERE beneficiary.policy_no = policy.policy_no) < 1
     * then 0
     * else 1 end)
     * where NEW.policy_no = policy.policy_no;
     * END;
     */
    public void addMedicalValidationTrigger() {
        tableName = TABLE_NAME_BENEFICIARY;
        triggerName = TRIGGER_MEDICAL_VALIDATE;
        executeOn = SQL_AFTER_INSERT_ON;
        statements = new String[]{
                "UPDATE " + TABLE_NAME_POLICY + " set " + TABLE_COLUMN_IS_VALID,
                " = (case when " + DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_EFFECTIVE) + " > " + DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_EXPIRY),
                " or ",
                DBUtils.parenthesise(DBUtils.constructSelectQuery(
                        new String[]{TABLE_NAME_BENEFICIARY},
                        new String[]{SQL_COUNT + DBUtils.parenthesise(SQL_ALL)},
                        new String[]{
                                DBUtils.dot(TABLE_NAME_BENEFICIARY, TABLE_COLUMN_POLICY_ID)
                                        + " = " + DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_ID)})) + "< 1",
                " then 0 else 1 end)",
                " where " + DBUtils.dot("NEW", TABLE_COLUMN_POLICY_ID) + " = " + DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_ID) + ";",
        };
        manager.addTriggerToTable(tableName, triggerName, executeOn, statements);
    }

    /**
     * Adds trigger that checks validity of medical policy when a corresponding beneficiary is deleted
     * <p>
     * Statement:
     * CREATE TRIGGER IF NOT EXISTS medical_delete
     * AFTER DELETE
     * ON beneficiary
     * BEGIN
     * UPDATE policy
     * set is_valid = 0
     * WHERE policy_no = OLD.policy_no
     * AND (SELECT count(*) FROM beneficiary as T WHERE T.policy_no = OLD.policy_no) < 1;
     * END;
     */
    public void addMedicalDeletionTrigger() {
        tableName = TABLE_NAME_BENEFICIARY;
        triggerName = TRIGGER_MEDICAL_DELETE;
        executeOn = SQL_AFTER_DELETE_ON;
        statements = new String[]{
                "UPDATE " + TABLE_NAME_POLICY + " set " + TABLE_COLUMN_IS_VALID + " = 0 WHERE "
                        + TABLE_COLUMN_ID + " = " + DBUtils.dot("OLD", TABLE_COLUMN_POLICY_ID),
                " AND ",
                DBUtils.parenthesise(DBUtils.constructSelectQuery(
                        new String[]{TABLE_NAME_BENEFICIARY + " as T"},
                        new String[]{SQL_COUNT + DBUtils.parenthesise(SQL_ALL)},
                        new String[]{
                                DBUtils.dot("T", TABLE_COLUMN_POLICY_ID)
                                        + " = " + DBUtils.dot("OLD", TABLE_COLUMN_POLICY_ID)})) + "< 1 ;",
        };
        manager.addTriggerToTable(tableName, triggerName, executeOn, statements);
    }

    /**
     * Adds trigger that asserts there is only ne self beneficiary per medical policy
     * <p>
     * Statement:
     * CREATE TRIGGER IF NOT EXISTS medical_self
     * BEFORE INSERT
     * ON beneficiary
     * BEGIN
     * SELECT CASE
     * WHEN NEW.relation = 'self' AND (SELECT count(*)
     * FROM beneficiary
     * WHERE beneficiary.policy_no = NEW.policy_no
     * AND beneficiary.relation = 'self') > 0
     * THEN RAISE(ABORT, 'Can only have one beneficiary as self') END;
     * END;
     * END;
     */
    public void addMedicalOneSelfTrigger() {
        tableName = TABLE_NAME_BENEFICIARY;
        triggerName = TRIGGER_MEDICAL_ONE_SELF;
        executeOn = SQL_BEFORE_INSERT_ON;
        statements = new String[]{
                "SELECT CASE WHEN ",
                DBUtils.dot("NEW", TABLE_COLUMN_RELATION) + " = 'self' AND ",
                DBUtils.parenthesise(DBUtils.constructSelectQuery(
                        new String[]{TABLE_NAME_BENEFICIARY},
                        new String[]{SQL_COUNT + DBUtils.parenthesise(SQL_ALL)},
                        new String[]{
                                DBUtils.dot(TABLE_NAME_BENEFICIARY, TABLE_COLUMN_POLICY_ID) + " =  " + DBUtils.dot("NEW", TABLE_COLUMN_POLICY_ID),
                                " AND " + DBUtils.dot(TABLE_NAME_BENEFICIARY, TABLE_COLUMN_RELATION) + " = 'self'"})) + " > 0 ",
                "THEN RAISE (ABORT, 'Can only have one beneficiary as self') END;END;"
        };
        manager.addTriggerToTable(tableName, triggerName, executeOn, statements);
    }

    /**
     * Adds trigger that aborts claim insertion if the policy does not exist or the incurred date is not between effective and expiry
     * <p>
     * Statement:
     * CREATE TRIGGER IF NOT EXISTS claim_abort
     * BEFORE INSERT
     * ON claim
     * BEGIN
     * SELECT CASE
     * WHEN NEW.incurred_date NOT BETWEEN (SELECT effective FROM policy WHERE policy_no = NEW.policy_no) AND (SELECT expiry FROM policy WHERE policy_no = NEW.policy_no)
     * THEN RAISE(ABORT, 'Claim is rejected because Policy# is inactive or expired!')
     * WHEN NOT EXISTS(SELECT policy_no FROM policy WHERE policy_no = NEW.policy_no) THEN RAISE(ABORT,
     * 'Cannot submit a claim for Policy#  because it does not exist!') END;
     * END;
     * END;
     */
    public void addClaimAbortTrigger() {
        tableName = TABLE_NAME_CLAIM;
        triggerName = TRIGGER_CLAIM_ABORT;
        executeOn = SQL_BEFORE_INSERT_ON;
        statements = new String[]{
                "SELECT CASE WHEN ",
                " NEW." + TABLE_COLUMN_INCURRED_DATE + " NOT BETWEEN ",
                DBUtils.parenthesise(DBUtils.constructSelectQuery(
                        new String[]{TABLE_NAME_POLICY},
                        new String[]{TABLE_COLUMN_EFFECTIVE},
                        new String[]{TABLE_COLUMN_POLICY_NO + " =  " + DBUtils.dot("NEW", TABLE_COLUMN_POLICY_NO)})),
                " AND ",
                DBUtils.parenthesise(DBUtils.constructSelectQuery(
                        new String[]{TABLE_NAME_POLICY},
                        new String[]{TABLE_COLUMN_EXPIRY},
                        new String[]{TABLE_COLUMN_POLICY_NO + " =  " + DBUtils.dot("NEW", TABLE_COLUMN_POLICY_NO)})),
                " THEN RAISE (ABORT, 'Claim is rejected because Policy# is inactive or expired!')",
                " WHEN NOT EXISTS ",
                DBUtils.parenthesise(DBUtils.constructSelectQuery(
                        new String[]{TABLE_NAME_POLICY},
                        new String[]{TABLE_COLUMN_POLICY_NO},
                        new String[]{TABLE_COLUMN_POLICY_NO + " =  " + DBUtils.dot("NEW", TABLE_COLUMN_POLICY_NO)})),
                " THEN RAISE (ABORT, 'Cannot submit a claim for Policy#  because it does not exist!')",
                " END;END;"
        };
        manager.addTriggerToTable(tableName, triggerName, executeOn, statements);
    }

}
