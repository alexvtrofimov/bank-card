package com.example.bankcards.util;

import java.util.Random;

public class CardSerialNumberUtil {
    private final static Random RANDOM = new Random();

    public static String generate(int group, int size) {

        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= (size * group + group); i++) {
            if (i % (size + 1) == 0) {
                builder.append(" ");
            } else {
                builder.append(RANDOM.nextInt(10));
            }
        }
        return builder.toString().trim();
    }

    public static String encodeSerialNumber(String serialNumber) {
        String[] groups = serialNumber.split(" ");
        StringBuilder result = new StringBuilder(groups[groups.length - 1]);
        for (int i = 0; i < groups.length - 1; i++) {
            String stars = String.valueOf('*').repeat(groups[i].length());
            result.append(" " + stars);
        }
        return result.reverse().toString();
    }
}
