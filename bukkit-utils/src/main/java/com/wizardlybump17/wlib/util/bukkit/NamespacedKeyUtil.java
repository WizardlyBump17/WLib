package com.wizardlybump17.wlib.util.bukkit;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class NamespacedKeyUtil {

    public static boolean isValidNamespaceChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '.' || c == '_' || c == '-';
    }

    public static boolean isValidKeyChar(char c) {
        return isValidNamespaceChar(c) || c == '/';
    }

    /**
     * <p>
     *     Gets a {@link NamespacedKey} from the given {@link String}.<br>
     * </p>
     * <p>
     *     It returns the {@link NamespacedKey} following these steps:
     *     <ol>
     *         <li>It checks if the string contains a {@code :} character. It it does not, it simply calls the {@link NamespacedKey#fromString(String)} and the {@link #clearKey(String)}.</li>
     *         <li>It checks each character with {@link #isValidKeyChar(char)} and {@link #isValidNamespaceChar(char)}.</li>
     *         <li>Finally, it uses the {@link NamespacedKey#fromString(String)} to return the {@link NamespacedKey}.</li>
     *     </ol>
     * </p>
     * @param string the string to get the {@link NamespacedKey}
     * @return the clean {@link NamespacedKey}
     */
    public static @Nullable NamespacedKey fromString(@NonNull String string) {
        int index = string.indexOf(':');
        if (index == -1)
            return NamespacedKey.fromString(clearKey(string));

        StringBuilder builder = new StringBuilder(string.length());
        boolean namespace = true;
        char[] chars = string.toLowerCase().toCharArray();
        for (char c : chars) {
            if (c == ':' && !namespace)
                return null;

            if (c == ':') {
                namespace = false;
                builder.append(':');
                continue;
            }

            if ((namespace && isValidNamespaceChar(c)) || (!namespace && isValidKeyChar(c)))
                builder.append(c);
        }

        return NamespacedKey.fromString(builder.toString());
    }

    @NonNull
    public static String clearKey(@NonNull String string) {
        char[] chars = string.toLowerCase().toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char c : chars)
            if (isValidKeyChar(c))
                builder.append(c);
        return builder.toString();
    }

    @NonNull
    public static String clearNamespace(@NonNull String string) {
        char[] chars = string.toLowerCase().toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char c : chars)
            if (isValidNamespaceChar(c))
                builder.append(c);
        return builder.toString();
    }
}
