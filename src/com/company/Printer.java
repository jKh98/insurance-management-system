package com.company;

class Printer {
    static void showPolicyAddedMessage(Long result, String policyNo) {
        // If result is inserted notify with a message
        if (result != 0L) {
            System.out.println(Constants.TABLE_NAME_POLICY + Constants.MESSAGE_SPACE + policyNo + Constants.MESSAGE_SUCCESSFULLY_ADDED);
        } else {
            System.out.println(Constants.MESSAGE_INVALID_POLICY);
        }
    }

    public static void printDriverName(String driverName) {
        System.out.println(Constants.MESSAGE_DB_DRIVER + driverName);
    }

    public static void printDBConnection(String url) {
        System.out.println(Constants.MESSAGE_OPENED_DB + url);
    }

    public static void printCloseDBMessage() {
        System.out.println(Constants.MESSAGE_CLOSED_DB);
    }

    public static void printTableAddedMessage(String tableName) {
        System.out.println(tableName + Constants.MESSAGE_TABLE_ADDED);
    }

    public static void printTriggerAddedMessage(String tableName, String triggerName) {
        System.out.println(Constants.MESSAGE_TRIGGER + triggerName + Constants.MESSAGE_TRIGGER_ADDED + tableName);
    }
}
