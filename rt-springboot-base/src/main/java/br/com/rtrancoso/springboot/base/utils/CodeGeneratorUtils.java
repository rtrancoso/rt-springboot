package br.com.rtrancoso.springboot.base.utils;

import java.util.Random;

public class CodeGeneratorUtils {

    private CodeGeneratorUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String generate() {
        return generate(4);
    }

    public static String generate(int size) {
        StringBuilder result = new StringBuilder();
        var characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        var charactersLength = characters.length();
        for (int i = 0; i < size; i++) {
            result.append(characters.charAt(new Random().nextInt(charactersLength)));
        }
        return result.toString();
    }

}
