package com.wizardlybump17.wlib.command.completer;

import com.wizardlybump17.wlib.command.CommandSender;
import lombok.NonNull;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.List;

public class EntityTypeArgumentCompleter implements ArgumentCompleter {

    public static final @NonNull List<String> ENTITIES = Arrays.stream(EntityType.values())
            .map(Enum::name)
            .toList();

    @Override
    public @NonNull List<String> complete(CommandSender<?> sender, String[] args) {
        return ENTITIES;
    }
}
