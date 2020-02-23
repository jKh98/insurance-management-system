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

}
