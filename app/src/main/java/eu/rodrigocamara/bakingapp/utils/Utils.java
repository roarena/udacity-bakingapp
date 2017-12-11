package eu.rodrigocamara.bakingapp.utils;

/**
 * Created by rodrigo.camara on 11/12/2017.
 */

public class Utils {
    public static String capitalizeString(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
