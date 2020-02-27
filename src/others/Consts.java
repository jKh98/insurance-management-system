package others;

public class Consts {

    // General values
    public static final String SQLITE_DIR_NAME = "sqlite";
    public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    public static final String SQL_INSERT_INTO_TABLE = "INSERT INTO ";
    public static final String SQL_VALUES = " VALUES";
    public static final String SQL_SELECT = "SELECT ";
    public static final String SQL_ALL = "*";
    public static final String SQL_FROM = " FROM ";
    public static final String SQL_WHERE = " WHERE ";
    public static final String SQL_DELETE_FROM_TABLE = "DELETE FROM ";
    public static final String SQL_CREATE_TRIGGER = "CREATE TRIGGER IF NOT EXISTS ";
    public static final String SQL_AFTER_INSERT_ON = " AFTER INSERT ON ";
    public static final String SQL_AFTER_DELETE_ON = " AFTER DELETE ON ";
    public static final String SQL_BEFORE_INSERT_ON = " BEFORE INSERT ON ";
    public static final String SQL_BEGIN = " BEGIN ";
    public static final String SQL_END = " END ";
    public static final String SQL_BETWEEN = " BETWEEN";
    public static final String SQL_COUNT = "count";
    public static final String SQL_SUM = "sum";
    public static final String SQL_MIN = "min";
    public static final String SQL_MAX = "max";
    public static final String SQL_DATE = "DATE";
    public static final String PARAM_JDBC_DB_PREFIX = "jdbc:sqlite:";
    public static final String PARAM_JDBC_DB_ENABLE_FK = "PRAGMA foreign_keys = ON";
    public static final String MESSAGE_OPENED_DB = "Opened connection to database :";
    public static final String MESSAGE_DB_DRIVER = "Using the database driver :";
    public static final String MESSAGE_CLOSED_DB = "Closed connection to database";
    public static final String MESSAGE_TABLE_ADDED = " table added to database";
    public static final String MESSAGE_SUCCESSFULLY_ADDED = " was added successfully!";
    public static final String MESSAGE_SPACE = " ";
    public static final String MESSAGE_INVALID_POLICY = "Cannot add policy because it is invalid!";
    public static final String MESSAGE_INVALID_BENEFICIARY = "Cannot add beneficiary because it is invalid!";
    public static final String MESSAGE_BENEFICIARY_SELF_ERROR = "Cannot add another beneficiary with relation self.";
    public static final String MESSAGE_TRIGGER = "Trigger ";
    public static final String MESSAGE_TRIGGER_ADDED = " was added on table ";
    public static final String MESSAGE_NO_RESULTS = "No results";
    public static final String MESSAGE_INVALID_CLAIM = "Cannot add beneficiary because it is invalid!";

    // Database specific values
    public static final String TABLE_COLUMN_ID = "id";
    public static final String TABLE_AUTO_ID = TABLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT";

    // *************************** Parameters for policy table ***************************
    public static final String TABLE_NAME_POLICY = "policy";
    public static final String TABLE_COLUMN_EFFECTIVE = "effective";
    public static final String TABLE_COLUMN_EXPIRY = "expiry";
    public static final String TABLE_COLUMN_PREMIUM = "premium";
    public static final String TABLE_COLUMN_IS_VALID = "is_valid";
    public static final String TABLE_COLUMN_POLICY_TYPE = "policy_type";
    public static final String TABLE_COLUMN_POLICY_NO = "policy_no";

