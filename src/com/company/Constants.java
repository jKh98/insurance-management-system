package com.company;

class Constants {

    static final String PARAM_JDBC_DB_ENABLE_FK = "PRAGMA foreign_keys = ON";
    // General values
    static final String SQLITE_DIR_NAME = "sqlite";
    static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    static final String SQL_INSERT_INTO_TABLE = "INSERT INTO ";
    static final String SQL_VALUES = " VALUES";
    static final String SQL_SELECT = "SELECT ";
    static final String SQL_ALL = "*";
    static final String SQL_FROM = " FROM ";
    static final String SQL_WHERE = " WHERE ";
    static final String SQL_DELETE_FROM_TABLE = "DELETE FROM ";
    static final String SQL_CREATE_TRIGGER = "CREATE TRIGGER IF NOT EXISTS ";
    static final String SQL_AFTER_INSERT_ON = " AFTER INSERT ON ";
    static final String SQL_AFTER_DELETE_ON = " AFTER DELETE ON ";
    static final String SQL_BEGIN = " BEGIN ";
    static final String SQL_END = " END ";
    static final String PARAM_JDBC_DB_PREFIX = "jdbc:sqlite:";
    static final String MESSAGE_OPENED_DB = "Opened connection to database :";
    static final String MESSAGE_DB_DRIVER = "Using the database driver :";
    static final String MESSAGE_CLOSED_DB = "Closed connection to database";
    static final String MESSAGE_TABLE_ADDED = " table added to database";
    static final String MESSAGE_SUCCESSFULLY_ADDED = " was added successfully!";
    static final String MESSAGE_SPACE = " ";
    static final String MESSAGE_INVALID_POLICY = "Cannot add policy because it is invalid!";
    static final String MESSAGE_BENEFICIARY_SELF_ERROR = "Cannot add another beneficiary with relation self.";
    static final String MESSAGE_TRIGGER = "Trigger ";
    static final String MESSAGE_TRIGGER_ADDED = " was added on table ";

    // Database specific values
    static final String TABLE_COLUMN_ID = "id";
    static final String TABLE_AUTO_ID = TABLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT";

    // *************************** Parameters for policy table ***************************
    static final String TABLE_NAME_POLICY = "policy";
    static final String TABLE_COLUMN_EFFECTIVE = "effective";
    static final String TABLE_COLUMN_EXPIRY = "expiry";
    static final String TABLE_COLUMN_PREMIUM = "premium";
    static final String TABLE_COLUMN_IS_VALID = "is_valid";
    static final String TABLE_COLUMN_POLICY_TYPE = "policy_type";
    static final String TABLE_COLUMN_POLICY_NO = "policy_no";

    static final String[] TABLE_COLUMN_VALUES_POLICY = new String[]
            {Constants.TABLE_AUTO_ID
                    , TABLE_COLUMN_EFFECTIVE + " BIGINT NOT NULL"
                    , TABLE_COLUMN_EXPIRY + " BIGINT NOT NULL CHECK (" + TABLE_COLUMN_EXPIRY + " > " + TABLE_COLUMN_EFFECTIVE + ")"
                    , TABLE_COLUMN_PREMIUM + " DECIMAL NOT NULL"
                    , TABLE_COLUMN_IS_VALID + " INTEGER NOT NULL CHECK (" + TABLE_COLUMN_IS_VALID + " IN (0,1))"
                    , TABLE_COLUMN_POLICY_TYPE + " TEXT NOT NULL CHECK (" +
                    "(LOWER(" + TABLE_COLUMN_POLICY_TYPE + ") = 'travel' " +
                    "AND ((" + TABLE_COLUMN_EXPIRY + " - " + TABLE_COLUMN_EFFECTIVE + ")/86400 <= 30)) " +
                    "OR LOWER(" + TABLE_COLUMN_POLICY_TYPE + ") = 'motor' " +
                    "OR LOWER(" + TABLE_COLUMN_POLICY_TYPE + ") = 'medical')"
                    , TABLE_COLUMN_POLICY_NO + " TEXT UNIQUE NOT NULL GENERATED ALWAYS AS " +
                    "( STRFTIME('%Y',datetime(" + TABLE_COLUMN_EFFECTIVE + ", 'unixepoch')) || '-' || "
                    + TABLE_COLUMN_POLICY_TYPE + " || '-' || " + TABLE_COLUMN_ID + " ) STORED"

            };

    // *************************** Parameters for travel table ***************************
    static final String TABLE_NAME_TRAVEL = "travel";
    static final String TABLE_COLUMN_DEPARTURE = "departure";
    static final String TABLE_COLUMN_DESTINATION = "destination";
    static final String TABLE_COLUMN_FAMILY = "family";
    static final String TABLE_CONSTRAINT_TRAVEL = "fk_policy_no";

