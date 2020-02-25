package com.sqlite;


import com.others.Constants;

import java.util.ArrayList;
import java.util.Map;

/**
 * Class to handle table-specific insertions
 */
public class Inserter {
    /**
     * Adds general policy regardless of type to policy table and returns new policy number
     * Access is private since user can only add one of the given policy types
     *
     * @param values to add to tab;e
     * @return new policy number
     */
    private static String addGenericPolicy(Map<String, Object> values) {
        Long id;
        // Insert a new policy to policy table
        id = SQLiteManager.insertDataInTable(Constants.TABLE_NAME_POLICY, new Object[]{
                null,
                values.get(Constants.TABLE_COLUMN_EFFECTIVE),
                values.get(Constants.TABLE_COLUMN_EXPIRY),
                0.0,
                values.get(Constants.TABLE_COLUMN_IS_VALID),
                values.get(Constants.TABLE_COLUMN_POLICY_TYPE)
        });
        // Use returned policy id to get thew new policyNo
        ArrayList<Object[]> result = SQLiteManager.selectDataFromTable(
                new String[]{Constants.TABLE_NAME_POLICY,},
                new String[]{Constants.TABLE_COLUMN_POLICY_NO,},
                new String[]{Constants.TABLE_COLUMN_ID + " = ?",},
                new Object[]{id}
        );
        // Return new policy number from result
        return (String) result.get(0)[0];

    }

    /**
     * Adds a new travel policy by adding a policy first to policy table
     * then adding travel policy data to travel table
     *
     * @param values values of travel policy to add
     */
    public static void addTravelPolicy(Map<String, Object> values) {

        // Add new policy to policy data and get its new policy number
        String policyNo = addGenericPolicy(values);
        // Use received policyNo to add travel policy specific info
        Long result = SQLiteManager.insertDataInTable(Constants.TABLE_NAME_TRAVEL, new Object[]{
                null,
                policyNo,
                values.get(Constants.TABLE_COLUMN_DEPARTURE),
                values.get(Constants.TABLE_COLUMN_DESTINATION),
                values.get(Constants.TABLE_COLUMN_FAMILY),
        });
        // If result is inserted notify with a message
        Printer.showPolicyAddedMessage(result, policyNo);
    }

    /**
     * Adds a new motor policy by adding a policy first to policy table
     * then adding motor policy data to motor table
     *
     * @param values values of motor policy to add
     */
    public static void addMotorPolicy(Map<String, Object> values) {
        // Add new policy to policy data and get its new policy number
        String policyNo = addGenericPolicy(values);
        // Use received policyNo to add motor policy specific info
        Long result = SQLiteManager.insertDataInTable(Constants.TABLE_NAME_MOTOR, new Object[]{
                null,
                policyNo,
                values.get(Constants.TABLE_COLUMN_VEHICLE_PRICE),
        });
        // If result is inserted notify with a message
        Printer.showPolicyAddedMessage(result, policyNo);
    }

    /**
     * Adds a new medical policy by adding a policy first to policy table
     * then adding a beneficiary since medical policies have beneficiaries
     * as extra information and each medical policy requires at least one
     * beneficiary
     *
     * @param values of medical policy and correlated beneficiary to add
     */
    public static void addMedicalPolicy(Map<String, Object> values) {
        // Add new policy to policy data and get its new policy number
        String policyNo = addGenericPolicy(values);
        // Use received policyNo to add beneficiary since each medical policy
        // requires at least one beneficiary
        Long result = addBeneficiary(Map.ofEntries(
                Map.entry(Constants.TABLE_COLUMN_NAME, values.get(Constants.TABLE_COLUMN_NAME)),
                Map.entry(Constants.TABLE_COLUMN_RELATION, values.get(Constants.TABLE_COLUMN_RELATION)),
                Map.entry(Constants.TABLE_COLUMN_GENDER, values.get(Constants.TABLE_COLUMN_GENDER)),
                Map.entry(Constants.TABLE_COLUMN_BIRTH_DATE, values.get(Constants.TABLE_COLUMN_BIRTH_DATE)),
                Map.entry(Constants.TABLE_COLUMN_POLICY_NO, policyNo)
        ));
        // If result is inserted notify with a message
        Printer.showPolicyAddedMessage(result, policyNo);
        // If beneficiary was not added correctly delete the policy
        if (result == 0F) {
            SQLiteManager.deleteDataFromTable(Constants.TABLE_NAME_POLICY,
                    new String[]{Constants.TABLE_COLUMN_POLICY_NO + " = ?",},
                    new Object[]{policyNo,}
            );
        }
    }

    /**
     * Adds a new beneficiary to beneficiaries table
     *
     * @param values of beneficiary to add
     */
    private static Long addBeneficiary(Map<String, Object> values) {
        // Check there is already a 'self' relation for the current policyNo
        if (values.get(Constants.TABLE_COLUMN_RELATION).equals("self")) {
            // If number of records with relation 'self' and current policy number is greater than zero, abort.
            ArrayList<Object[]> result = SQLiteManager.selectDataFromTable(
                    new String[]{Constants.TABLE_NAME_BENEFICIARY,},
                    new String[]{"COUNT(*)",},
                    new String[]{Constants.TABLE_COLUMN_POLICY_NO + " = ? AND ", Constants.TABLE_COLUMN_RELATION + " = ? "},
                    new Object[]{values.get(Constants.TABLE_COLUMN_POLICY_NO), "self"}
            );
            if ((int) result.get(0)[0] > 0) {
                System.out.println(Constants.MESSAGE_BENEFICIARY_SELF_ERROR);
                return 0L;
            }
        }
        // Return ID of added beneficiary
        return SQLiteManager.insertDataInTable(Constants.TABLE_NAME_BENEFICIARY, new Object[]{
                null,
                values.get(Constants.TABLE_COLUMN_NAME),
                values.get(Constants.TABLE_COLUMN_RELATION),
                values.get(Constants.TABLE_COLUMN_GENDER),
                values.get(Constants.TABLE_COLUMN_BIRTH_DATE),
                values.get(Constants.TABLE_COLUMN_POLICY_NO),
        });
    }

    /**
     * Adds a new claim to the claims table
     *
     * @param values of claim to add
     */
    public static void addClaim(Map<String, Object> values) {
        SQLiteManager.insertDataInTable(Constants.TABLE_NAME_CLAIM, new Object[]{
                null,
                Constants.TABLE_COLUMN_INCURRED_DATE,
                Constants.TABLE_COLUMN_POLICY_NO,
                Constants.TABLE_COLUMN_CLAIMED_AMOUNT,
        });
    }
}
