package com.company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Class to handle table-specific insertions
 */
public class InsertToTable {
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
                values.get(Constants.TABLE_COLUMN_POLICY_TYPE),
        });
        // Use returned policy id to get thew new policyNo
        ArrayList<Object[]> result = SQLiteManager.selectDataFromTable(
                new String[]{Constants.TABLE_NAME_POLICY,},
                new String[]{Constants.TABLE_COLUMN_POLICY_NO,},
                new String[]{Constants.TABLE_COLUMN_ID + " = ?",},
                new Object[]{id}
        );
        // Get new policy number from result
        String policyNo = (String) result.get(0)[0];
        System.out.println(policyNo);
        return policyNo;

    }

    /**
     * Adds a new travel policy by adding a policy first to policy table
     * then adding travel policy data to travel table
     *
     * @param values values of travel policy to add
     */
    static void addTravelPolicy(Map<String, Object> values) {

        // Add new policy to policy data and get its new policy number
        String policyNo = addGenericPolicy(values);
        // Use received policyNo to add travel policy specific info
        SQLiteManager.insertDataInTable(Constants.TABLE_NAME_TRAVEL, new Object[]{
                null,
                policyNo,
                values.get(Constants.TABLE_COLUMN_DEPARTURE),
                values.get(Constants.TABLE_COLUMN_DESTINATION),
                values.get(Constants.TABLE_COLUMN_FAMILY),
        });

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
        SQLiteManager.insertDataInTable(Constants.TABLE_NAME_MOTOR, new Object[]{
                null,
                policyNo,
                values.get(Constants.TABLE_COLUMN_VEHICLE_PRICE),
        });
    }

    public static void addMedicalPolicy(Map<String, Object> values) {

    }

    public static void addBeneficiary(Map<String, Object> values) {

    }

    public static void addClaim(Map<String, Object> values) {

    }
}
