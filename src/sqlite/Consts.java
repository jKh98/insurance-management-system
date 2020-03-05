package sqlite;

/**
 * Constant fields
 */
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
    public static final String SQL_BETWEEN = " BETWEEN ";
    public static final String SQL_COUNT = "count";
    public static final String SQL_SUM = "sum";
    public static final String SQL_MIN = "min";
    public static final String SQL_MAX = "max";
    public static final String SQL_DATE = "DATE";
    public static final String SQL_IF_NULL = "IFNULL";
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

    // *************************** Parameters for travel table ***************************
    public static final String TABLE_NAME_TRAVEL = "travel";
    public static final String TABLE_COLUMN_POLICY_ID = "policy_id";
    public static final String TABLE_COLUMN_DEPARTURE = "departure";
    public static final String TABLE_COLUMN_DESTINATION = "destination";
    public static final String TABLE_COLUMN_FAMILY = "family";
    public static final String TABLE_CONSTRAINT_TRAVEL = "fk_policy_id";

    // *************************** Parameters for motor table ***************************
    public static final String TABLE_NAME_MOTOR = "motor";
    public static final String TABLE_COLUMN_VEHICLE_PRICE = "vehicle_price";
    public static final String TABLE_CONSTRAINT_MOTOR = "fk_policy_id";

    // *************************** Parameters for beneficiary table ***************************
    public static final String TABLE_NAME_BENEFICIARY = "beneficiary";
    public static final String TABLE_COLUMN_NAME = "name";
    public static final String TABLE_COLUMN_RELATION = "relation";
    public static final String TABLE_COLUMN_GENDER = "gender";
    public static final String TABLE_COLUMN_BIRTH_DATE = "birth_date";
    public static final String TABLE_CONSTRAINT_BENEFICIARY = "fk_policy_id";

    // *************************** Parameters for claim table ***************************
    public static final String TABLE_NAME_CLAIM = "claim";
    public static final String TABLE_COLUMN_INCURRED_DATE = "incurred_date";
    public static final String TABLE_COLUMN_CLAIMED_AMOUNT = "claimed_amount";
    public static final String TABLE_CONSTRAINT_CLAIM = "fk_policy_no";

    // *************************** Parameters for travel triggers ***************************
    public static final String TRIGGER_TRAVEL_PREMIUM = "travel_premium";
    public static final String TRIGGER_TRAVEL_VALIDATE = "travel_validate";
    public static final String TRIGGER_TRAVEL_DELETE = "travel_delete";

    // *************************** Parameters for motor triggers ***************************
    public static final String TRIGGER_MOTOR_PREMIUM = "motor_premium";
    public static final String TRIGGER_MOTOR_VALIDATE = "motor_validate";
    public static final String TRIGGER_MOTOR_DELETE = "motor_delete";

    // *************************** Parameters for medical triggers ***************************
    public static final String TRIGGER_MEDICAL_PREMIUM = "medical_premium";
    public static final String TRIGGER_MEDICAL_VALIDATE = "medical_validate";
    public static final String TRIGGER_MEDICAL_DELETE = "medical_delete";
    public static final String TRIGGER_MEDICAL_ONE_SELF = "medical_self";

    // *************************** Parameters for claim triggers ***************************
    public static final String TRIGGER_CLAIM_ABORT = "claim_abort";

}
