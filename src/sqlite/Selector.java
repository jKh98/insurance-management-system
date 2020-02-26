package sqlite;

import others.Constants;

import java.util.ArrayList;

/**
 * Contains methods used to select and print specific data from specific tables usually based on requirements
 */
public class Selector {

    /**
     * Selects and prints all available travel policies
     * Prints following: PolicyNo, EffectiveDate, ExpiryDate, Premium, IsValid, DepartureCountry, DestinationCountry, Family
     *
     * Query:
     * SELECT policy.policy_no,
     *        DATE(policy.effective,'unixepoch'),
     *        DATE(policy.expiry,'unixepoch'),
     *        policy.premium,policy.is_valid,
     *        travel.departure,
     *        travel.destination,
     *        travel.family
     * FROM   policy,
     *        travel
     * WHERE  policy.policy_no =  travel.policy_no
     */
    public static void selectAllTravelPolicies() {
        // Array list to store query result
        ArrayList<Object[]> result;
        result = SQLiteManager.selectDataFromTable(
                new String[]{
                        Constants.TABLE_NAME_POLICY,
                        Constants.TABLE_NAME_TRAVEL,
                },
                new String[]{
                        Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_POLICY_NO,
                        "DATE(" + Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_EFFECTIVE + ",'unixepoch')",
                        "DATE(" + Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_EXPIRY + ",'unixepoch')",
                        Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_PREMIUM,
                        Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_IS_VALID,
                        Constants.TABLE_NAME_TRAVEL + "." + Constants.TABLE_COLUMN_DEPARTURE,
                        Constants.TABLE_NAME_TRAVEL + "." + Constants.TABLE_COLUMN_DESTINATION,
                        Constants.TABLE_NAME_TRAVEL + "." + Constants.TABLE_COLUMN_FAMILY,
                },
                new String[]{Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_POLICY_NO + " = ",
                        Constants.TABLE_NAME_TRAVEL + "." + Constants.TABLE_COLUMN_POLICY_NO},
                null
        );
        // Prints results in table form
        Printer.printAsList(new String[]{
                Constants.TABLE_COLUMN_POLICY_NO, Constants.TABLE_COLUMN_EFFECTIVE, Constants.TABLE_COLUMN_EXPIRY,
                Constants.TABLE_COLUMN_PREMIUM, Constants.TABLE_COLUMN_IS_VALID, Constants.TABLE_COLUMN_DEPARTURE,
                Constants.TABLE_COLUMN_DESTINATION, Constants.TABLE_COLUMN_FAMILY,
        }, result);
    }

    /**
     * Selects and prints all available motor policies
     * Prints following: PolicyNo, EffectiveDate, ExpiryDate, Premium, IsValid, VehiclePrice
     *
     * Query:
     * SELECT   policy.policy_no,
     *          DATE(policy.effective,'unixepoch'),
     *          DATE(policy.expiry,'unixepoch'),
     *          policy.premium,
     *          policy.is_valid,
     *          motor.vehicle_price
     * FROM     policy,
     *          motor
     * WHERE    policy.policy_no =  motor.policy_no
     */
    public static void selectAllMotorPolicies() {
        // Array list to store query result
        ArrayList<Object[]> result;
        result = SQLiteManager.selectDataFromTable(new String[]{
                Constants.TABLE_NAME_POLICY,
                Constants.TABLE_NAME_MOTOR,
        }, new String[]{
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_POLICY_NO,
                "DATE(" + Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_EFFECTIVE + ",'unixepoch')",
                "DATE(" + Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_EXPIRY + ",'unixepoch')",
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_PREMIUM,
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_IS_VALID,
                Constants.TABLE_NAME_MOTOR + "." + Constants.TABLE_COLUMN_VEHICLE_PRICE,
        }, new String[]{
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_POLICY_NO + " = ",
                Constants.TABLE_NAME_MOTOR + "." + Constants.TABLE_COLUMN_POLICY_NO,
        }, null);
        // Prints results in table form
        Printer.printAsList(new String[]{
                Constants.TABLE_COLUMN_POLICY_NO, Constants.TABLE_COLUMN_EFFECTIVE, Constants.TABLE_COLUMN_EXPIRY,
                Constants.TABLE_COLUMN_PREMIUM, Constants.TABLE_COLUMN_IS_VALID, Constants.TABLE_COLUMN_VEHICLE_PRICE,
        }, result);
    }

