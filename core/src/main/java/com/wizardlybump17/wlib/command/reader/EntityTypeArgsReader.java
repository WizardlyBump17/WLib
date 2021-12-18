package com.wizardlybump17.wlib.command.reader;

import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import org.bukkit.entity.EntityType;

public class EntityTypeArgsReader extends ArgsReader<EntityType> {

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
}
