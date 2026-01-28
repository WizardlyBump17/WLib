package com.wizardlybump17.wlib.util.bukkit;

import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.wizardlybump17.wlib.util.StringUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class MiniMessageUtil {

    private static final @NotNull TextComponent NULL = Component.text("null");

    private MiniMessageUtil() {
    }

    public static @NotNull Component getMessage(@NotNull String message, @NotNull Map<String, Object> placeholders) {
        MiniMessage miniMessage = MiniMessage.miniMessage();
        if (placeholders.isEmpty())
            return miniMessage.deserialize(message);

        TagResolver[] resolvers = new TagResolver[placeholders.size()];
        int resolverIndex = 0;
        for (Map.Entry<String, Object> entry : placeholders.entrySet()) {
            String key = StringUtil.pascalToCamel(entry.getKey());
            Object value = entry.getValue();
            resolvers[resolverIndex++] = TagResolver.builder()
                    .tag(
                            key,
                            Tag.inserting(getInsertion(value))
                    )
                    .build();
        }
        return miniMessage.deserialize(message, resolvers);
    }

    public static @NotNull Component getMessage(@NotNull String message, @NotNull String placeholderKey, @NotNull Object placeholderValue, @NotNull String prefix, @NotNull Map<String , ?> additionalPlaceholders) {
        MiniMessage miniMessage = MiniMessage.miniMessage();
        if (additionalPlaceholders.isEmpty())
            return getMessage(message, Map.of(placeholderKey, placeholderValue));

        TagResolver[] resolvers = new TagResolver[additionalPlaceholders.size() + 1];
        int resolverIndex = 0;
        for (Map.Entry<String, ?> entry : additionalPlaceholders.entrySet()) {
            String key = StringUtil.pascalToCamel(entry.getKey());
            Object value = entry.getValue();
            resolvers[resolverIndex++] = TagResolver.builder()
                    .tag(
                            prefix + key,
                            Tag.inserting(getInsertion(value))
                    )
                    .build();
        }
        resolvers[resolverIndex] = TagResolver.builder()
                .tag(
                        prefix + placeholderKey,
                        Tag.inserting(getInsertion(placeholderValue))
                )
                .build();
        return miniMessage.deserialize(message, resolvers);
    }

    private static @NotNull Component getInsertion(@Nullable Object object) {
        if (object == null)
            return NULL;

        return switch (object) {
            case Component component -> component;
            case JsonPrimitive jsonPrimitive -> Component.text(jsonPrimitive.getAsString());
            case JsonNull ignored -> NULL;
            default -> Component.text(String.valueOf(object));
        };
    }

    public static @NotNull Component getMessage(@NotNull String message, @NotNull String placeholderKey, @NotNull Object placeholderValue, @NotNull Map<String , ?> additionalPlaceholders) {
        return getMessage(message, placeholderKey, placeholderValue, "", additionalPlaceholders);
    }

    public static @NotNull Component getMessage(@NotNull String message) {
        return getMessage(message, Map.of());
    }
}
