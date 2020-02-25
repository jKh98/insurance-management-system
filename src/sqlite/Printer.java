package sqlite;


import others.Constants;

import java.util.ArrayList;

/**
 * Contains methods used to print messages, errors, and query results
 */
class Printer {
    /**
     * Prints message if policy is added and error otherwise
     *
     * @param result   result of INSERT query. Value is 0L if insert was not successful
     * @param policyNo string value of PolicyNo
     */
    static void showPolicyAddedMessage(Long result, String policyNo) {
        // If result is inserted notify with a message
        if (result != 0L) {
            System.out.println(Constants.TABLE_NAME_POLICY + Constants.MESSAGE_SPACE + policyNo + Constants.MESSAGE_SUCCESSFULLY_ADDED);
        } else {
            System.out.println(Constants.MESSAGE_INVALID_POLICY);
        }
    }

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
            String spacing = getFormatSpacing(result.get(0).length);
            // Print header
            System.out.format(spacing + "\n",header);
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

    /**
     * Prints message if beneficiary is added and error otherwise
     *
     * @param result result of INSERT query. Value is 0L if insert was not successful
     */
    public static void showBeneficiaryAddedMessage(Long result) {
        // If result is inserted notify with a message
        if (result != 0L) {
            System.out.println(Constants.TABLE_NAME_BENEFICIARY + Constants.MESSAGE_SUCCESSFULLY_ADDED);
        } else {
            System.out.println(Constants.MESSAGE_INVALID_BENEFICIARY);
        }
    }
}
