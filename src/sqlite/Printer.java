package sqlite;


import others.Constants;
import others.Utils;

import java.util.ArrayList;

/**
 * Contains methods used to print messages, errors, and query results
 */
class Printer {

    /**
     * Prints the name of the jdbc driver
     *
     * @param driverName jdbc driver
     */
    public static void printDriverName(String driverName) {
        System.out.println(Constants.MESSAGE_DB_DRIVER + driverName);
    }

    /**
     * Prints jdbc connection message
     *
     * @param url database url
     */
    public static void printDBConnection(String url) {
        System.out.println(Constants.MESSAGE_OPENED_DB + url);
    }

    /**
     * Prints DB close message
     */
    public static void printCloseDBMessage() {
        System.out.println(Constants.MESSAGE_CLOSED_DB);
    }

    /**
     * Prints table added message
     *
     * @param tableName
     */
    public static void printTableAddedMessage(String tableName) {
        System.out.println(tableName + Constants.MESSAGE_TABLE_ADDED);
    }

    /**
     * Prints trigger added to table message
     *
     * @param tableName
     * @param triggerName
     */
    public static void printTriggerAddedMessage(String tableName, String triggerName) {
        System.out.println(Constants.MESSAGE_TRIGGER + triggerName + Constants.MESSAGE_TRIGGER_ADDED + tableName);
    }

    /**
     * Prints query result which consists of a 2D array into list form
     *
     * @param header table header or column names
     * @param result 2D array containing query result
     */
    public static void printAsList(String[] header, ArrayList<Object[]> result) {
        System.out.println("\n");
        if (result.size() > 0) {
            // Print table data row by row
            for (Object[] row : result) {
                for (int i = 0; i < row.length; i++) {
                    System.out.format("%-20s%-20s\n", header[i] + ": ", row[i]);
                }
                System.out.println("\n");
            }
        } else {
            System.out.println(Constants.MESSAGE_NO_RESULTS);
        }
    }

    /**
     * Prints query result which consists of a 2D array into table form
     *
     * @param header table header or column names
     * @param result 2D array containing query result
     */
    public static void printAsTable(String[] header, ArrayList<Object[]> result) {
        System.out.println("\n");
        if (result.size() > 0) {
            // Get column spacing based on number of columns
            String spacing = Utils.getFormatSpacing(result.get(0).length);
            // Print header
            System.out.format(spacing + "\n", (Object[]) header);
            // Print table data row by row
            for (Object[] row : result) {
                System.out.format(spacing + "\n", row);
            }
        } else {
            System.out.println(Constants.MESSAGE_NO_RESULTS);
        }
        System.out.println("\n");
    }

    /**
     * Prints message if policy is added and error otherwise
     *
     * @param result   result of INSERT query. Value is 0L if insert was not successful
     * @param policyNo string value of PolicyNo
     */
    static void printPolicyAddedMessage(Object result, String policyNo) {
        // If result is Long then insert returned an ID and hence was successful
        if (result instanceof Long) {
            System.out.println(Constants.TABLE_NAME_POLICY + Constants.MESSAGE_SPACE + policyNo + Constants.MESSAGE_SUCCESSFULLY_ADDED);
        } else if (result instanceof String) {
            // if result is a string then it is definitely an error message
            System.out.println(result);
        } else {
            System.out.println(Constants.MESSAGE_INVALID_POLICY);
        }
    }

    /**
     * Prints message if beneficiary is added and error otherwise
     *
     * @param result result of INSERT query.
     */
    public static void printBeneficiaryAddedMessage(Object result) {
        // If result is Long then insert returned an ID and hence was successful
        if (result instanceof Long) {
            System.out.println(Constants.TABLE_NAME_BENEFICIARY + Constants.MESSAGE_SUCCESSFULLY_ADDED);
        } else if (result instanceof String) {
            // if result is a string then it is definitely an error message
            System.out.println(result);
        } else {
            System.out.println(Constants.MESSAGE_INVALID_BENEFICIARY);
        }
    }

    public static void printClaimAddedMessage(Object result) {
        // If result is Long then insert returned an ID and hence was successful
        if (result instanceof Long) {
            System.out.println(Constants.TABLE_NAME_CLAIM + Constants.MESSAGE_SUCCESSFULLY_ADDED);
        } else if (result instanceof String) {
            // if result is a string then it is definitely an error message
            System.out.println(result);
        } else {
            System.out.println(Constants.MESSAGE_INVALID_CLAIM);
        }
    }
}
