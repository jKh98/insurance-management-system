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

    public void addTravelPremiumTrigger(){
        tableName = TABLE_NAME_TRAVEL;
        triggerName = TRIGGER_TRAVEL_PREMIUM;
        executeOn = SQL_AFTER_INSERT_ON;
        statements = new String[]{
                "UPDATE " + TABLE_NAME_POLICY + " SET " + TABLE_COLUMN_PREMIUM + " = ",
                "CASE ",
                "WHEN NEW." + TABLE_COLUMN_FAMILY + " = 1 THEN 10*(" + TABLE_COLUMN_EXPIRY + " - " + TABLE_COLUMN_EFFECTIVE + ")/86400 ",
                "WHEN NEW." + TABLE_COLUMN_FAMILY + " = 0 THEN 5*(" + TABLE_COLUMN_EXPIRY + " - " + TABLE_COLUMN_EFFECTIVE + ")/86400 ",
                "END ",
                "WHERE " + TABLE_COLUMN_POLICY_NO + " = " + "NEW." + TABLE_COLUMN_POLICY_NO + ";",
        };
        manager.addTriggerToTable(tableName, triggerName, executeOn, statements);
    }
    public void addTravelValidationTrigger(){
        tableName = TABLE_NAME_TRAVEL;
        triggerName = TRIGGER_TRAVEL_VALIDATE;
        executeOn = SQL_AFTER_INSERT_ON;
        statements = new String[]{
                "UPDATE " + TABLE_NAME_POLICY + " SET " + TABLE_COLUMN_IS_VALID,
                "= (case when " + TABLE_NAME_POLICY + "." + TABLE_COLUMN_EFFECTIVE + " > " + TABLE_NAME_POLICY + "." + TABLE_COLUMN_EXPIRY + " or ",
                " (" + TABLE_NAME_POLICY + "." + TABLE_COLUMN_EXPIRY + " - " + TABLE_NAME_POLICY + "." + TABLE_COLUMN_EFFECTIVE + ")/86400 > 30 then 0",
                " else 1 end)",
                " where new." + TABLE_COLUMN_POLICY_NO + " = " + TABLE_NAME_POLICY + "." + TABLE_COLUMN_POLICY_NO + ";",
        };
        manager.addTriggerToTable(tableName, triggerName, executeOn, statements);

    }
    public void addTravelDeletionTrigger(){
        tableName = TABLE_NAME_TRAVEL;
        triggerName = TRIGGER_TRAVEL_DELETE;
        executeOn = SQL_AFTER_DELETE_ON;
        statements = new String[]{
                "DELETE FROM " + TABLE_NAME_POLICY + " WHERE " + TABLE_COLUMN_POLICY_NO + " = " + "OLD." + TABLE_COLUMN_POLICY_NO + ";",
        };
        manager.addTriggerToTable(tableName, triggerName, executeOn, statements);

    }
    public void addMotorPremiumTrigger(){
        tableName = TABLE_NAME_MOTOR;
        triggerName = TRIGGER_MOTOR_PREMIUM;
        executeOn = SQL_AFTER_INSERT_ON;
        statements = new String[]{
                "UPDATE " + TABLE_NAME_POLICY,
                " SET " + TABLE_COLUMN_PREMIUM + " = 0.2*NEW." + TABLE_COLUMN_VEHICLE_PRICE,
                " WHERE " + TABLE_COLUMN_POLICY_NO + " = " + "NEW." + TABLE_COLUMN_POLICY_NO + ";",
        };
        manager.addTriggerToTable(tableName, triggerName, executeOn, statements);
    }
    public void addMotorValidationTrigger(){
        tableName = TABLE_NAME_MOTOR;
        triggerName = TRIGGER_MOTOR_VALIDATE;
        executeOn = SQL_AFTER_INSERT_ON;
        statements = new String[]{
                "UPDATE " + TABLE_NAME_POLICY + " SET " + TABLE_COLUMN_IS_VALID,
                "= (case when " + TABLE_NAME_POLICY + "." + TABLE_COLUMN_EFFECTIVE + " > " + TABLE_NAME_POLICY + "." + TABLE_COLUMN_EXPIRY + " then 0",
                " else 1 end)",
                " where new." + TABLE_COLUMN_POLICY_NO + " = " + TABLE_NAME_POLICY + "." + TABLE_COLUMN_POLICY_NO + ";",
        };
        manager.addTriggerToTable(tableName, triggerName, executeOn, statements);
    }
    public void addMotorDeletionTrigger(){
        tableName = TABLE_NAME_MOTOR;
        triggerName = TRIGGER_MOTOR_DELETE;
        executeOn = SQL_AFTER_DELETE_ON;
        statements = new String[]{
                "DELETE FROM " + TABLE_NAME_POLICY + " WHERE " + TABLE_COLUMN_POLICY_NO + " = " + "OLD." + TABLE_COLUMN_POLICY_NO + ";",
        };
        manager.addTriggerToTable(tableName, triggerName, executeOn, statements);
    }
    public void addMedicalPremiumTrigger(){
        tableName = TABLE_NAME_BENEFICIARY;
        triggerName = TRIGGER_MEDICAL_PREMIUM;
        executeOn = SQL_AFTER_INSERT_ON;
        statements = new String[]{
                "UPDATE " + TABLE_NAME_POLICY + " SET " + TABLE_COLUMN_PREMIUM + " = ( SELECT SUM( CASE ",
                "WHEN (STRFTIME('%Y','now') - STRFTIME('%Y',datetime(T." + TABLE_COLUMN_BIRTH_DATE + ", 'unixepoch'))) < 10 THEN 15 ",
                "WHEN (STRFTIME('%Y','now') - STRFTIME('%Y',datetime(T." + TABLE_COLUMN_BIRTH_DATE + ", 'unixepoch'))) BETWEEN 11 AND 45 THEN 30 ",
                "WHEN (STRFTIME('%Y','now') - STRFTIME('%Y',datetime(T." + TABLE_COLUMN_BIRTH_DATE + ", 'unixepoch'))) > 45 THEN 45 ",
                " END) FROM " + TABLE_NAME_BENEFICIARY + " AS T WHERE T." + TABLE_COLUMN_POLICY_NO + " = " + "NEW." + TABLE_COLUMN_POLICY_NO + ")",
                " WHERE " + TABLE_COLUMN_POLICY_NO + " = " + "NEW." + TABLE_COLUMN_POLICY_NO + ";",
        };
        manager.addTriggerToTable(tableName, triggerName, executeOn, statements);
    }
    public void addMedicalValidationTrigger(){
        tableName = TABLE_NAME_BENEFICIARY;
        triggerName = TRIGGER_MEDICAL_VALIDATE;
        executeOn = SQL_AFTER_INSERT_ON;
        statements = new String[]{
                "    update policy set is_valid",
                " = (case when policy.effective > policy.expiry or",
                " (select count(*) from beneficiary where beneficiary.policy_no = policy.policy_no)<1 then 0 else 1 end)",
                " where new.policy_no = policy.policy_no;",
        };
        manager.addTriggerToTable(tableName, triggerName, executeOn, statements);
    }
    public void addMedicalDeletionTrigger(){
        tableName = TABLE_NAME_BENEFICIARY;
        triggerName = TRIGGER_MEDICAL_DELETE;
        executeOn = SQL_AFTER_DELETE_ON;
        statements = new String[]{
                "UPDATE " + TABLE_NAME_POLICY + " set is_valid = 0 WHERE " + TABLE_COLUMN_POLICY_NO + " = " + "OLD." + TABLE_COLUMN_POLICY_NO,
                " AND (SELECT COUNT(*) FROM " + TABLE_NAME_BENEFICIARY + " as T ",
                "WHERE T." + TABLE_COLUMN_POLICY_NO + " = OLD." + TABLE_COLUMN_POLICY_NO + ") < 1;",
        };
        manager.addTriggerToTable(tableName, triggerName, executeOn, statements);
    };
    public void addMedicalOneSelfTrigger(){
        tableName = TABLE_NAME_BENEFICIARY;
        triggerName = TRIGGER_MEDICAL_ONE_SELF;
        executeOn = SQL_BEFORE_INSERT_ON;
        statements = new String[]{
                "SELECT CASE WHEN ",
                " NEW." + TABLE_COLUMN_RELATION + " = 'self' AND ",
                "(SELECT COUNT(*) FROM " + TABLE_NAME_BENEFICIARY,
                " WHERE " + TABLE_NAME_BENEFICIARY + "." + TABLE_COLUMN_POLICY_NO + " = NEW." + TABLE_COLUMN_POLICY_NO,
                " AND " + TABLE_NAME_BENEFICIARY + "." + TABLE_COLUMN_RELATION + " = 'self')>0 ",
                "THEN RAISE (ABORT, 'Can only have one beneficiary as self') END;END;"
        };
        manager.addTriggerToTable(tableName, triggerName, executeOn, statements);
    };
    public void addClaimAbortTrigger(){
        tableName = TABLE_NAME_CLAIM;
        triggerName = TRIGGER_CLAIM_ABORT;
        executeOn = SQL_BEFORE_INSERT_ON;
        statements = new String[]{
                "SELECT CASE WHEN ",
                " NEW." + TABLE_COLUMN_INCURRED_DATE + " NOT BETWEEN ",
                "(SELECT " + TABLE_COLUMN_EFFECTIVE + " FROM " + TABLE_NAME_POLICY,
                " WHERE " + TABLE_COLUMN_POLICY_NO + " = NEW." + TABLE_COLUMN_POLICY_NO + " )",
                " AND (SELECT " + TABLE_COLUMN_EXPIRY + " FROM " + TABLE_NAME_POLICY,
                " WHERE " + TABLE_COLUMN_POLICY_NO + " = NEW." + TABLE_COLUMN_POLICY_NO + " )",
                "THEN RAISE (ABORT, 'Claim is rejected because Policy# is inactive or expired!')",
                "WHEN NOT EXISTS (SELECT " + TABLE_COLUMN_POLICY_NO + " FROM " + TABLE_NAME_POLICY + " WHERE " + TABLE_COLUMN_POLICY_NO +
                        " = NEW." + TABLE_COLUMN_POLICY_NO + ") THEN RAISE (ABORT, 'Cannot submit a claim for Policy#  because it does not exist!')",
                " END;END;"
        };
        manager.addTriggerToTable(tableName, triggerName, executeOn, statements);
    };
}
