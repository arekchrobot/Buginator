package pl.ark.chr.buginator.util;

/**
 * Created by Arek on 2016-10-21.
 */
public class ValidationUtil {

    public static boolean isNull(Object o) {
        return null == o;
    }
    public static boolean isNotNull(Object o) {
        return null != o;
    }

    public static boolean isBlank(String text) {
        int strLength;
        if (null == text || (strLength = text.length()) == 0) {
            return true;
        }
        for (int i = 0; i <strLength; i++) {
            if (Character.isWhitespace(text.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
}
