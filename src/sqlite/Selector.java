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
                        Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_EFFECTIVE,
                        Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_EXPIRY,
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
     */
    public static void selectAllMotorPolicies() {
        // Array list to store query result
        ArrayList<Object[]> result;
        result = SQLiteManager.selectDataFromTable(new String[]{
                Constants.TABLE_NAME_POLICY,
                Constants.TABLE_NAME_MOTOR,
        }, new String[]{
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_POLICY_NO,
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_EFFECTIVE,
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_EXPIRY,
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
     */
    public static void selectAllMedicalPolicies() {
        // Array list to store query result
        ArrayList<Object[]> result;
        result = SQLiteManager.selectDataFromTable(new String[]{
                Constants.TABLE_NAME_POLICY,
                Constants.TABLE_NAME_BENEFICIARY,
        }, new String[]{
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_POLICY_NO,
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_EFFECTIVE,
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_EXPIRY,
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
}
