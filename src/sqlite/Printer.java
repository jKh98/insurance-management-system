package sqlite;


import others.Consts;

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
        System.out.println(Consts.MESSAGE_DB_DRIVER + driverName);
    }

    /**
     * Prints jdbc connection message
     *
     * @param url database url
     */
    public static void printDBConnection(String url) {
        System.out.println(Consts.MESSAGE_OPENED_DB + url);
    }

    /**
     * Prints DB close message
     */
    public static void printCloseDBMessage() {
        System.out.println(Consts.MESSAGE_CLOSED_DB);
    }

    /**
     * Prints table added message
     *
     * @param tableName
     */
    public static void printTableAddedMessage(String tableName) {
        System.out.println(tableName + Consts.MESSAGE_TABLE_ADDED);
    }

    /**
     * Prints trigger added to table message
     *
     * @param tableName
     * @param triggerName
     */
    public static void printTriggerAddedMessage(String tableName, String triggerName) {
        System.out.println(Consts.MESSAGE_TRIGGER + triggerName + Consts.MESSAGE_TRIGGER_ADDED + tableName);
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
            System.out.println(Consts.MESSAGE_NO_RESULTS);
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
            String spacing = getFormatSpacing(result.get(0).length);
            // Print header
            System.out.format(spacing + "\n", (Object[]) header);
            // Print table data row by row
            for (Object[] row : result) {
                System.out.format(spacing + "\n", row);
            }
        } else {
            System.out.println(Consts.MESSAGE_NO_RESULTS);
        }
        System.out.println("\n");
    }

    /**
     * Prints message if policy is added and error otherwise
     *
     * @param result   result of INSERT query.
     * @param policyNo string value of PolicyNo
     */
    static void printPolicyAddedMessage(Object result, String policyNo) {
        // If result is a number then insert returned an ID and hence was successful
        if ((result instanceof Long || result instanceof Integer) && policyNo != null) {
            System.out.println(Consts.TABLE_NAME_POLICY + Consts.MESSAGE_SPACE + policyNo + Consts.MESSAGE_SUCCESSFULLY_ADDED);
        } else if (result instanceof String) {
            // if result is a string then it is definitely an error message
            System.out.println(result);
        } else {
            System.out.println(Consts.MESSAGE_INVALID_POLICY);
        }
    }

    /**
     * Prints message if beneficiary is added and error otherwise
     *
     * @param result result of INSERT query.
     */
    public static void printBeneficiaryAddedMessage(Object result) {
        // If result is a number then insert returned an ID and hence was successful
        if (result instanceof Long || result instanceof Integer) {
            System.out.println(Consts.TABLE_NAME_BENEFICIARY + Consts.MESSAGE_SUCCESSFULLY_ADDED);
        } else if (result instanceof String) {
            // if result is a string then it is definitely an error message
            System.out.println(result);
        } else {
            System.out.println(Consts.MESSAGE_INVALID_BENEFICIARY);
        }
    }

    public static void printClaimAddedMessage(Object result) {
        // If result is a number then insert returned an ID and hence was successful
        if (result instanceof Long || result instanceof Integer) {
            System.out.println(Consts.TABLE_NAME_CLAIM + Consts.MESSAGE_SUCCESSFULLY_ADDED);
        } else if (result instanceof String) {
            // if result is a string then it is definitely an error message
            System.out.println(result);
        } else {
            System.out.println(Consts.MESSAGE_INVALID_CLAIM);
        }
    }

    /**
     * Returns a formatting string based on given table
     *
     * @param length number of table columns
     * @return format string
     */
    private static String getFormatSpacing(int length) {
        StringBuilder s = new StringBuilder("%-20s");
        while (--length > 0) {
            s.append("%-20s");
        }
        return s.toString();
    }
}