    public static final String[] TABLE_COLUMN_VALUES_POLICY = new String[]
            {Consts.TABLE_AUTO_ID
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
    public static final String TABLE_NAME_TRAVEL = "travel";
    public static final String TABLE_COLUMN_DEPARTURE = "departure";
    public static final String TABLE_COLUMN_DESTINATION = "destination";
    public static final String TABLE_COLUMN_FAMILY = "family";
    public static final String TABLE_CONSTRAINT_TRAVEL = "fk_policy_no";

    public static final String[] TABLE_COLUMN_VALUES_TRAVEL = new String[]
            {Consts.TABLE_AUTO_ID
                    , TABLE_COLUMN_POLICY_NO + " TEXT NOT NULL UNIQUE CHECK ( " + TABLE_COLUMN_POLICY_NO + " LIKE '%travel%')"
                    , TABLE_COLUMN_DEPARTURE + " TEXT NOT NULL"
                    , TABLE_COLUMN_DESTINATION + " TEXT NOT NULL"
                    , TABLE_COLUMN_FAMILY + " INTEGER NOT NULL CHECK (" + TABLE_COLUMN_FAMILY + " IN (0,1))"
                    , " CONSTRAINT " + TABLE_CONSTRAINT_TRAVEL + " FOREIGN KEY  (" + TABLE_COLUMN_POLICY_NO + ") REFERENCES " +
                    Consts.TABLE_NAME_POLICY + "(" + TABLE_COLUMN_POLICY_NO +
                    ") ON DELETE CASCADE"
            };

    // *************************** Parameters for motor table ***************************
    public static final String TABLE_NAME_MOTOR = "motor";
    public static final String TABLE_COLUMN_VEHICLE_PRICE = "vehicle_price";
    public static final String TABLE_CONSTRAINT_MOTOR = "fk_policy_no";

    public static final String[] TABLE_COLUMN_VALUES_MOTOR = new String[]
            {Consts.TABLE_AUTO_ID
                    , TABLE_COLUMN_POLICY_NO + " TEXT NOT NULL UNIQUE CHECK ( " + TABLE_COLUMN_POLICY_NO + " LIKE '%motor%') "
                    , TABLE_COLUMN_VEHICLE_PRICE + " DECIMAL NOT NULL"
                    , " CONSTRAINT " + TABLE_CONSTRAINT_MOTOR + " FOREIGN KEY  (" + TABLE_COLUMN_POLICY_NO + ") REFERENCES " +
                    Consts.TABLE_NAME_POLICY + "(" + TABLE_COLUMN_POLICY_NO +
                    ") ON DELETE CASCADE"
            };

    // *************************** Parameters for beneficiary table ***************************
    public static final String TABLE_NAME_BENEFICIARY = "beneficiary";
    public static final String TABLE_COLUMN_NAME = "name";
    public static final String TABLE_COLUMN_RELATION = "relation";
    public static final String TABLE_COLUMN_GENDER = "gender";
    public static final String TABLE_COLUMN_BIRTH_DATE = "birth_date";
    public static final String TABLE_CONSTRAINT_BENEFICIARY = "fk_policy_no";

    public static final String[] TABLE_COLUMN_VALUES_BENEFICIARY = new String[]
            {Consts.TABLE_AUTO_ID
                    , TABLE_COLUMN_NAME + " TEXT NOT NULL"
                    , TABLE_COLUMN_RELATION + " TEXT NOT NULL CHECK (LOWER(" + TABLE_COLUMN_RELATION + ") IN ('self','spouse','son','daughter'))"
                    , TABLE_COLUMN_GENDER + " TEXT NOT NULL CHECK (LOWER(" + TABLE_COLUMN_GENDER + ") IN ('male','female'))"
                    , TABLE_COLUMN_BIRTH_DATE + " BIGINT NOT NULL"
                    , TABLE_COLUMN_POLICY_NO + " TEXT NOT NULL CHECK ( " + TABLE_COLUMN_POLICY_NO + " LIKE '%medical%')"
                    , " CONSTRAINT " + TABLE_CONSTRAINT_BENEFICIARY + " FOREIGN KEY  (" + TABLE_COLUMN_POLICY_NO + ") REFERENCES " +
                    Consts.TABLE_NAME_POLICY + "(" + TABLE_COLUMN_POLICY_NO +
                    ") ON DELETE CASCADE"
            };

    // *************************** Parameters for claim table ***************************
    public static final String TABLE_NAME_CLAIM = "claim";
    public static final String TABLE_COLUMN_INCURRED_DATE = "incurred_date";
    public static final String TABLE_COLUMN_CLAIMED_AMOUNT = "claimed_amount";
    public static final String TABLE_CONSTRAINT_CLAIM = "fk_policy_no";

    public static final String[] TABLE_COLUMN_VALUES_CLAIM = new String[]
            {Consts.TABLE_AUTO_ID
                    , TABLE_COLUMN_POLICY_NO + " TEXT NOT NULL"
                    , TABLE_COLUMN_INCURRED_DATE + " BIGINT NOT NULL"
                    , TABLE_COLUMN_CLAIMED_AMOUNT + " DECIMAL NOT NULL"
                    , " CONSTRAINT " + TABLE_CONSTRAINT_CLAIM + " FOREIGN KEY  (" + TABLE_COLUMN_POLICY_NO + ") REFERENCES " +
                    Consts.TABLE_NAME_POLICY + "(" + TABLE_COLUMN_POLICY_NO +
                    ") ON DELETE CASCADE"
            };

    // *************************** Parameters for travel triggers ***************************
    public static final String TRIGGER_TRAVEL_PREMIUM = "travel_premium";
    public static final String[] TRIGGER_STATEMENTS_TRAVEL_PREMIUM = new String[]
            {"UPDATE " + TABLE_NAME_POLICY + " SET " + TABLE_COLUMN_PREMIUM + " = ",
                    "CASE ",
                    "WHEN NEW." + TABLE_COLUMN_FAMILY + " = 1 THEN 10*(" + TABLE_COLUMN_EXPIRY + " - " + TABLE_COLUMN_EFFECTIVE + ")/86400 ",
                    "WHEN NEW." + TABLE_COLUMN_FAMILY + " = 0 THEN 5*(" + TABLE_COLUMN_EXPIRY + " - " + TABLE_COLUMN_EFFECTIVE + ")/86400 ",
                    "END ",
                    "WHERE " + TABLE_COLUMN_POLICY_NO + " = " + "NEW." + TABLE_COLUMN_POLICY_NO + ";",
            };
    public static final String TRIGGER_TRAVEL_DELETE = "travel_delete";
    public static final String[] TRIGGER_STATEMENTS_TRAVEL_DELETE = new String[]
            {"DELETE FROM " + TABLE_NAME_POLICY + " WHERE " + TABLE_COLUMN_POLICY_NO + " = " + "OLD." + TABLE_COLUMN_POLICY_NO + ";",};

    // *************************** Parameters for motor triggers ***************************
    public static final String TRIGGER_MOTOR_PREMIUM = "motor_premium";
    public static final String[] TRIGGER_STATEMENTS_MOTOR_PREMIUM = new String[]
            {"UPDATE " + TABLE_NAME_POLICY,
                    " SET " + TABLE_COLUMN_PREMIUM + " = 0.2*NEW." + TABLE_COLUMN_VEHICLE_PRICE,
                    " WHERE " + TABLE_COLUMN_POLICY_NO + " = " + "NEW." + TABLE_COLUMN_POLICY_NO + ";",
            };
    public static final String TRIGGER_MOTOR_DELETE = "motor_delete";
    public static final String[] TRIGGER_STATEMENTS_MOTOR_DELETE = new String[]
            {"DELETE FROM " + TABLE_NAME_POLICY + " WHERE " + TABLE_COLUMN_POLICY_NO + " = " + "OLD." + TABLE_COLUMN_POLICY_NO + ";",};

    // *************************** Parameters for medical triggers ***************************
    public static final String TRIGGER_MEDICAL_PREMIUM = "medical_premium";
    public static final String[] TRIGGER_STATEMENTS_MEDICAL_PREMIUM = new String[]
            {"UPDATE " + TABLE_NAME_POLICY + " SET " + TABLE_COLUMN_PREMIUM + " = ( SELECT SUM( CASE ",
                    "WHEN (STRFTIME('%Y','now') - STRFTIME('%Y',datetime(T." + TABLE_COLUMN_BIRTH_DATE + ", 'unixepoch'))) < 10 THEN 15 ",
                    "WHEN (STRFTIME('%Y','now') - STRFTIME('%Y',datetime(T." + TABLE_COLUMN_BIRTH_DATE + ", 'unixepoch'))) BETWEEN 11 AND 45 THEN 30 ",
                    "WHEN (STRFTIME('%Y','now') - STRFTIME('%Y',datetime(T." + TABLE_COLUMN_BIRTH_DATE + ", 'unixepoch'))) > 45 THEN 45 ",
                    " END) FROM " + TABLE_NAME_BENEFICIARY + " AS T WHERE T." + TABLE_COLUMN_POLICY_NO + " = " + "NEW." + TABLE_COLUMN_POLICY_NO + ")",
                    " WHERE " + TABLE_COLUMN_POLICY_NO + " = " + "NEW." + TABLE_COLUMN_POLICY_NO + ";",
            };
    public static final String TRIGGER_MEDICAL_DELETE = "medical_delete";
    public static final String[] TRIGGER_STATEMENTS_MEDICAL_DELETE = new String[]
            {"DELETE FROM " + TABLE_NAME_POLICY + " WHERE " + TABLE_COLUMN_POLICY_NO + " = " + "OLD." + TABLE_COLUMN_POLICY_NO,
                    " AND (SELECT COUNT(*) FROM " + TABLE_NAME_BENEFICIARY + " as T ",
                    "WHERE T." + TABLE_COLUMN_POLICY_NO + " = OLD." + TABLE_COLUMN_POLICY_NO + ") < 1;",};
    public static final String TRIGGER_MEDICAL_ONE_SELF = "medical_self";
    public static final String[] TRIGGER_STATEMENTS_MEDICAL_SELF = new String[]
            {"SELECT CASE WHEN ",
                    " NEW." + TABLE_COLUMN_RELATION + " = 'self' AND ",
                    "(SELECT COUNT(*) FROM " + TABLE_NAME_BENEFICIARY,
                    " WHERE " + TABLE_NAME_BENEFICIARY + "." + TABLE_COLUMN_POLICY_NO + " = NEW." + TABLE_COLUMN_POLICY_NO,
                    " AND " + TABLE_NAME_BENEFICIARY + "." + TABLE_COLUMN_RELATION + " = 'self')>0 ",
                    "THEN RAISE (ABORT, 'Can only have one beneficiary as self') END;END;"};

    // *************************** Parameters for claim triggers ***************************
    public static final String TRIGGER_CLAIM_ABORT = "claim_abort";
    public static final String[] TRIGGER_STATEMENTS_CLAIM_ABORT = new String[]
            {"SELECT CASE WHEN ",
                    " NEW." + TABLE_COLUMN_INCURRED_DATE + " NOT BETWEEN ",
                    "(SELECT " + TABLE_COLUMN_EFFECTIVE + " FROM " + TABLE_NAME_POLICY,
                    " WHERE " + TABLE_COLUMN_POLICY_NO + " = NEW." + TABLE_COLUMN_POLICY_NO + " )",
                    " AND (SELECT " + TABLE_COLUMN_EXPIRY + " FROM " + TABLE_NAME_POLICY,
                    " WHERE " + TABLE_COLUMN_POLICY_NO + " = NEW." + TABLE_COLUMN_POLICY_NO + " )",
                    "THEN RAISE (ABORT, 'Claim is rejected because Policy# is inactive or expired!')",
                    "WHEN NOT EXISTS (SELECT " + TABLE_COLUMN_POLICY_NO + " FROM " + TABLE_NAME_POLICY + " WHERE " + TABLE_COLUMN_POLICY_NO +
                            " = NEW." + TABLE_COLUMN_POLICY_NO + ") THEN RAISE (ABORT, 'Cannot submit a claim for Policy#  because it does not exist!')",
                    " END;END;"};

}
//' || New." + TABLE_COLUMN_POLICY_NO + " || '