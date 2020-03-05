package sqlite;

import static sqlite.Consts.*;

/**
 * Class to handle addition of specific SQLite tables
 */
public class TableAdder {
    /**
     * SQLiteManager object to access database methods
     */
    SQLiteManager manager;

    /**
     * Constructor that sets up the SQLiteManager object
     *
     * @param manager SQLite manager object
     */
    public TableAdder(SQLiteManager manager) {
        this.manager = manager;
    }

    private String tableName;
    private String[] columns;

    /**
     * Adds policy table to store all policies
     * <p>
     * Statement:
     * CREATE TABLE IF NOT EXISTS policy
     * (
     *     id          INTEGER PRIMARY KEY AUTOINCREMENT,
     *     effective   BIGINT      NOT NULL,
     *     expiry      BIGINT      NOT NULL,
     *     premium     DECIMAL     NOT NULL,
     *     is_valid    INTEGER     NOT NULL CHECK (is_valid IN (0, 1)) default 1,
     *     policy_type TEXT        NOT NULL CHECK (LOWER(policy_type) IN ('travel', 'motor', 'medical')),
     *     policy_no   TEXT UNIQUE NOT NULL GENERATED ALWAYS AS ( STRFTIME('%Y', datetime (effective, 'unixepoch')) || '-' || policy_type || '-' || id ) STORED
     * );
     */
    public void addPolicyTable() {
        tableName = TABLE_NAME_POLICY;
        columns = new String[]{
                TABLE_AUTO_ID
                , TABLE_COLUMN_EFFECTIVE + " BIGINT NOT NULL"
                , TABLE_COLUMN_EXPIRY + " BIGINT NOT NULL"
                , TABLE_COLUMN_PREMIUM + " DECIMAL NOT NULL"
                , TABLE_COLUMN_IS_VALID + " INTEGER NOT NULL CHECK (" + TABLE_COLUMN_IS_VALID + " IN (0,1)) default 1"
                , TABLE_COLUMN_POLICY_TYPE + " TEXT NOT NULL CHECK (LOWER(" + TABLE_COLUMN_POLICY_TYPE + ") IN  ('travel', 'motor', 'medical'))"
                , TABLE_COLUMN_POLICY_NO + " TEXT UNIQUE NOT NULL GENERATED ALWAYS AS " +
                "( STRFTIME('%Y',datetime(" + TABLE_COLUMN_EFFECTIVE + ", 'unixepoch')) || '-' || "
                + TABLE_COLUMN_POLICY_TYPE + " || '-' || " + TABLE_COLUMN_ID + " ) STORED"
        };
        manager.addTableToDB(tableName, columns);
    }

    /**
     * Adds travel table to store data related to travel policies
     * <p>
     * Statement:
     * CREATE TABLE IF NOT EXISTS travel
     * (
     *     id          INTEGER PRIMARY KEY AUTOINCREMENT,
     *     policy_id   INTEGER NOT NULL UNIQUE,
     *     departure   TEXT    NOT NULL,
     *     destination TEXT    NOT NULL,
     *     family      INTEGER NOT NULL CHECK (family IN (0, 1)),
     *     CONSTRAINT fk_travel_policy_id FOREIGN KEY (policy_id) REFERENCES policy (id) ON DELETE CASCADE
     * );
     */
    public void addTravelTable() {
        tableName = TABLE_NAME_TRAVEL;
        columns = new String[]{TABLE_AUTO_ID
                , TABLE_COLUMN_POLICY_ID + " INTEGER NOT NULL UNIQUE "
                , TABLE_COLUMN_DEPARTURE + " TEXT NOT NULL"
                , TABLE_COLUMN_DESTINATION + " TEXT NOT NULL"
                , TABLE_COLUMN_FAMILY + " INTEGER NOT NULL CHECK (" + TABLE_COLUMN_FAMILY + " IN (0,1))"
                , " CONSTRAINT " + TABLE_CONSTRAINT_TRAVEL + " FOREIGN KEY  (" + TABLE_COLUMN_POLICY_ID + ") REFERENCES " +
                TABLE_NAME_POLICY + "(" + TABLE_COLUMN_ID + ") ON DELETE CASCADE"
        };
        manager.addTableToDB(tableName, columns);
    }

