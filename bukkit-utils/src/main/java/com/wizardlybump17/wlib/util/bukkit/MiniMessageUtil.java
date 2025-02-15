package com.wizardlybump17.wlib.util.bukkit;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class MiniMessageUtil {

    private MiniMessageUtil() {
    }

    public static @NotNull Component getMessage(@NotNull String message, @NotNull Map<String, Object> placeholders) {
        MiniMessage miniMessage = MiniMessage.miniMessage();
        if (placeholders.isEmpty())
            return miniMessage.deserialize(message);

        TagResolver[] resolvers = new TagResolver[placeholders.size()];
        int resolverIndex = 0;
        for (Map.Entry<String, Object> entry : placeholders.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            resolvers[resolverIndex++] = TagResolver.builder()
                    .tag(
                            key,
                            Tag.inserting(value instanceof Component component ? component : Component.text(String.valueOf(value)))
                    )
                    .build();
        }
        return miniMessage.deserialize(message, resolvers);
    }

    public static @NotNull Component getMessage(@NotNull String message) {
        return getMessage(message, Map.of());
    }
}
