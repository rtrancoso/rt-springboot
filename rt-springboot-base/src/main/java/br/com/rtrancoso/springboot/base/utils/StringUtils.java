package br.com.rtrancoso.springboot.base.utils;

public class StringUtils {

    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final String NOT_NUMBER_REGEX = "[^0-9]";

    public static String getOnlyNumbers(String text) {
        if (text == null) {
            return null;
        }
        return text.replaceAll(NOT_NUMBER_REGEX, "");
    }
}
