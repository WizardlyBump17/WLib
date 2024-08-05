package com.wizardlybump17.wlib.util;

import com.wizardlybump17.wlib.util.exception.PlaceholderException;
import com.wizardlybump17.wlib.util.exception.QuotedStringException;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StringUtil {

    public static final char PLACEHOLDER_BEGIN = '{';
    public static final char PLACEHOLDER_END = '}';
    public static final char ESCAPE = '\\';
    public static final char SPACE = ' ';
    public static final char QUOTE = '"';
    public static final char QUOTE_ESCAPE = '\\';
    public static final char QUOTE_DELIMITER = ' ';

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

    /**
     * <p>
     * Parses the quoted strings from the input {@link String}.
     * </p>
     * <p>
     * This method moves through the {@link String}, {@code char} by {@code char}, until it finds a quote {@code char}.
     * When it finds it, it will start to append the {@code char}s to the quoted {@link StringBuilder} until it finds the quote {@code char} again, then it will add the quoted {@link String} to the {@link List} of strings.
     * <br>
     * While it does not find the quote {@code char}, it will append the {@code char}s to the {@link StringBuilder} of the non-quoted {@link String}, and when it finds a space {@code char}, it will add the non-quoted {@link String} to the {@link List} of strings and reset the {@link StringBuilder}.
     * Any {@code char} after the escape ({@code \}) {@code char} will be appended to the quoted, or non-quoted, {@link StringBuilder} without any processing.
     * </p>
     * <p>
     * Examples:
     *     <table>
     *         <tr>
     *             <th>Input</th>
     *             <th>Output</th>
     *         </tr>
     *         <tr>
     *             <td>{@code Hello World}</td>
     *             <td>{@code [Hello, World]}</td>
     *         </tr>
     *         <tr>
     *             <td>{@code Hello "World"}</td>
     *             <td>{@code [Hello, World]}</td>
     *         </tr>
     *         <tr>
     *             <td>{@code "Hello World"}</td>
     *             <td>{@code [Hello World]}</td>
     *         </tr>
     *         <tr>
     *             <td>{@code Hello "Beautiful" World}</td>
     *             <td>{@code [Hello, Beautiful, World]}</td>
     *         </tr>
     *         <tr>
     *             <td>{@code Hello "Beautiful World"}</td>
     *             <td>{@code [Hello, Beautiful World]}</td>
     *          </tr>
     *     </table>
     * </p>
     *
     * @param input     the input {@link String}
     * @return the {@link List} of {@link String}s
     * @throws QuotedStringException if any of the following situations happen:
     *                               <ul>
     *                                   <li>A quoted string is started right after a normal (or escaped) {@code char};</li>
     *                                   <li>A quoted string is ended right before a normal (or escaped) {@code char};</li>
     *                                   <li>A escape {@code char} is found at the end of the input;</li>
     *                                   <li>A quoted string is not closed.</li>
     *                               </ul>
     * @see #parseQuotedStrings(String, char, char, char)
     * @see #parseQuotedStrings(String, char, char, char, boolean)
     */
    public static @NonNull List<String> parseQuotedStrings(@NonNull String input) {
        return parseQuotedStrings(input, QUOTE, QUOTE_ESCAPE, QUOTE_DELIMITER, false);
    }

    /**
     * <p>
     * Parses the quoted strings from the input {@link String}.
     * </p>
     * <p>
     * This method moves through the {@link String}, {@code char} by {@code char}, until it finds the quote {@code char}.
     * When it finds it, it will start to append the {@code char}s to the quoted {@link StringBuilder} until it finds the quote {@code char} again, then it will add the quoted {@link String} to the {@link List} of strings.
     * <br>
     * While it does not find the quote {@code char}, it will append the {@code char}s to the {@link StringBuilder} of the non-quoted {@link String}, and when it finds the delimiter {@code char}, it will add the non-quoted {@link String} to the {@link List} of strings and reset the {@link StringBuilder}.
     * Any {@code char} after the escape {@code char} will be appended to the quoted, or non-quoted, {@link StringBuilder} without any processing.
     * </p>
     * <p>
     * If the {@code trim} parameter is {@code true}, it will trim the input and the outputs (e.g. remove any extra space).
     * </p>
     * <p>
     * Examples:
     *     <table>
     *         <tr>
     *             <th>Input</th>
     *             <th>Output</th>
     *         </tr>
     *         <tr>
     *             <td>{@code Hello World}</td>
     *             <td>{@code [Hello, World]}</td>
     *         </tr>
     *         <tr>
     *             <td>{@code Hello "World"}</td>
     *             <td>{@code [Hello, World]}</td>
     *         </tr>
     *         <tr>
     *             <td>{@code "Hello World"}</td>
     *             <td>{@code [Hello World]}</td>
     *         </tr>
     *         <tr>
     *             <td>{@code Hello "Beautiful" World}</td>
     *             <td>{@code [Hello, Beautiful, World]}</td>
     *         </tr>
     *         <tr>
     *             <td>{@code Hello "Beautiful World"}</td>
     *             <td>{@code [Hello, Beautiful World]}</td>
     *          </tr>
     *     </table>
     * </p>
     *
     * @param input     the input {@link String}
     * @param quote     the quote {@code char}
     * @param escape    the escape {@code char}
     * @param delimiter the delimiter {@code char}
     * @param trim      if the input and outputs should be trimmed
     * @return the {@link List} of {@link String}s
     * @throws QuotedStringException if any of the following situations happen:
     *                               <ul>
     *                                   <li>A quoted string is started right after a normal (or escaped) {@code char};</li>
     *                                   <li>A quoted string is ended right before a normal (or escaped) {@code char};</li>
     *                                   <li>A escape {@code char} is found at the end of the input;</li>
     *                                   <li>A quoted string is not closed.</li>
     *                               </ul>
     * @see #parseQuotedStrings(String, char, char, char)
     * @see #parseQuotedStrings(String)
     */
    public static @NonNull List<String> parseQuotedStrings(@NonNull String input, char quote, char escape, char delimiter, boolean trim) {
        List<String> strings = parseQuotedStrings(trim ? trim(input) : input, quote, escape, delimiter);
        if (trim)
            strings.replaceAll(StringUtil::trim);
        return strings;
    }

    /**
     * <p>
     * Parses the quoted strings from the input {@link String}.
     * </p>
     * <p>
     * This method moves through the {@link String}, {@code char} by {@code char}, until it finds the quote {@code char}.
     * When it finds it, it will start to append the {@code char}s to the quoted {@link StringBuilder} until it finds the quote {@code char} again, then it will add the quoted {@link String} to the {@link List} of strings.
     * <br>
     * While it does not find the quote {@code char}, it will append the {@code char}s to the {@link StringBuilder} of the non-quoted {@link String}, and when it finds the delimiter {@code char}, it will add the non-quoted {@link String} to the {@link List} of strings and reset the {@link StringBuilder}.
     * Any {@code char} after the escape {@code char} will be appended to the quoted, or non-quoted, {@link StringBuilder} without any processing.
     * </p>
     * <p>
     * Examples:
     *     <table>
     *         <tr>
     *             <th>Input</th>
     *             <th>Output</th>
     *         </tr>
     *         <tr>
     *             <td>{@code Hello World}</td>
     *             <td>{@code [Hello, World]}</td>
     *         </tr>
     *         <tr>
     *             <td>{@code Hello "World"}</td>
     *             <td>{@code [Hello, World]}</td>
     *         </tr>
     *         <tr>
     *             <td>{@code "Hello World"}</td>
     *             <td>{@code [Hello World]}</td>
     *         </tr>
     *         <tr>
     *             <td>{@code Hello "Beautiful" World}</td>
     *             <td>{@code [Hello, Beautiful, World]}</td>
     *         </tr>
     *         <tr>
     *             <td>{@code Hello "Beautiful World"}</td>
     *             <td>{@code [Hello, Beautiful World]}</td>
     *          </tr>
     *     </table>
     * </p>
     *
     * @param input     the input {@link String}
     * @param quote     the quote {@code char}
     * @param escape    the escape {@code char}
     * @param delimiter the delimiter {@code char}
     * @return the {@link List} of {@link String}s
     * @throws QuotedStringException if any of the following situations happen:
     *                               <ul>
     *                                   <li>A quoted string is started right after a normal (or escaped) {@code char};</li>
     *                                   <li>A quoted string is ended right before a normal (or escaped) {@code char};</li>
     *                                   <li>A escape {@code char} is found at the end of the input;</li>
     *                                   <li>A quoted string is not closed.</li>
     *                               </ul>
     * @see #parseQuotedStrings(String, char, char, char, boolean)
     * @see #parseQuotedStrings(String)
     */
    public static @NonNull List<String> parseQuotedStrings(@NonNull String input, char quote, char escape, char delimiter) throws QuotedStringException {
        List<String> strings = new ArrayList<>();

        char[] chars = input.toCharArray();
        StringBuilder builder = new StringBuilder();
        StringBuilder quoted = new StringBuilder();
        boolean escaped = false;
        boolean delimited = true;
        boolean hadQuote = false;

        for (char current : chars) {
            if (current == escape && !escaped) { // start of an escaped char
                escaped = true;
                continue;
            }

            if (escaped) { // end of the escaped char
                (quoted.isEmpty() ? builder : quoted).append(current);
                escaped = false;
                continue;
            }

            if (current == quote) {
                if (!delimited) // the previous char was not the delimiter. Example case: string"quoted"
                    throw new QuotedStringException(QuotedStringException.QUOTED_WITHOUT_DELIMITER);

                if (quoted.isEmpty()) { // begin of quoted string
                    quoted.append(quote);
                    continue;
                }

                // end of quoted string
                strings.add(quoted.substring(1));
                delimited = false;
                hadQuote = true;
                quoted.setLength(0);
                continue;
            }

            if (current == delimiter && quoted.isEmpty()) { // delimiter (space)
                if (!builder.isEmpty()) {
                    strings.add(builder.toString());
                    builder.setLength(0);
                }
                delimited = true;
                hadQuote = false;
                continue;
            }

            if (hadQuote) // the previous char was a quote. Example case: "quoted"string
                throw new QuotedStringException(QuotedStringException.NON_QUOTED_AFTER_QUOTED);

            (quoted.isEmpty() ? builder : quoted).append(current); // any char
            if (quoted.isEmpty())
                delimited = false;
        }

        if (escaped)
            throw new QuotedStringException(QuotedStringException.INVALID_ESCAPE);
        if (!quoted.isEmpty())
            throw new QuotedStringException(QuotedStringException.UNCLOSED_QUOTE);

        if (!builder.isEmpty())
            strings.add(builder.toString());
        return strings;
    }

    /**
     * <p>
     * Removes any extra space from the {@link String}.
     * </p>
     * <p>
     * Examples inputs and outputs (quotes not included):
     * <pre>
     * "        Hello        ":         "Hello"
     * "     Hello    World     ":      "Hello World"
     * "Hello        ":                 "Hello"
     * "        Hello":                 "Hello"
     * </pre>
     * </p>
     *
     * @param string    the {@link String} to trim
     * @return the trimmed {@link String}
     */
    public static @NonNull String trim(@NonNull String string) {
        return trim(string, SPACE);
    }

    /**
     * <p>
     * Removes any extra {@code character} from the {@link String}.
     * </p>
     * <p>
     * Examples inputs and outputs with the space character (quotes not included in the inputs and outputs):
     * <pre>
     * "        Hello        ":         "Hello"
     * "     Hello    World     ":      "Hello World"
     * "Hello        ":                 "Hello"
     * "        Hello":                 "Hello"
     * </pre>
     * </p>
     *
     * @param string    the {@link String} to trim
     * @param character the {@code character} to remove
     * @return the trimmed {@link String}
     */
    public static @NonNull String trim(@NonNull String string, char character) {
        if (string.isEmpty())
            return string;

        StringBuilder builder = new StringBuilder(string.length());
        char[] chars = string.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char current = chars[i];
            if (current != character || (i != 0 && chars[i - 1] != character))
                builder.append(current);
        }

        int length = builder.length();
        if (length > 0 && builder.charAt(length - 1) == character)
            builder.deleteCharAt(length - 1);

        return builder.toString();
    }
}
