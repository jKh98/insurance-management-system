package others;

public class Utils {

    /**
     * Dynamically builds "values(?, ... )" string of placeholders
     * needed for an SQL prepared statement based on number of values
     *
     * @param size number of values characters
     * @return "values(?, ... )" string with proper number of values
     */
    public static String valuesPlaceholderDynamicConstructor(int size) {

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

    /**
     * Dynamically builds (args1,arg2, ...) string of arguments
     * Needed for SQL table columns for new tables
     *
     * @param args list of string arguments
     * @return (arg1, arg2, ...) string with all arguments
     */
    public static String argumentsDynamicConstructor(String[] args, boolean addParenthesis) {

        // Use string builder for higher performance when appending
        StringBuilder arguments = new StringBuilder();
        if (addParenthesis) arguments.append("(");
        // Append values
        for (String arg : args) {
            arguments.append(arg);
            arguments.append(",");
        }
        // Remove last comma
        arguments.delete(arguments.length() - 1, arguments.length());
        if (addParenthesis) arguments.append(")");
        return arguments.toString();
    }
}
