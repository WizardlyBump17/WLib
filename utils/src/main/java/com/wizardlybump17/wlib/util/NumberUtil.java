package com.wizardlybump17.wlib.util;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class NumberUtil {

    public static final int MAX_ROMAN_NUMBER = 4999;

    public static @Nullable Number getNumber(@Nullable Object object) {
        return object instanceof Number number ? number : null;
    }
    
    public static @Nullable Byte getByte(@Nullable Object object) {
        return object instanceof Byte b ? b : null;
    }

    public static @Nullable Short getShort(@Nullable Object object) {
        return object instanceof Short s ? s : null;
    }

    public static @Nullable Integer getInteger(@Nullable Object object) {
        return object instanceof Integer i ? i : null;
    }

    public static @Nullable Long getLong(@Nullable Object object) {
        return object instanceof Long l ? l : null;
    }

    public static @Nullable Float getFloat(@Nullable Object object) {
        return object instanceof Float f ? f : null;
    }

    public static @Nullable Double getDouble(@Nullable Object object) {
        return object instanceof Double d ? d : null;
    }

    /**
     * <p>
     *     Converts the given {@code int} to a roman numeral.
     *     The {@code int} value must be between 1 and {@link #MAX_ROMAN_NUMBER}.
     * </p>
     * @param number the number to convert
     * @return the roman numeral representation of the given number
     */
    public static @NonNull String toRoman(int number) {
        if (number < 1 || number > MAX_ROMAN_NUMBER)
            throw new IllegalArgumentException("The number must be between 1 and " + MAX_ROMAN_NUMBER);

        StringBuilder builder = new StringBuilder();
        RomanNumeral[] numerals = RomanNumeral.invertedValues();

        for (RomanNumeral numeral : numerals) {
            while (number >= numeral.getAmount()) {
                builder.append(numeral.getCharacter());
                number -= numeral.getAmount();
            }
        }

        return builder.toString();
    }

    /**
     * <p>
     *     Converts the given roman numeral to an {@code int}.
     *     The roman numeral must be a valid roman numeral.
     *     If the given roman numeral is invalid, an {@link IllegalArgumentException} will be thrown.
     * </p>
     * @param roman the roman numeral to convert
     * @return the {@code int} representation of the given roman numeral
     * @throws IllegalArgumentException if the given roman numeral is invalid
     */
    public static int fromRoman(@NonNull String roman) throws IllegalArgumentException {
        int result = 0;
        for (int i = 0; i < roman.length(); i++) {
            RomanNumeral currentNumeral = RomanNumeral.fromString(String.valueOf(roman.charAt(i)));
            RomanNumeral nextNumeral = i + 1 < roman.length() ? RomanNumeral.fromString(String.valueOf(roman.charAt(i + 1))) : null;

            if (nextNumeral != null && currentNumeral.getAmount() < nextNumeral.getAmount())
                result -= currentNumeral.getAmount();
            else
                result += currentNumeral.getAmount();
        }
        return result;
    }

    @Getter
    public enum RomanNumeral {

        ONE("I", 1),
        FOUR("IV", 4),
        FIVE("V", 5),
        NINE("IX", 9),
        TEN("X", 10),
        FORTY("XL", 40),
        FIFTY("L", 50),
        NINETY("XC", 90),
        HUNDRED("C", 100),
        FOUR_HUNDRED("CD", 400),
        FIVE_HUNDRED("D", 500),
        NINE_HUNDRED("CM", 900),
        THOUSAND("M", 1000);

        private static final @NonNull Map<String, RomanNumeral> BY_CHARACTER = new HashMap<>(values().length);

        static {
            for (RomanNumeral numeral : values())
                BY_CHARACTER.put(numeral.getCharacter(), numeral);
        }

        private final @NonNull String character;
        private final int amount;
        RomanNumeral(@NonNull String character, int amount) {
            this.character = character;
            this.amount = amount;
        }

        public static RomanNumeral[] invertedValues() {
            RomanNumeral[] values = values();
            int length = values.length;
            RomanNumeral[] numerals = new RomanNumeral[length];
            for (int i = 0; i < length; i++)
                numerals[length - i - 1] = values[i];
            return numerals;
        }

        public static @NonNull NumberUtil.RomanNumeral fromString(@NonNull String string) {
            RomanNumeral numeral = BY_CHARACTER.get(string.toUpperCase());
            if (numeral == null)
                throw new IllegalArgumentException("Invalid character: " + string);
            return numeral;
        }
    }
}