    static final String[] TABLE_COLUMN_VALUES_TRAVEL = new String[]
            {Constants.TABLE_AUTO_ID
                    , TABLE_COLUMN_POLICY_NO + " TEXT NOT NULL UNIQUE CHECK ( " + TABLE_COLUMN_POLICY_NO + " LIKE '%travel%')"
                    , TABLE_COLUMN_DEPARTURE + " TEXT NOT NULL"
                    , TABLE_COLUMN_DESTINATION + " TEXT NOT NULL"
                    , TABLE_COLUMN_FAMILY + " INTEGER NOT NULL CHECK (" + TABLE_COLUMN_FAMILY + " IN (0,1))"
                    , " CONSTRAINT " + TABLE_CONSTRAINT_TRAVEL + " FOREIGN KEY  (" + TABLE_COLUMN_POLICY_NO + ") REFERENCES " +
                    Constants.TABLE_NAME_POLICY + "(" + TABLE_COLUMN_POLICY_NO +
                    ") ON DELETE CASCADE"
            };

    // *************************** Parameters for motor table ***************************
    static final String TABLE_NAME_MOTOR = "motor";
    static final String TABLE_COLUMN_VEHICLE_PRICE = "vehicle_price";
    static final String TABLE_CONSTRAINT_MOTOR = "fk_policy_no";

    static final String[] TABLE_COLUMN_VALUES_MOTOR = new String[]
            {Constants.TABLE_AUTO_ID
                    , TABLE_COLUMN_POLICY_NO + " TEXT NOT NULL UNIQUE CHECK ( " + TABLE_COLUMN_POLICY_NO + " LIKE '%motor%') "
                    , TABLE_COLUMN_VEHICLE_PRICE + " DECIMAL NOT NULL"
                    , " CONSTRAINT " + TABLE_CONSTRAINT_MOTOR + " FOREIGN KEY  (" + TABLE_COLUMN_POLICY_NO + ") REFERENCES " +
                    Constants.TABLE_NAME_POLICY + "(" + TABLE_COLUMN_POLICY_NO +
                    ") ON DELETE CASCADE"
            };

    // *************************** Parameters for beneficiary table ***************************
    static final String TABLE_NAME_BENEFICIARY = "beneficiary";
    static final String TABLE_COLUMN_NAME = "name";
    static final String TABLE_COLUMN_RELATION = "relation";
    static final String TABLE_COLUMN_GENDER = "gender";
    static final String TABLE_COLUMN_BIRTH_DATE = "birth_date";
    static final String TABLE_CONSTRAINT_BENEFICIARY = "fk_policy_no";

    static final String[] TABLE_COLUMN_VALUES_BENEFICIARY = new String[]
            {Constants.TABLE_AUTO_ID
                    , TABLE_COLUMN_NAME + " TEXT NOT NULL"
                    , TABLE_COLUMN_RELATION + " TEXT NOT NULL CHECK (LOWER(" + TABLE_COLUMN_RELATION + ") IN ('self','spouse','son','daughter'))"
                    , TABLE_COLUMN_GENDER + " TEXT NOT NULL CHECK (LOWER(" + TABLE_COLUMN_GENDER + ") IN ('male','female'))"
                    , TABLE_COLUMN_BIRTH_DATE + " BIGINT NOT NULL"
                    , TABLE_COLUMN_POLICY_NO + " TEXT NOT NULL CHECK ( " + TABLE_COLUMN_POLICY_NO + " LIKE '%medical%')"
                    , " CONSTRAINT " + TABLE_CONSTRAINT_BENEFICIARY + " FOREIGN KEY  (" + TABLE_COLUMN_POLICY_NO + ") REFERENCES " +
                    Constants.TABLE_NAME_POLICY + "(" + TABLE_COLUMN_POLICY_NO +
                    ") ON DELETE CASCADE"
            };

    // *************************** Parameters for claim table ***************************
    static final String TABLE_NAME_CLAIM = "claim";
    static final String TABLE_COLUMN_INCURRED_DATE = "incurred_date";
    static final String TABLE_COLUMN_CLAIMED_AMOUNT = "claimed_amount";
    static final String TABLE_CONSTRAINT_CLAIM = "fk_policy_no";

    static final String[] TABLE_COLUMN_VALUES_CLAIM = new String[]
            {Constants.TABLE_AUTO_ID
                    , TABLE_COLUMN_POLICY_NO + " TEXT NOT NULL"
                    , TABLE_COLUMN_INCURRED_DATE + " BIGINT NOT NULL"
                    , TABLE_COLUMN_CLAIMED_AMOUNT + " DECIMAL NOT NULL"
                    , " CONSTRAINT " + TABLE_CONSTRAINT_CLAIM + " FOREIGN KEY  (" + TABLE_COLUMN_POLICY_NO + ") REFERENCES " +
                    Constants.TABLE_NAME_POLICY + "(" + TABLE_COLUMN_POLICY_NO +
                    ") ON DELETE CASCADE"
            };