    /**
     * Adds motor table to store data related to motor policies
     * <p>
     * Statement:
     * CREATE TABLE IF NOT EXISTS motor
     * (
     *     id            INTEGER PRIMARY KEY AUTOINCREMENT,
     *     policy_id     INTEGER NOT NULL UNIQUE,
     *     vehicle_price DECIMAL NOT NULL,
     *     CONSTRAINT fk_motor_policy_id FOREIGN KEY (policy_id) REFERENCES policy (id) ON DELETE CASCADE
     * );
     */
    public void addMotorTable() {
        tableName = TABLE_NAME_MOTOR;
        columns = new String[]{TABLE_AUTO_ID
                , TABLE_COLUMN_POLICY_ID + " INTEGER NOT NULL UNIQUE "
                , TABLE_COLUMN_VEHICLE_PRICE + " DECIMAL NOT NULL"
                , " CONSTRAINT " + TABLE_CONSTRAINT_MOTOR + " FOREIGN KEY  (" + TABLE_COLUMN_POLICY_ID + ") REFERENCES " +
                TABLE_NAME_POLICY + "(" + TABLE_COLUMN_ID +
                ") ON DELETE CASCADE"
        };
        manager.addTableToDB(tableName, columns);
    }

    /**
     * Adds beneficiary table to store beneficiaries related to medical policies
     * Note that medical policies do not need a table on their own
     * <p>
     * Statement:
     * CREATE TABLE IF NOT EXISTS beneficiary
     * (
     *     id         INTEGER PRIMARY KEY AUTOINCREMENT,
     *     name       TEXT    NOT NULL,
     *     relation   TEXT    NOT NULL CHECK (LOWER(relation) IN ('self', 'spouse', 'son', 'daughter')),
     *     gender     TEXT    NOT NULL CHECK (LOWER(gender) IN ('male', 'female')),
     *     birth_date BIGINT  NOT NULL,
     *     policy_id  INTEGER NOT NULL UNIQUE,
     *     CONSTRAINT fk_medical_policy_id FOREIGN KEY (policy_id) REFERENCES policy (id) ON DELETE CASCADE
     * );
     */
    public void addBeneficiaryTable() {
        tableName = TABLE_NAME_BENEFICIARY;
        columns = new String[]{TABLE_AUTO_ID
                , TABLE_COLUMN_NAME + " TEXT NOT NULL"
                , TABLE_COLUMN_RELATION + " TEXT NOT NULL CHECK (LOWER(" + TABLE_COLUMN_RELATION + ") IN ('self','spouse','son','daughter'))"
                , TABLE_COLUMN_GENDER + " TEXT NOT NULL CHECK (LOWER(" + TABLE_COLUMN_GENDER + ") IN ('male','female'))"
                , TABLE_COLUMN_BIRTH_DATE + " BIGINT NOT NULL"
                , TABLE_COLUMN_POLICY_ID + " INTEGER NOT NULL UNIQUE "
                , " CONSTRAINT " + TABLE_CONSTRAINT_BENEFICIARY + " FOREIGN KEY  (" + TABLE_COLUMN_POLICY_ID + ") REFERENCES " +
                TABLE_NAME_POLICY + "(" + TABLE_COLUMN_ID + ") ON DELETE CASCADE"
        };
        manager.addTableToDB(tableName, columns);
    }

    /**
     * Adds claim table to store data related to claims on policies
     * <p>
     * Statement:
     * CREATE TABLE IF NOT EXISTS claim
     * (
     *     id             INTEGER PRIMARY KEY AUTOINCREMENT,
     *     policy_no      TEXT    NOT NULL,
     *     incurred_date  BIGINT  NOT NULL,
     *     claimed_amount DECIMAL NOT NULL
     * )
     */
    public void addClaimTable() {
        tableName = TABLE_NAME_CLAIM;
        columns = new String[]{
                TABLE_AUTO_ID
                , TABLE_COLUMN_POLICY_NO + " TEXT NOT NULL"
                , TABLE_COLUMN_INCURRED_DATE + " BIGINT NOT NULL"
                , TABLE_COLUMN_CLAIMED_AMOUNT + " DECIMAL NOT NULL"
        };
        manager.addTableToDB(tableName, columns);
    }

}
