package com.wizardlybump17.wlib.command.completer;

import com.wizardlybump17.wlib.command.CommandSender;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class EntityTypeArgumentCompleter implements ArgumentCompleter {

    @Override
    public List<String> complete(CommandSender<?> sender, String[] args) {
        EntityType[] values = EntityType.values();
        List<String> entities = new ArrayList<>(values.length);
        for (EntityType type : values)
            entities.add(type.name());
        return entities;
    }
}
