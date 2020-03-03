package sqlite;

import java.util.ArrayList;

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
     * @param manager
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
                Consts.TABLE_NAME_POLICY,
                Consts.TABLE_NAME_TRAVEL,
        };
        selections = new String[]{
                DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_POLICY_NO),
                Consts.SQL_DATE + DBUtils.parenthesise(DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_EFFECTIVE) + ",'unixepoch'"),
                Consts.SQL_DATE + DBUtils.parenthesise(DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_EXPIRY) + ",'unixepoch'"),
                DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_PREMIUM),
                DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_IS_VALID),
                DBUtils.dot(Consts.TABLE_NAME_TRAVEL, Consts.TABLE_COLUMN_DEPARTURE),
                DBUtils.dot(Consts.TABLE_NAME_TRAVEL, Consts.TABLE_COLUMN_DESTINATION),
                DBUtils.dot(Consts.TABLE_NAME_TRAVEL, Consts.TABLE_COLUMN_FAMILY),
        };
        conditions = new String[]{DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_POLICY_NO) + " = ",
                DBUtils.dot(Consts.TABLE_NAME_TRAVEL, Consts.TABLE_COLUMN_POLICY_NO)};

        result = manager.selectDataFromTable(tableNames, selections, conditions, null
        );
        // Prints results in table form
        Printer.printAsList(new String[]{
                Consts.TABLE_COLUMN_POLICY_NO, Consts.TABLE_COLUMN_EFFECTIVE, Consts.TABLE_COLUMN_EXPIRY,
                Consts.TABLE_COLUMN_PREMIUM, Consts.TABLE_COLUMN_IS_VALID, Consts.TABLE_COLUMN_DEPARTURE,
                Consts.TABLE_COLUMN_DESTINATION, Consts.TABLE_COLUMN_FAMILY,
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
                Consts.TABLE_NAME_POLICY,
                Consts.TABLE_NAME_MOTOR,
        };
        selections = new String[]{
                DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_POLICY_NO),
                Consts.SQL_DATE + DBUtils.parenthesise(DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_EFFECTIVE) + ",'unixepoch'"),
                Consts.SQL_DATE + DBUtils.parenthesise(DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_EXPIRY) + ",'unixepoch'"),
                DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_PREMIUM),
                DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_IS_VALID),
                DBUtils.dot(Consts.TABLE_NAME_MOTOR, Consts.TABLE_COLUMN_VEHICLE_PRICE),
        };
        conditions = new String[]{
                DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_POLICY_NO) + " = ",
                DBUtils.dot(Consts.TABLE_NAME_MOTOR, Consts.TABLE_COLUMN_POLICY_NO),
        };

        result = manager.selectDataFromTable(tableNames, selections, conditions, null);
        // Prints results in table form
        Printer.printAsList(new String[]{
                Consts.TABLE_COLUMN_POLICY_NO, Consts.TABLE_COLUMN_EFFECTIVE, Consts.TABLE_COLUMN_EXPIRY,
                Consts.TABLE_COLUMN_PREMIUM, Consts.TABLE_COLUMN_IS_VALID, Consts.TABLE_COLUMN_VEHICLE_PRICE,
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
                Consts.TABLE_NAME_POLICY,
                Consts.TABLE_NAME_BENEFICIARY,
        };
        // Specify selections
        selections = new String[]{
                DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_POLICY_NO),
                Consts.SQL_DATE + DBUtils.parenthesise(DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_EFFECTIVE) + ",'unixepoch'"),
                Consts.SQL_DATE + DBUtils.parenthesise(DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_EXPIRY) + ",'unixepoch'"),
                DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_PREMIUM),
                DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_IS_VALID),
                DBUtils.constructSelectQuery(new String[]{Consts.TABLE_NAME_BENEFICIARY},
                        new String[]{Consts.SQL_COUNT + DBUtils.parenthesise(Consts.SQL_ALL)},
                        new String[]{DBUtils.dot(Consts.TABLE_NAME_BENEFICIARY, Consts.TABLE_COLUMN_POLICY_NO)
                                + " = " + DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_POLICY_NO),
                                " AND " + Consts.TABLE_COLUMN_RELATION + " <> 'self'"}),
        };
        // Specify conditions
        conditions = new String[]{
                DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_POLICY_NO) + " = ",
                DBUtils.dot(Consts.TABLE_NAME_BENEFICIARY, Consts.TABLE_COLUMN_POLICY_NO),
        };

        // Execute query
        result = manager.selectDataFromTable(tableNames, selections, conditions, null);
        // Prints results in table form
        Printer.printAsList(new String[]{
                Consts.TABLE_COLUMN_POLICY_NO, Consts.TABLE_COLUMN_EFFECTIVE, Consts.TABLE_COLUMN_EXPIRY,
                Consts.TABLE_COLUMN_PREMIUM, Consts.TABLE_COLUMN_IS_VALID, "dependencies",
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
        tableNames = new String[]{Consts.TABLE_NAME_POLICY};
        selections = new String[]{Consts.TABLE_COLUMN_POLICY_NO, Consts.TABLE_COLUMN_PREMIUM};
        conditions = new String[]{Consts.TABLE_COLUMN_PREMIUM, Consts.SQL_BETWEEN, " ? AND ? "};
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
        tableNames = new String[]{Consts.TABLE_NAME_POLICY};
        selections = new String[]{
                DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_POLICY_NO),
                DBUtils.parenthesise(
                        DBUtils.constructSelectQuery(
                        new String[]{Consts.TABLE_NAME_CLAIM},
                        new String[]{Consts.SQL_IF_NULL + DBUtils.parenthesise(Consts.SQL_COUNT +
                                DBUtils.parenthesise(Consts.TABLE_COLUMN_CLAIMED_AMOUNT) + ",0")},
                        new String[]{DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_POLICY_NO) + " = " +
                                DBUtils.dot(Consts.TABLE_NAME_CLAIM, Consts.TABLE_COLUMN_POLICY_NO)}
                                )),
                DBUtils.parenthesise(
                        DBUtils.constructSelectQuery(
                        new String[]{Consts.TABLE_NAME_CLAIM},
                        new String[]{Consts.SQL_IF_NULL + DBUtils.parenthesise(Consts.SQL_SUM +
                                DBUtils.parenthesise(Consts.TABLE_COLUMN_CLAIMED_AMOUNT) + ",0")},
                        new String[]{DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_POLICY_NO) + " = " +
                                DBUtils.dot(Consts.TABLE_NAME_CLAIM, Consts.TABLE_COLUMN_POLICY_NO)})),
                DBUtils.parenthesise(
                        DBUtils.constructSelectQuery(
                        new String[]{Consts.TABLE_NAME_CLAIM},
                        new String[]{Consts.SQL_IF_NULL + DBUtils.parenthesise(Consts.SQL_MIN +
                                DBUtils.parenthesise(Consts.TABLE_COLUMN_CLAIMED_AMOUNT) + ",0")},
                        new String[]{DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_POLICY_NO) + " = " +
                                DBUtils.dot(Consts.TABLE_NAME_CLAIM, Consts.TABLE_COLUMN_POLICY_NO)})),
                DBUtils.parenthesise(
                        DBUtils.constructSelectQuery(
                        new String[]{Consts.TABLE_NAME_CLAIM},
                        new String[]{Consts.SQL_IF_NULL + DBUtils.parenthesise(Consts.SQL_MAX +
                                DBUtils.parenthesise(Consts.TABLE_COLUMN_CLAIMED_AMOUNT) + ",0")},
                        new String[]{DBUtils.dot(Consts.TABLE_NAME_POLICY, Consts.TABLE_COLUMN_POLICY_NO) + " = " +
                                DBUtils.dot(Consts.TABLE_NAME_CLAIM, Consts.TABLE_COLUMN_POLICY_NO)})),
        };
        ArrayList<Object[]> result;
        result = manager.selectDataFromTable(tableNames, selections, null, null);
        // Prints results in table form
        Printer.printAsTable(new String[]{
                Consts.TABLE_COLUMN_POLICY_NO, Consts.SQL_COUNT, Consts.SQL_SUM, Consts.SQL_MIN, Consts.SQL_MAX
        }, result);
    }
}
