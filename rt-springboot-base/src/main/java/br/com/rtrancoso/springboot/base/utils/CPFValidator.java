package br.com.rtrancoso.springboot.base.utils;

import java.util.Arrays;
import java.util.List;

public class CPFValidator {

    public static boolean validate(final String cpf) {
        if (isParameterInvalid(cpf) || isParameterSizeInvalid(cpf) || isAnyDefault(cpf)) {
            return false;
        }
        return validateVerifyingDigit(cpf, calculateVerifyingDigit(cpf));
    }

    private static boolean isParameterInvalid(String cpf) {
        return cpf == null || cpf.isEmpty() || cpf.isBlank();
    }

    private static boolean isParameterSizeInvalid(String cpf) {
        return cpf.length() != 11;
    }

    private static boolean isAnyDefault(final String cpf) {
        List<String> defaults = Arrays.asList("00000000000", "11111111111", "22222222222", "33333333333", "44444444444", "55555555555", "66666666666",
            "77777777777", "88888888888", "9999999999");
        return defaults.contains(cpf);
    }

    private static boolean validateVerifyingDigit(String cpf, String digitCalculated) {
        return cpf.substring(cpf.length() - 2).equals(digitCalculated);
    }

    private static String calculateVerifyingDigit(String cpf) {
        int accumulator1 = calculateAccumulator(cpf, 0);
        int dv1 = getVerifyingDigit(accumulator1);

        int accumulator2 = calculateAccumulator(cpf, accumulator1 + (2 * dv1));
        int dv2 = getVerifyingDigit(accumulator2);

        return String.format("%d%d", dv1, dv2);
    }


    private static int calculateAccumulator(String cpf, int starterOf) {
        int accumulator = starterOf;
        for (int i = 1; i < cpf.length() - 1; i++) {
            accumulator += calculateAccumulatorValue(cpf.substring(i - 1, i), i);
        }
        return accumulator;
    }

    private static int calculateAccumulatorValue(String digit, int position) {
        return (11 - position) * Integer.parseInt(digit);
    }

    private static int getVerifyingDigit(int accumulator) {
        int rest = accumulator % 11;
        if (rest >= 2) {
            return 11 - rest;
        }
        return 0;
    }
}
