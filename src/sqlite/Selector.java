package sqlite;

import java.util.ArrayList;
import static sqlite.Consts.*;


/**
 * Contains methods used to select and print specific data from specific tables usually based on requirements
 */
public class Selector {

    /**
     * manager object to access database methods
     */
    SQLiteManager manager;

    /**
     * Constructor that sets up the manager object
     *
     * @param manager SQLite manager object
     */
    public Selector(SQLiteManager manager) {
        this.manager = manager;
    }

    private String[] tableNames;
    private String[] selections;
    private String[] conditions;
    private Object[] values;

    /**
     * Selects and prints all available travel policies
     * Prints following: PolicyNo, EffectiveDate, ExpiryDate, Premium, IsValid, DepartureCountry, DestinationCountry, Family
     * <p>
     * Query:
     * SELECT policy.policy_no,
     * DATE(policy.effective,'unixepoch'),
     * DATE(policy.expiry,'unixepoch'),
     * policy.premium,policy.is_valid,
     * travel.departure,
     * travel.destination,
     * travel.family
     * FROM   policy,
     * travel
     * WHERE  policy.policy_no =  travel.policy_no
     */
    public void selectAllTravelPolicies() {
        // Array list to store query result
        ArrayList<Object[]> result;
        tableNames = new String[]{
                TABLE_NAME_POLICY,
                TABLE_NAME_TRAVEL,
        };
        selections = new String[]{
                DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_POLICY_NO),
                SQL_DATE + DBUtils.parenthesise(DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_EFFECTIVE) + ",'unixepoch'"),
                SQL_DATE + DBUtils.parenthesise(DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_EXPIRY) + ",'unixepoch'"),
                DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_PREMIUM),
                DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_IS_VALID),
                DBUtils.dot(TABLE_NAME_TRAVEL, TABLE_COLUMN_DEPARTURE),
                DBUtils.dot(TABLE_NAME_TRAVEL, TABLE_COLUMN_DESTINATION),
                DBUtils.dot(TABLE_NAME_TRAVEL, TABLE_COLUMN_FAMILY),
        };
        conditions = new String[]{DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_POLICY_NO) + " = ",
                DBUtils.dot(TABLE_NAME_TRAVEL, TABLE_COLUMN_POLICY_NO)};

        result = manager.selectDataFromTable(tableNames, selections, conditions, null
        );
        // Prints results in table form
        Printer.printAsList(new String[]{
                TABLE_COLUMN_POLICY_NO, TABLE_COLUMN_EFFECTIVE, TABLE_COLUMN_EXPIRY,
                TABLE_COLUMN_PREMIUM,
                TABLE_COLUMN_IS_VALID,
                TABLE_COLUMN_DEPARTURE,
                TABLE_COLUMN_DESTINATION, TABLE_COLUMN_FAMILY,
        }, result);
    }

    /**
     * Selects and prints all available motor policies
     * Prints following: PolicyNo, EffectiveDate, ExpiryDate, Premium, IsValid, VehiclePrice
     * <p>
     * Query:
     * SELECT   policy.policy_no,
     * DATE(policy.effective,'unixepoch'),
     * DATE(policy.expiry,'unixepoch'),
     * policy.premium,
     * policy.is_valid,
     * motor.vehicle_price
     * FROM     policy,
     * motor
     * WHERE    policy.policy_no =  motor.policy_no
     */
    public void selectAllMotorPolicies() {
        // Array list to store query result
        ArrayList<Object[]> result;
        tableNames = new String[]{
                TABLE_NAME_POLICY,
                TABLE_NAME_MOTOR,
        };
        selections = new String[]{
                DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_POLICY_NO),
                SQL_DATE + DBUtils.parenthesise(DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_EFFECTIVE) + ",'unixepoch'"),
                SQL_DATE + DBUtils.parenthesise(DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_EXPIRY) + ",'unixepoch'"),
                DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_PREMIUM),
                DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_IS_VALID),
                DBUtils.dot(TABLE_NAME_MOTOR, TABLE_COLUMN_VEHICLE_PRICE),
        };
        conditions = new String[]{
                DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_POLICY_NO) + " = ",
                DBUtils.dot(TABLE_NAME_MOTOR, TABLE_COLUMN_POLICY_NO),
        };

        result = manager.selectDataFromTable(tableNames, selections, conditions, null);
        // Prints results in table form
        Printer.printAsList(new String[]{
                TABLE_COLUMN_POLICY_NO, TABLE_COLUMN_EFFECTIVE, TABLE_COLUMN_EXPIRY,
                TABLE_COLUMN_PREMIUM,
                TABLE_COLUMN_IS_VALID,
                TABLE_COLUMN_VEHICLE_PRICE,
        }, result);
    }

    /**
     * Selects and prints all available medical policies
     * Prints following: PolicyNo, EffectiveDate, ExpiryDate, Premium, IsValid, number of Beneficiaries
     * <p>
     * Query:
     * SELECT   policy.policy_no,
     * DATE(policy.effective,'unixepoch'),
     * DATE(policy.expiry,'unixepoch'),
     * policy.premium,
     * policy.is_valid,
     * (SELECT  count(*) From beneficiary as b1  WHERE  b1.policy_no = policy.policy_no AND b1.relation <> 'self') AS dependents
     * FROM     policy,
     * beneficiary
     * WHERE    policy.policy_no =  beneficiary.policy_no
     */
    public void selectAllMedicalPolicies() {
        // Array list to store query result
        ArrayList<Object[]> result;
        // Specify tables
        tableNames = new String[]{
                TABLE_NAME_POLICY,
                TABLE_NAME_BENEFICIARY,
        };
        // Specify selections
        selections = new String[]{
                DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_POLICY_NO),
                SQL_DATE + DBUtils.parenthesise(DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_EFFECTIVE) + ",'unixepoch'"),
                SQL_DATE + DBUtils.parenthesise(DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_EXPIRY) + ",'unixepoch'"),
                DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_PREMIUM),
                DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_IS_VALID),
                DBUtils.parenthesise(
                        DBUtils.constructSelectQuery(new String[]{TABLE_NAME_BENEFICIARY},
                        new String[]{SQL_COUNT + DBUtils.parenthesise(SQL_ALL)},
                        new String[]{DBUtils.dot(TABLE_NAME_BENEFICIARY, TABLE_COLUMN_POLICY_NO)
                                + " = " + DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_POLICY_NO),
                                " AND " + TABLE_COLUMN_RELATION + " <> 'self'"})),
        };
        // Specify conditions
        conditions = new String[]{
                DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_POLICY_NO) + " = ",
                DBUtils.dot(TABLE_NAME_BENEFICIARY, TABLE_COLUMN_POLICY_NO),
        };

        // Execute query
        result = manager.selectDataFromTable(tableNames, selections, conditions, null);
        // Prints results in table form
        Printer.printAsList(new String[]{
                TABLE_COLUMN_POLICY_NO, TABLE_COLUMN_EFFECTIVE, TABLE_COLUMN_EXPIRY,
                TABLE_COLUMN_PREMIUM,
                TABLE_COLUMN_IS_VALID,
                "dependencies",
        }, result);
    }

    /**
     * Selects and prints all policy numbers with premium between lower and upper
     * Prints following: PolicyNo, Premium
     * <p>
     * Query:
     * SELECT   policy_no,
     * premium
     * FROM     policy
     * WHERE    premium  BETWEEN  ? AND ?
     *
     * @param lower lower bound of range
     * @param upper upper bound of range
     */
    public void selectPoliciesPremiumRange(double lower, double upper) {
        tableNames = new String[]{TABLE_NAME_POLICY};
        selections = new String[]{TABLE_COLUMN_POLICY_NO, TABLE_COLUMN_PREMIUM};
        conditions = new String[]{TABLE_COLUMN_PREMIUM, SQL_BETWEEN, " ? AND ? "};
        values = new Object[]{lower, upper};
        ArrayList<Object[]> result;
        result = manager.selectDataFromTable(tableNames, selections, conditions, values);
        // Prints results in table form
        Printer.printAsTable(selections, result);
    }

    /**
     * Selects and prints all for each policy number, the number of claims, sum of claimed amounts, min and max claim amount
     * Prints following: PolicyNo, count, sum, min, max
     * <p>
     * Query:
     * SELECT   policy.policy_no,
     * (SELECT     IFNULL(count(claimed_amount),0)
     * from        claim
     * where       policy.policy_no = claim.policy_no),
     * <p>
     * (SELECT     IFNULL(sum(claimed_amount),0)
     * from        claim
     * where       policy.policy_no = claim.policy_no),
     * <p>
     * (SELECT     IFNULL(min(claimed_amount),0)
     * from        claim
     * where       policy.policy_no = claim.policy_no),
     * <p>
     * (SELECT     IFNULL(max(claimed_amount),0)
     * from        claim
     * where       policy.policy_no = claim.policy_no)
     * <p>
     * FROM     policy
     */
    public void selectPoliciesClaimsData() {
        tableNames = new String[]{TABLE_NAME_POLICY};
        selections = new String[]{
                DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_POLICY_NO),
                DBUtils.parenthesise(
                        DBUtils.constructSelectQuery(
                        new String[]{TABLE_NAME_CLAIM},
                        new String[]{SQL_IF_NULL + DBUtils.parenthesise(SQL_COUNT +
                                DBUtils.parenthesise(TABLE_COLUMN_CLAIMED_AMOUNT) + ",0")},
                        new String[]{DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_POLICY_NO) + " = " +
                                DBUtils.dot(TABLE_NAME_CLAIM, TABLE_COLUMN_POLICY_NO)}
                                )),
                DBUtils.parenthesise(
                        DBUtils.constructSelectQuery(
                        new String[]{TABLE_NAME_CLAIM},
                        new String[]{SQL_IF_NULL + DBUtils.parenthesise(SQL_SUM +
                                DBUtils.parenthesise(TABLE_COLUMN_CLAIMED_AMOUNT) + ",0")},
                        new String[]{DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_POLICY_NO) + " = " +
                                DBUtils.dot(TABLE_NAME_CLAIM, TABLE_COLUMN_POLICY_NO)})),
                DBUtils.parenthesise(
                        DBUtils.constructSelectQuery(
                        new String[]{TABLE_NAME_CLAIM},
                        new String[]{SQL_IF_NULL + DBUtils.parenthesise(SQL_MIN +
                                DBUtils.parenthesise(TABLE_COLUMN_CLAIMED_AMOUNT) + ",0")},
                        new String[]{DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_POLICY_NO) + " = " +
                                DBUtils.dot(TABLE_NAME_CLAIM, TABLE_COLUMN_POLICY_NO)})),
                DBUtils.parenthesise(
                        DBUtils.constructSelectQuery(
                        new String[]{TABLE_NAME_CLAIM},
                        new String[]{SQL_IF_NULL + DBUtils.parenthesise(SQL_MAX +
                                DBUtils.parenthesise(TABLE_COLUMN_CLAIMED_AMOUNT) + ",0")},
                        new String[]{DBUtils.dot(TABLE_NAME_POLICY, TABLE_COLUMN_POLICY_NO) + " = " +
                                DBUtils.dot(TABLE_NAME_CLAIM, TABLE_COLUMN_POLICY_NO)})),
        };
        ArrayList<Object[]> result;
        result = manager.selectDataFromTable(tableNames, selections, null, null);
        // Prints results in table form
        Printer.printAsTable(new String[]{
                TABLE_COLUMN_POLICY_NO, SQL_COUNT, SQL_SUM, SQL_MIN, SQL_MAX
        }, result);
    }
}
