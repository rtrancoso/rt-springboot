package br.com.rtrancoso.springboot.base.utils;

public class EmailValidator {

    public static boolean validate(String email) {
        if (isParameterInvalid(email)) {
            return false;
        }
        return email.matches(".+@.+\\.[a-z]+");
    }

    private static boolean isParameterInvalid(String email) {
        return email == null || email.isEmpty() || email.isBlank();
    }

}
