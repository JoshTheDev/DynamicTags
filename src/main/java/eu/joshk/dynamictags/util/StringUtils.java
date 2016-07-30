package eu.joshk.dynamictags.util;

/**
 * Created by Josh on 30/07/2016.
 */
public class StringUtils {

    /**
     * Create a compounded string from an argument array.
     *
     * @param args argument array.
     * @param start index to start.
     * @param end index to stop.
     * @return compounded string.
     */
    public static String fromArgsArray(String[] args, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i <= end; i++) {
            sb.append(args[i]);
            sb.append(" ");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }

    /**
     * Create a compounded string from an argument array.
     *
     * @param args argument array.
     * @param start index to start.
     * @return compounded string.
     */
    public static String fromArgsArray(String[] args, int start) {
        return fromArgsArray(args, start, args.length - 1);
    }

    /**
     * Create a compounded string from an argument array.
     *
     * @param args argument array.
     * @return compounded string.
     */
    public static String fromArgsArray(String[] args) {
        return fromArgsArray(args, 0);
    }

}
