package sqlite;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to handle table-specific insertions
 */
public class Inserter {
    /**
     * SQLiteManager object to access database methods
     */
    SQLiteManager manager;

    /**
     * Constructor that sets up the SQLiteManager object
     *
     * @param manager
     */
    public Inserter(SQLiteManager manager) {
        this.manager = manager;
    }

    /**
     * Adds general policy regardless of type to policy table and returns new policy number
     * Access is private since user can only add one of the given policy types
     *
     * @param values to add to table
     * @return new policy number
     */
    private String addGenericPolicy(Map<String, Object> values) {
        Object id = null;
        // Insert a new policy to policy table
        id = manager.insertDataInTable(Consts.TABLE_NAME_POLICY, new Object[]{
                null,
                values.get(Consts.TABLE_COLUMN_EFFECTIVE),
                values.get(Consts.TABLE_COLUMN_EXPIRY),
                0.0,
//                0,
                values.get(Consts.TABLE_COLUMN_POLICY_TYPE)
        });
        // Use returned policy id to get thew new policyNo
        ArrayList<Object[]> result = manager.selectDataFromTable(
                new String[]{Consts.TABLE_NAME_POLICY,},
                new String[]{Consts.TABLE_COLUMN_POLICY_NO,},
                new String[]{Consts.TABLE_COLUMN_ID + " = ?",},
                new Object[]{id}
        );
        // Return new policy number from result if found
        if (result != null && result.size() > 0)
            return (String) result.get(0)[0];
        return null;
    }

    /**
     * Adds a new travel policy by adding a policy first to policy table
     * then adding travel policy data to travel table
     *
     * @param values values of travel policy to add
     */
    public void addTravelPolicy(Map<String, Object> values) {
        Map<String, Object> valuesMap = new HashMap<>(values);
        valuesMap.put(Consts.TABLE_COLUMN_POLICY_TYPE, "travel");
        // Add new policy to policy data and get its new policy number
        String policyNo = addGenericPolicy(valuesMap);
        Object result = null;
        if (policyNo != null) {
            // Use received policyNo to add travel policy specific info
            result = manager.insertDataInTable(Consts.TABLE_NAME_TRAVEL, new Object[]{
                    null,
                    policyNo,
                    valuesMap.get(Consts.TABLE_COLUMN_DEPARTURE),
                    valuesMap.get(Consts.TABLE_COLUMN_DESTINATION),
                    valuesMap.get(Consts.TABLE_COLUMN_FAMILY),
            });
        }
        // If result is inserted notify with a message
        Printer.printPolicyAddedMessage(result, policyNo);
    }

    /**
     * Adds a new motor policy by adding a policy first to policy table
     * then adding motor policy data to motor table
     *
     * @param values values of motor policy to add
     */
    public void addMotorPolicy(Map<String, Object> values) {
        Map<String, Object> valuesMap = new HashMap<>(values);
        valuesMap.put(Consts.TABLE_COLUMN_POLICY_TYPE, "motor");
        // Add new policy to policy data and get its new policy number
        String policyNo = addGenericPolicy(valuesMap);
        Object result = null;
        if (policyNo != null) {
            // Use received policyNo to add motor policy specific info
            result = manager.insertDataInTable(Consts.TABLE_NAME_MOTOR, new Object[]{
                    null,
                    policyNo,
                    valuesMap.get(Consts.TABLE_COLUMN_VEHICLE_PRICE),
            });
        }
        // If result is inserted notify with a message
        Printer.printPolicyAddedMessage(result, policyNo);
    }

    /**
     * Adds a new medical policy by adding a policy first to policy table
     * then adding a beneficiary since medical policies have beneficiaries
     * as extra information and each medical policy requires at least one
     * beneficiary
     *
     * @param values of medical policy and correlated beneficiary to add
     */
    public void addMedicalPolicy(Map<String, Object> values) {
        Object result = null;
        Map<String, Object> valuesMap = new HashMap<>(values);
        valuesMap.put(Consts.TABLE_COLUMN_POLICY_TYPE, "medical");
        // Add new policy to policy data and get its new policy number
        String policyNo = addGenericPolicy(valuesMap);
        if (policyNo != null) {
            // Use received policyNo to add beneficiary since each medical policy
            // requires at least one beneficiary
            result = manager.insertDataInTable(Consts.TABLE_NAME_BENEFICIARY, new Object[]{
                    null,
                    values.get(Consts.TABLE_COLUMN_NAME),
                    values.get(Consts.TABLE_COLUMN_RELATION),
                    values.get(Consts.TABLE_COLUMN_GENDER),
                    values.get(Consts.TABLE_COLUMN_BIRTH_DATE),
                    policyNo,
            });

        }
        // If beneficiary was not added correctly delete the policy
        if (result == null) {
            manager.deleteDataFromTable(Consts.TABLE_NAME_POLICY,
                    new String[]{Consts.TABLE_COLUMN_POLICY_NO + " = ?",},
                    new Object[]{policyNo,}
            );
        }
        // If result is inserted notify with a message
        Printer.printPolicyAddedMessage(result, policyNo);
    }

    /**
     * Adds a new beneficiary to beneficiaries table
     *
     * @param values of beneficiary to add
     */
    private void addBeneficiary(Map<String, Object> values) {
        Object result = null;
        // Return ID of added beneficiary
        result = manager.insertDataInTable(Consts.TABLE_NAME_BENEFICIARY, new Object[]{
                null,
                values.get(Consts.TABLE_COLUMN_NAME),
                values.get(Consts.TABLE_COLUMN_RELATION),
                values.get(Consts.TABLE_COLUMN_GENDER),
                values.get(Consts.TABLE_COLUMN_BIRTH_DATE),
                values.get(Consts.TABLE_COLUMN_POLICY_NO),
        });
        // If result is inserted notify with a message
        Printer.printBeneficiaryAddedMessage(result);
    }

    /**
     * Adds a new claim to the claims table
     *
     * @param values of claim to add
     */
    public void addClaim(Map<String, Object> values) {
        Object result;
        // Return ID of added beneficiary
        result = manager.insertDataInTable(Consts.TABLE_NAME_CLAIM, new Object[]{
                null,
                values.get(Consts.TABLE_COLUMN_POLICY_NO),
                values.get(Consts.TABLE_COLUMN_INCURRED_DATE),
                values.get(Consts.TABLE_COLUMN_CLAIMED_AMOUNT),
        });
        // If result is inserted notify with a message
        Printer.printClaimAddedMessage(result, (String) values.get(Consts.TABLE_COLUMN_POLICY_NO));
    }
}
