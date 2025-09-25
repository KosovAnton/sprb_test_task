package com.spribe.helpers;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.UUID;

public final class StringGeneratorHelper {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";

    private StringGeneratorHelper() {
    }

    public static String generatePlayerLogin() {
        StringBuilder sb = new StringBuilder("player_");

        for (int i = 0; i < 5; i++) {
            char c = LETTERS.charAt(RANDOM.nextInt(LETTERS.length()));
            sb.append(c);
        }

        long number = Math.abs(RANDOM.nextLong()) % 1_000_000_0000L;
        sb.append(String.format("%010d", number));

        return sb.toString().toLowerCase(Locale.ROOT);
    }

    public static String generateScreenName() {
        String uuidPart = UUID.randomUUID().toString().substring(0, 10);
        return "screenName_" + uuidPart;
    }

    public static String generatePassword() {
        return generatePassword(12);
    }

    public static String generatePassword(int length) {
        String chars = LETTERS + LETTERS.toUpperCase(Locale.ROOT) + DIGITS;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
