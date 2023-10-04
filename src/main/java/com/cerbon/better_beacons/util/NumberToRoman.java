package com.cerbon.better_beacons.util;

public class NumberToRoman {
    private static final int[] VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    private static final String[] SYMBOLS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    public static String convert(int number) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < VALUES.length && number > 0; i++) {
            while (VALUES[i] <= number) {
                number -= VALUES[i];
                result.append(SYMBOLS[i]);
            }
        }
        return result.toString();
    }
}
