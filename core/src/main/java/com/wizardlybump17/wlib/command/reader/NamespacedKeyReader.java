package com.wizardlybump17.wlib.command.reader;

import com.wizardlybump17.wlib.command.CommandSender;
import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import lombok.NonNull;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NamespacedKeyReader extends ArgsReader<NamespacedKey> {

    private static final @NotNull List<String> SUGGESTIONS = List.of("key", "namespace:key");

    @Override
    public @NotNull Class<NamespacedKey> getType() {
        return NamespacedKey.class;
    }

    @Override
    public NamespacedKey read(String string) {
        return NamespacedKey.fromString(string.toLowerCase());
    }

    @Override
    public @NonNull List<@NonNull String> autoComplete(@NonNull CommandSender<?> sender, @NonNull String current) {
        return SUGGESTIONS;
    }
}
