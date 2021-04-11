package br.com.rtrancoso.springboot.base.utils;

public class CNPJValidator {

    public static boolean validate(final String cnpj) {
        if (isParameterInvalid(cnpj) || isParameterSizeInvalid(cnpj)) {
            return false;
        }
        return validateVerifyingDigit(cnpj, calculateVerifyingDigit(cnpj));
    }

    private static boolean isParameterInvalid(String cnpj) {
        return cnpj == null || cnpj.isEmpty() || cnpj.isBlank();
    }

    private static boolean isParameterSizeInvalid(String cnpj) {
        return cnpj.length() != 14;
    }

    private static boolean validateVerifyingDigit(String cnpj, String digitCalculated) {
        return cnpj.substring(cnpj.length() - 2).equals(digitCalculated);
    }

    private static String calculateVerifyingDigit(String cnpj) {
        int accumulator1 = calculateAccumulator1(cnpj);
        int dv1 = getVerifyingDigit(accumulator1);

        int accumulator2 = calculateAccumulator2(cnpj);
        int dv2 = getVerifyingDigit(accumulator2);

        return String.format("%d%d", dv1, dv2);
    }

    private static int calculateAccumulator1(String cnpj) {
        int accumulator = 0;
        char[] cnpjCharArray = cnpj.toCharArray();
        for (int i = 0; i < 4; i++) {
            if (cnpjCharArray[i] - 48 >= 0 && cnpjCharArray[i] - 48 <= 9) {
                accumulator += (cnpjCharArray[i] - 48) * (6 - (i + 1));
            }
        }
        for (int i = 0; i < 8; i++) {
            if (((cnpjCharArray[i + 4] - 48) >= 0) && ((cnpjCharArray[i + 4] - 48) <= 9)) {
                accumulator += (cnpjCharArray[i + 4] - 48) * (10 - (i + 1));
            }
        }
        return accumulator;
    }

    private static int calculateAccumulator2(String cnpj) {
        int accumulator = 0;
        char[] cnpjCharArray = cnpj.toCharArray();
        for (int i = 0; i < 5; i++) {
            if (cnpjCharArray[i] - 48 >= 0 && cnpjCharArray[i] - 48 <= 9) {
                accumulator += (cnpjCharArray[i] - 48) * (7 - (i + 1));
            }
        }
        for (int i = 0; i < 8; i++) {
            if (cnpjCharArray[i + 5] - 48 >= 0 && cnpjCharArray[i + 5] - 48 <= 9) {
                accumulator += (cnpjCharArray[i + 5] - 48) * (10 - (i + 1));
            }
        }
        return accumulator;
    }

    private static int getVerifyingDigit(int accumulator) {
        int rest = 11 - (accumulator % 11);
        if (rest == 10 || rest == 11) {
            return 0;
        }
        return rest;
    }

}
