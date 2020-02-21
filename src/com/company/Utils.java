package com.company;

public class Utils {

    /**
     * Dynamically builds "values(?, ... )" string
     * needed for an SQL prepared statement based on number of values
     *
     * @param size number of values characters
     * @return "values(?, ... )" string with proper number of values
     */
    public static String valuesDynamicConstructor(int size) {

        // Use string builder for higher performance when appending
        StringBuilder valuesBuilder = new StringBuilder("(");
        for (int i = 0; i < size; i++) {
            valuesBuilder.append("?");

            // Do not add "," after last "?"
            if (i != size - 1) {
                valuesBuilder.append(",");
            }
        }
        String values = valuesBuilder.toString();
        values += ")";
        return values;
    }
}
