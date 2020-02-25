package com.sqlite;

import com.others.Constants;

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
        // List of columns to be selected from tables
        String[] selections = new String[]{
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_POLICY_NO,
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_EFFECTIVE,
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_EXPIRY,
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_PREMIUM,
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_IS_VALID,
                Constants.TABLE_NAME_TRAVEL + "." + Constants.TABLE_COLUMN_DEPARTURE,
                Constants.TABLE_NAME_TRAVEL + "." + Constants.TABLE_COLUMN_DESTINATION,
                Constants.TABLE_NAME_TRAVEL + "." + Constants.TABLE_COLUMN_FAMILY,
        };
        // Array list to store query result
        ArrayList<Object[]> result;
        result = SQLiteManager.selectDataFromTable(
                new String[]{
                        Constants.TABLE_NAME_POLICY,
                        Constants.TABLE_NAME_TRAVEL,
                },
                selections,
                new String[]{Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_POLICY_NO + " = ",
                        Constants.TABLE_NAME_TRAVEL + "." + Constants.TABLE_COLUMN_POLICY_NO},
                null
        );
        // Prints results in table form
        Printer.printTable(selections, result);
    }

    /**
     * Selects and prints all available motor policies
     * Prints following: PolicyNo, EffectiveDate, ExpiryDate, Premium, IsValid, VehiclePrice
     */
    public static void selectAllMotorPolicies() {
        // List of columns to be selected from tables
        String[] selections = new String[]{
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_POLICY_NO,
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_EFFECTIVE,
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_EXPIRY,
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_PREMIUM,
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_IS_VALID,
                Constants.TABLE_NAME_MOTOR + "." + Constants.TABLE_COLUMN_VEHICLE_PRICE,
        };
        // Array list to store query result
        ArrayList<Object[]> result;
        result = SQLiteManager.selectDataFromTable(new String[]{
                Constants.TABLE_NAME_POLICY,
                Constants.TABLE_NAME_MOTOR,
        }, selections, new String[]{
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_POLICY_NO + " = ",
                Constants.TABLE_NAME_MOTOR + "." + Constants.TABLE_COLUMN_POLICY_NO,
        }, null);
        // Prints results in table form
        Printer.printTable(selections, result);
    }

    /**
     * Selects and prints all available medical policies
     * Prints following: PolicyNo, EffectiveDate, ExpiryDate, Premium, IsValid, number of Beneficiaries
     */
    public static void selectAllMedicalPolicies() {
        // List of columns to be selected from tables
        String[] selections = new String[]{
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_POLICY_NO,
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_EFFECTIVE,
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_EXPIRY,
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_PREMIUM,
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_IS_VALID,
                "(" + Constants.SQL_SELECT + " count(*) From " + Constants.TABLE_NAME_BENEFICIARY +
                        " as b1 " + Constants.SQL_WHERE + " b1." + Constants.TABLE_COLUMN_POLICY_NO +
                        " = " + Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_POLICY_NO +
                        " AND b1." + Constants.TABLE_COLUMN_RELATION + " <> 'self') AS dependents ",
        };
        // Array list to store query result
        ArrayList<Object[]> result;
        result = SQLiteManager.selectDataFromTable(new String[]{
                Constants.TABLE_NAME_POLICY,
                Constants.TABLE_NAME_BENEFICIARY,
        }, selections, new String[]{
                Constants.TABLE_NAME_POLICY + "." + Constants.TABLE_COLUMN_POLICY_NO + " = ",
                Constants.TABLE_NAME_BENEFICIARY + "." + Constants.TABLE_COLUMN_POLICY_NO,
        }, null);
        // Prints results in table form
        Printer.printTable(selections, result);
    }
}