    /**
     * Selects and prints all available medical policies
     * Prints following: PolicyNo, EffectiveDate, ExpiryDate, Premium, IsValid, number of Beneficiaries
     *
     * Query:
     * SELECT   policy.policy_no,
     *          DATE(policy.effective,'unixepoch'),
     *          DATE(policy.expiry,'unixepoch'),
     *          policy.premium,
     *          policy.is_valid,
     *          (SELECT  count(*) From beneficiary as b1  WHERE  b1.policy_no = policy.policy_no AND b1.relation <> 'self') AS dependents
     * FROM     policy,
     *          beneficiary
     * WHERE    policy.policy_no =  beneficiary.policy_no
     */
    public static void selectAllMedicalPolicies() {
        // Array list to store query result
        ArrayList<Object[]> result;
        result = SQLiteManager.selectDataFromTable(new String[]{
                Constants.TABLE_NAME_POLICY,
                Constants.TABLE_NAME_BENEFICIARY,
        }, new String[]{
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_POLICY_NO,
                "DATE(" + Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_EFFECTIVE + ",'unixepoch')",
                "DATE(" + Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_EXPIRY + ",'unixepoch')",
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_PREMIUM,
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_IS_VALID,
                "(" + Constants.SQL_SELECT + " count(*) From " + Constants.TABLE_NAME_BENEFICIARY +
                        " as b1 " + Constants.SQL_WHERE + " b1." + Constants.TABLE_COLUMN_POLICY_NO +
                        " = " + Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_POLICY_NO +
                        " AND b1." + Constants.TABLE_COLUMN_RELATION + " <> 'self') AS dependents ",
        }, new String[]{
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_POLICY_NO + " = ",
                Constants.TABLE_NAME_BENEFICIARY + "." + Constants.TABLE_COLUMN_POLICY_NO,
        }, null);
        // Prints results in table form
        Printer.printAsList(new String[]{
                Constants.TABLE_COLUMN_POLICY_NO, Constants.TABLE_COLUMN_EFFECTIVE, Constants.TABLE_COLUMN_EXPIRY,
                Constants.TABLE_COLUMN_PREMIUM, Constants.TABLE_COLUMN_IS_VALID, "dependencies",
        }, result);
    }

    /**
     * Selects and prints all policy numbers with premium between lower and upper
     * Prints following: PolicyNo, Premium
     *
     * Query:
     * SELECT   policy_no,
     *          premium
     * FROM     policy
     * WHERE    premium  BETWEEN  ? AND ?
     *
     * @param lower lower bound of range
     * @param upper upper bound of range
     */
    public static void selectPoliciesPremiumRange(double lower, double upper) {
        String[] selections = new String[]{Constants.TABLE_COLUMN_POLICY_NO, Constants.TABLE_COLUMN_PREMIUM};
        ArrayList<Object[]> result;
        result = SQLiteManager.selectDataFromTable(new String[]{Constants.TABLE_NAME_POLICY},
                selections,
                new String[]{Constants.TABLE_COLUMN_PREMIUM, Constants.SQL_BETWEEN, " ? AND ? "},
                new Object[]{lower, upper});
        // Prints results in table form
        Printer.printAsTable(selections, result);
    }

    /**
     * Selects and prints all for each policy number, the number of claims, sum of claimed amounts, min and max claim amount
     * Prints following: PolicyNo, count, sum, min, max
     *
     * Query:
     * SELECT   policy.policy_no,
     *          (SELECT     count(claimed_amount)
     *           from       claim
     *           where      policy.policy_no = claim.policy_no),
     *
     *          (SELECT     sum(claimed_amount)
     *          from        claim
     *          where       policy.policy_no = claim.policy_no),
     *
     *          (SELECT     min(claimed_amount)
     *          from        claim
     *          where       policy.policy_no = claim.policy_no),
     *
     *          (SELECT     max(claimed_amount)
     *          from        claim
     *          where       policy.policy_no = claim.policy_no)
     *
     * FROM     policy
     */
    public static void selectPoliciesClaimsData() {
        ArrayList<Object[]> result;
        result = SQLiteManager.selectDataFromTable(new String[]{Constants.TABLE_NAME_POLICY},
                new String[]{
                        "" + Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_POLICY_NO + "",
                        "(" + Constants.SQL_SELECT + " " + Constants.SQL_COUNT + "(" + Constants.TABLE_COLUMN_CLAIMED_AMOUNT + ") from " + Constants.TABLE_NAME_CLAIM + " where " + Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_POLICY_NO + " = " + Constants.TABLE_NAME_CLAIM + "." + Constants.TABLE_COLUMN_POLICY_NO + ")",
                        "(" + Constants.SQL_SELECT + " " + Constants.SQL_SUM + "(" + Constants.TABLE_COLUMN_CLAIMED_AMOUNT + ") from " + Constants.TABLE_NAME_CLAIM + " where " + Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_POLICY_NO + " = " + Constants.TABLE_NAME_CLAIM + "." + Constants.TABLE_COLUMN_POLICY_NO + ")",
                        "(" + Constants.SQL_SELECT + " " + Constants.SQL_MIN + "(" + Constants.TABLE_COLUMN_CLAIMED_AMOUNT + ") from " + Constants.TABLE_NAME_CLAIM + " where " + Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_POLICY_NO + " = " + Constants.TABLE_NAME_CLAIM + "." + Constants.TABLE_COLUMN_POLICY_NO + ")",
                        "(" + Constants.SQL_SELECT + " " + Constants.SQL_MAX + "(" + Constants.TABLE_COLUMN_CLAIMED_AMOUNT + ") from " + Constants.TABLE_NAME_CLAIM + " where " + Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_POLICY_NO + " = " + Constants.TABLE_NAME_CLAIM + "." + Constants.TABLE_COLUMN_POLICY_NO + ")"
                },
                null, null);
        // Prints results in table form
        Printer.printAsTable(new String[]{
                Constants.TABLE_COLUMN_POLICY_NO, Constants.SQL_COUNT, Constants.SQL_SUM, Constants.SQL_MIN, Constants.SQL_MAX
        }, result);
    }
}
