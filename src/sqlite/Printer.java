package sqlite;


import java.util.ArrayList;

import static sqlite.Consts.*;

/**
 * Contains methods used to print messages, errors, and query results
 */
class Printer {

    /**
     * Prints the name of the jdbc driver
     *
     * @param driverName jdbc driver
     */
    static void printDriverName(String driverName) {
        System.out.println(MESSAGE_DB_DRIVER + driverName);
    }

    /**
     * Prints jdbc connection message
     *
     * @param url database url
     */
    static void printDBConnection(String url) {
        System.out.println(MESSAGE_OPENED_DB + url);
    }

    /**
     * Prints DB close message
     */
    static void printCloseDBMessage() {
        System.out.println(MESSAGE_CLOSED_DB);
    }

    /**
     * Prints table added message
     *
     * @param tableName
     */
    static void printTableAddedMessage(String tableName) {
        System.out.println(tableName + MESSAGE_TABLE_ADDED);
    }

    /**
     * Prints trigger added to table message
     *
     * @param tableName
     * @param triggerName
     */
    static void printTriggerAddedMessage(String tableName, String triggerName) {
        System.out.println(MESSAGE_TRIGGER + triggerName + MESSAGE_TRIGGER_ADDED + tableName);
    }

    /**
     * Prints query result which consists of a 2D array into list form
     *
     * @param header table header or column names
     * @param result 2D array containing query result
     */
    static void printAsList(String[] header, ArrayList<Object[]> result) {
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
            System.out.println(MESSAGE_NO_RESULTS);
        }
    }

    /**
     * Prints query result which consists of a 2D array into table form
     *
     * @param header table header or column names
     * @param result 2D array containing query result
     */
    static void printAsTable(String[] header, ArrayList<Object[]> result) {
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
            System.out.println(MESSAGE_NO_RESULTS);
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
            System.out.println(TABLE_NAME_POLICY + MESSAGE_SPACE + policyNo + MESSAGE_SUCCESSFULLY_ADDED);
        } else if (result instanceof String) {
            // if result is a string then it is definitely an error message
            System.out.println(result);
        } else {
            System.out.println(MESSAGE_INVALID_POLICY);
        }
    }

    /**
     * Prints message if beneficiary is added and error otherwise
     *
     * @param result result of INSERT query.
     */
    static void printBeneficiaryAddedMessage(Object result) {
        // If result is a number then insert returned an ID and hence was successful
        if (result instanceof Long || result instanceof Integer) {
            System.out.println(TABLE_NAME_BENEFICIARY + MESSAGE_SUCCESSFULLY_ADDED);
        } else if (result instanceof String) {
            // if result is a string then it is definitely an error message
            System.out.println(result);
        } else {
            System.out.println(MESSAGE_INVALID_BENEFICIARY);
        }
    }

    static void printClaimAddedMessage(Object result, String policyNo) {
        // If result is a number then insert returned an ID and hence was successful
        if (result instanceof Long || result instanceof Integer) {
            System.out.println(TABLE_NAME_CLAIM + MESSAGE_SUCCESSFULLY_ADDED);
        } else if (result instanceof String) {
            // if result is a string then it is definitely an error message
            result = insertString((String) result, policyNo, '#');
            System.out.println(result);
        } else {
            System.out.println(MESSAGE_INVALID_CLAIM);
        }
    }

    private static String insertString(String original, String inserted, char after) {
        StringBuilder newString = new StringBuilder();

        for (int i = 0; i < original.length(); i++) {
            newString.append(original.charAt(i));
            if (original.charAt(i) == after) {
                newString.append(inserted);
            }
        }
        // return the modified String
        return newString.toString();
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