    // *************************** Parameters for travel triggers ***************************
    static final String TRIGGER_TRAVEL_PREMIUM = "travel_premium";
    static final String[] TRIGGER_STATEMENTS_TRAVEL_PREMIUM = new String[]
            {"UPDATE " + TABLE_NAME_POLICY + " SET " + TABLE_COLUMN_PREMIUM +
                    " = CASE " +
                    "WHEN NEW." + TABLE_COLUMN_FAMILY + " = 1 THEN 10*(" + TABLE_COLUMN_EXPIRY + " - " + TABLE_COLUMN_EFFECTIVE + ")/86400 " +
                    "WHEN NEW." + TABLE_COLUMN_FAMILY + " = 0 THEN 5*(" + TABLE_COLUMN_EXPIRY + " - " + TABLE_COLUMN_EFFECTIVE + ")/86400 " +
                    "END WHERE " + TABLE_COLUMN_POLICY_NO + " = " + "NEW." + TABLE_COLUMN_POLICY_NO + ";",
            };
    static final String TRIGGER_TRAVEL_DELETE = "travel_delete";
    static final String[] TRIGGER_STATEMENTS_TRAVEL_DELETE = new String[]
            {"DELETE FROM " + TABLE_NAME_POLICY + " WHERE " + TABLE_COLUMN_POLICY_NO + " = " + "OLD." + TABLE_COLUMN_POLICY_NO + ";",};

    // *************************** Parameters for motor triggers ***************************
    static final String TRIGGER_MOTOR_PREMIUM = "motor_premium";
    static final String[] TRIGGER_STATEMENTS_MOTOR_PREMIUM = new String[]
            {"UPDATE " + TABLE_NAME_POLICY + " SET " + TABLE_COLUMN_PREMIUM +
                    " = 0.2*NEW." + TABLE_COLUMN_VEHICLE_PRICE +
                    " WHERE " + TABLE_COLUMN_POLICY_NO + " = " + "NEW." + TABLE_COLUMN_POLICY_NO + ";",
            };
    static final String TRIGGER_MOTOR_DELETE = "motor_delete";
    static final String[] TRIGGER_STATEMENTS_MOTOR_DELETE = new String[]
            {"DELETE FROM " + TABLE_NAME_POLICY + " WHERE " + TABLE_COLUMN_POLICY_NO + " = " + "OLD." + TABLE_COLUMN_POLICY_NO + ";",};

    // *************************** Parameters for medical triggers ***************************
    static final String TRIGGER_MEDICAL_PREMIUM = "medical_premium";
    static final String[] TRIGGER_STATEMENTS_MEDICAL_PREMIUM = new String[]
            {"UPDATE " + TABLE_NAME_POLICY + " SET " + TABLE_COLUMN_PREMIUM +
                    " = ( SELECT SUM( CASE " +
                    "WHEN (STRFTIME('%Y','now') - STRFTIME('%Y',datetime(T." + TABLE_COLUMN_BIRTH_DATE + ", 'unixepoch'))) < 10 THEN 15 " +
                    "WHEN (STRFTIME('%Y','now') - STRFTIME('%Y',datetime(T." + TABLE_COLUMN_BIRTH_DATE + ", 'unixepoch'))) BETWEEN 11 AND 45 THEN 30 " +
                    "WHEN (STRFTIME('%Y','now') - STRFTIME('%Y',datetime(T." + TABLE_COLUMN_BIRTH_DATE + ", 'unixepoch'))) > 45 THEN 45 " +
                    " END) FROM " + TABLE_NAME_BENEFICIARY + " AS T WHERE T." + TABLE_COLUMN_POLICY_NO + " = " + "NEW." + TABLE_COLUMN_POLICY_NO + ")" +
                    " WHERE " + TABLE_COLUMN_POLICY_NO + " = " + "NEW." + TABLE_COLUMN_POLICY_NO + ";",
            };
    static final String TRIGGER_MEDICAL_DELETE = "medical_delete";
    static final String[] TRIGGER_STATEMENTS_MEDICAL_DELETE = new String[]
            {"DELETE FROM " + TABLE_NAME_POLICY + " WHERE " + TABLE_COLUMN_POLICY_NO + " = " + "OLD." + TABLE_COLUMN_POLICY_NO + " AND (SELECT COUNT(*) FROM " +
                    TABLE_NAME_BENEFICIARY + " as T WHERE T." + TABLE_COLUMN_POLICY_NO + " = OLD." + TABLE_COLUMN_POLICY_NO + ") < 1;",};
}
