package com.wizardlybump17.wlib.command.reader;

import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import lombok.NonNull;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.List;

public class EntityTypeArgsReader extends ArgsReader<EntityType> {

    public static final List<String> SUGGESTIONS = Arrays.stream(EntityType.values())
            .filter(EntityType::isAlive)
            .map(EntityType::name)
            .toList();

    @Override
    public Class<EntityType> getType() {
        return EntityType.class;
    }

    @Override
    public EntityType read(String string) {
        try {
            return EntityType.valueOf(string.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public @NonNull List<@NonNull String> autoComplete(@NonNull CommandSender<?> sender, @NonNull String current) {
        return SUGGESTIONS;
    }
}
