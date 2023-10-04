package com.cerbon.better_beacons.util;

import java.util.HashMap;
import java.util.Map;

public class StringIntMapping {
    private static final Map<String, Integer> stringToInt = new HashMap<>();
    private static final Map<Integer, String> intToString = new HashMap<>();
    private static int currentInt = 0;

    public static int addString(String s) {
        if (!stringToInt.containsKey(s)) {
            stringToInt.put(s, currentInt);
            intToString.put(currentInt, s);
            currentInt++;
        }
        return stringToInt.get(s);
    }

    public static String getString(int i) {
        return intToString.get(i);
    }

    public static int getInt(String s){
        return stringToInt.getOrDefault(s, -1);
    }
}
