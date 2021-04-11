package br.com.rtrancoso.springboot.base.utils;

public class StringUtils {

    public final static String NOT_NUMBER_REGEX = "[^0-9]";

    public static String getOnlyNumbers(String text) {
        if (text == null) {
            return null;
        }
        return text.replaceAll(NOT_NUMBER_REGEX, "");
    }
}
