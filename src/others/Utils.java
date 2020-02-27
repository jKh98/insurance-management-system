package others;

public class Utils {

    /**
     * Returns a formatting string based on given table
     *
     * @param length number of table columns
     * @return format string
     */
    public static String getFormatSpacing(int length) {
        StringBuilder s = new StringBuilder("%-20s");
        while (--length > 0) {
            s.append("%-20s");
        }
        return s.toString();
    }
}
