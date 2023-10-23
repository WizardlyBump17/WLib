package com.wizardlybump17.wlib.util.bukkit;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class NamespacedKeyUtil {

    /**
     * <p>
     *     Gets a {@link NamespacedKey} from the given {@link String}.<br>
     * </p>
     * <p>
     *     It returns the {@link NamespacedKey} following these steps:
     *     <ol>
     *         <li>It checks if the string contains a {@code :} character. It it does not, it simply calls the {@link NamespacedKey#fromString(String)} and the {@link StringUtil#clearKey(String)}.</li>
     *         <li>It checks each character with {@link StringUtil#isValidKeyChar(char)} and {@link StringUtil#isValidNamespaceChar(char)}.</li>
     *         <li>Finally, it uses the {@link NamespacedKey#fromString(String)} to return the {@link NamespacedKey}.</li>
     *     </ol>
     * </p>
     * @param string the string to get the {@link NamespacedKey}
     * @return the clean {@link NamespacedKey}
     */
    public static @Nullable NamespacedKey fromString(@NonNull String string) {
        if (string.indexOf(':') == -1)
            return NamespacedKey.fromString(StringUtil.clearKey(string));

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

            if ((namespace && StringUtil.isValidNamespaceChar(c)) || (!namespace && StringUtil.isValidKeyChar(c)))
                builder.append(c);
        }

        return NamespacedKey.fromString(builder.toString());
    }
}
