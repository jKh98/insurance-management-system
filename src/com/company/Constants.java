package com.company;

class Constants {

    // General values
    static final String SQLITE_DIR_NAME = "sqlite";
    static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    static final String SQL_INSERT_INTO_TABLE = "INSERT INTO ";
    static final String SQL_VALUES = " VALUES";
    static final String PARAM_JDBC_DB_PREFIX = "jdbc:sqlite:";
    static final String MESSAGE_OPENED_DB = "Opened connection to database :";
    static final String MESSAGE_DB_DRIVER = "Using the database driver :";
    static final String MESSAGE_CLOSED_DB = "Closed connection to database";
    static final String MESSAGE_TABLE_ADDED = " table added to database";

    // Database specific values
    public static final String TABLE_COLUMN_ID = "id";
    public static final String TABLE_AUTO_ID = TABLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT";

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
                    , TABLE_COLUMN_POLICY_NO + " TEXT GENERATED ALWAYS AS " +
                    "( STRFTIME('%Y',datetime(" + TABLE_COLUMN_EFFECTIVE + ", 'unixepoch')) || '-' || "
                    + TABLE_COLUMN_POLICY_TYPE + " || '-' || " + TABLE_COLUMN_ID + " ) STORED"

            };

    // *************************** Parameters for travel table ***************************
    static final String TABLE_NAME_TRAVEL = "travel";
    static final String TABLE_COLUMN_DEPARTURE = "departure";
    static final String TABLE_COLUMN_DESTINATION = "destination";
    static final String TABLE_COLUMN_FAMILY = "family";

    static final String[] TABLE_COLUMN_VALUES_TRAVEL = new String[]
            {Constants.TABLE_AUTO_ID
                    , TABLE_COLUMN_POLICY_NO + " TEXT NOT NULL UNIQUE REFERENCES " + Constants.TABLE_NAME_POLICY + "(" + TABLE_COLUMN_POLICY_NO + ") " +
                    "CHECK ( " + TABLE_COLUMN_POLICY_NO + " LIKE '%travel%')"
                    , TABLE_COLUMN_DEPARTURE + " TEXT NOT NULL"
                    , TABLE_COLUMN_DESTINATION + " TEXT NOT NULL"
                    , TABLE_COLUMN_FAMILY + " INTEGER NOT NULL CHECK (" + TABLE_COLUMN_FAMILY + " IN (0,1))"
            };

    // *************************** Parameters for motor table ***************************
    static final String TABLE_NAME_MOTOR = "motor";
    static final String TABLE_COLUMN_VEHICLE_PRICE = "vehicle_price";

    static final String[] TABLE_COLUMN_VALUES_MOTOR = new String[]
            {Constants.TABLE_AUTO_ID
                    , TABLE_COLUMN_POLICY_NO + " TEXT NOT NULL UNIQUE REFERENCES " + Constants.TABLE_NAME_POLICY + "(" + TABLE_COLUMN_POLICY_NO + ")" +
                    "CHECK ( " + TABLE_COLUMN_POLICY_NO + " LIKE '%motor%') "
                    , TABLE_COLUMN_VEHICLE_PRICE + " DECIMAL NOT NULL"
            };

    // *************************** Parameters for claim table ***************************
    static final String TABLE_NAME_CLAIM = "claim";
    static final String TABLE_COLUMN_VEHICLE_INCURRED_DATE = "incurred_date";
    static final String TABLE_COLUMN_VEHICLE_CLAIMED_AMOUNT = "claimed_amount";

    static final String[] TABLE_COLUMN_VALUES_CLAIM = new String[]
            {Constants.TABLE_AUTO_ID
                    , TABLE_COLUMN_POLICY_NO + " TEXT NOT NULL REFERENCES " + Constants.TABLE_NAME_POLICY + "(" + TABLE_COLUMN_POLICY_NO + ")"
                    , TABLE_COLUMN_VEHICLE_INCURRED_DATE + " BIGINT NOT NULL"
                    , TABLE_COLUMN_VEHICLE_CLAIMED_AMOUNT + " DECIMAL NOT NULL"
            };

    // *************************** Parameters for beneficiary table ***************************
    static final String TABLE_NAME_BENEFICIARY = "beneficiary";
    static final String TABLE_COLUMN_VEHICLE_NAME = "name";
    static final String TABLE_COLUMN_VEHICLE_RELATION = "relation";
    static final String TABLE_COLUMN_VEHICLE_GENDER = "gender";
    static final String TABLE_COLUMN_VEHICLE_BIRTH_DATE = "birth_date";

    static final String[] TABLE_COLUMN_VALUES_BENEFICIARY = new String[]
            {Constants.TABLE_AUTO_ID
                    , TABLE_COLUMN_VEHICLE_NAME + " TEXT NOT NULL"
                    , TABLE_COLUMN_VEHICLE_RELATION + " TEXT NOT NULL CHECK (LOWER(" + TABLE_COLUMN_VEHICLE_RELATION + ") IN ('self','spouse','son','daughter'))"
                    , TABLE_COLUMN_VEHICLE_GENDER + " TEXT NOT NULL CHECK (LOWER(" + TABLE_COLUMN_VEHICLE_GENDER + ") IN ('male','female'))"
                    , TABLE_COLUMN_VEHICLE_BIRTH_DATE + " BIGINT NOT NULL"
                    , TABLE_COLUMN_POLICY_NO + " TEXT NOT NULL REFERENCES " + Constants.TABLE_NAME_POLICY + "(" + TABLE_COLUMN_POLICY_NO + ")" +
                    "CHECK ( " + TABLE_COLUMN_POLICY_NO + " LIKE '%medical%')"
            };
}
