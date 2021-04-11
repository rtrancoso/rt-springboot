package br.com.rtrancoso.springboot.base.utils;

public class CodeGeneratorUtils {

    public static String generate() {
        return generate(4);
    }

    public static String generate(int size) {
        var result = "";
        var characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        var charactersLength = characters.length();
        for (int i = 0; i < size; i++) {
            result += characters.charAt((int) Math.floor(Math.random() * charactersLength));
        }
        return result;
    }

}
