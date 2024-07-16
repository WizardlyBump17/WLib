package com.wizardlybump17.wlib.util;

import com.wizardlybump17.wlib.util.exception.PlaceholderException;
import lombok.NonNull;

import java.util.Map;

public class StringUtil {

    public static final char PLACEHOLDER_BEGIN = '{';
    public static final char PLACEHOLDER_END = '}';
    public static final char ESCAPE = '\\';

    private StringUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static <E extends Enum<E>> String getName(Enum<E> e) {
        String name = e.name().replace('_', ' ');
        String[] s = name.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String s1 : s)
            sb.append(s1.substring(0, 1).toUpperCase()).append(s1.substring(1).toLowerCase()).append(" ");
        return sb.toString().trim();
    }

    /**
     * It will convert a string to a camel case string.
     * Example: HELLO_WORLD -> Hello World
     * @param name the string to convert
     * @return the camel case string
     */
    public static String fixName(String name) {
        name = name.replace('_', ' ');
        String[] s = name.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String s1 : s)
            sb.append(s1.substring(0, 1).toUpperCase()).append(s1.substring(1).toLowerCase()).append(" ");
        return sb.toString().trim();
    }

    /**
     * <p>
     * Applies the given placeholders to the given {@link String}.
     * </p>
     * <p>
     * This method moves through the {@link String}, {@code char} by {@code char}, until it finds the begin of a placeholder (the { {@code char}) and the end of a placeholder (the } {@code char}).
     * Then it will get the value of the placeholder from the {@code placeholders} {@link Map} and append it to the {@link StringBuilder}.
     * </p>
     *
     * @param string       the {@link String} to apply the placeholders
     * @param placeholders the {@link Map} with the placeholders
     * @return the {@link String} with the placeholders applied
     * @throws PlaceholderException if any of the following situations happen:
     *                              <ul>
     *                                  <li>A begin {@code char} is found when one was already found and not ended;</li>
     *                                  <li>An end {@code char} is found when there was no begin {@code char};</li>
     *                                  <li>An escape {@code char} is found in the end of the {@link String};</li>
     *                                  <li>A placeholder is not closed.</li>
     *                              </ul>
     */
    public static @NonNull String applyPlaceholders(@NonNull String string, @NonNull Map<String, Object> placeholders) throws PlaceholderException {
        return applyPlaceholders(string, placeholders, PLACEHOLDER_BEGIN, PLACEHOLDER_END, ESCAPE);
    }

    /**
     * <p>
     * Applies the given placeholders to the given {@link String}.
     * </p>
     * <p>
     * This method moves through the {@link String}, {@code char} by {@code char}, until it finds the begin of a placeholder (provided by the {@code begin} parameter) and the end of a placeholder (provided by the {@code end} parameter).
     * Then it will get the value of the placeholder from the {@code placeholders} {@link Map} and append it to the {@link StringBuilder}.
     * </p>
     *
     * @param string       the {@link String} to apply the placeholders
     * @param placeholders the {@link Map} with the placeholders
     * @param begin        the begin {@code char} of a placeholder
     * @param end          the end {@code char} of a placeholder
     * @param escape       the escape {@code char} of a placeholder
     * @return the {@link String} with the placeholders applied
     * @throws PlaceholderException if any of the following situations happen:
     *                              <ul>
     *                                  <li>A begin {@code char} is found when one was already found and not ended;</li>
     *                                  <li>An end {@code char} is found when there was no begin {@code char};</li>
     *                                  <li>An escape {@code char} is found in the end of the {@link String};</li>
     *                                  <li>A placeholder is not closed.</li>
     *                              </ul>
     */
    public static @NonNull String applyPlaceholders(@NonNull String string, @NonNull Map<String, Object> placeholders, char begin, char end, char escape) throws PlaceholderException {
        char[] chars = string.toCharArray();
        StringBuilder builder = new StringBuilder(chars.length);

        StringBuilder placeholder = new StringBuilder();
        boolean escaped = false;
        for (char current : chars) {
            if (current == ESCAPE && !escaped) {
                escaped = true;
                continue;
            }

            if (current == begin && !escaped) {
                if (placeholder.isEmpty()) {
                    placeholder.append(current);
                    continue;
                }

                throw new PlaceholderException("Duplicated placeholder begin");
            }

            if (current == end && !escaped) {
                if (placeholder.isEmpty())
                    throw new PlaceholderException("Unexpected placeholder end");

                Object value = placeholders.get(placeholder.substring(1));
                builder.append(value);
                placeholder.setLength(0);
                continue;
            }

            (placeholder.isEmpty() ? builder : placeholder).append(current);
            escaped = false;
        }

        if (escaped)
            throw new PlaceholderException("Invalid escape sequence");
        if (!placeholder.isEmpty())
            throw new PlaceholderException("Unclosed placeholder");

        return builder.toString();
    }
}
