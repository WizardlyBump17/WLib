package com.wizardlybump17.wlib.database;

import com.wizardlybump17.wlib.database.annotation.Modifier;
import lombok.Data;

import java.util.Properties;

@Data
public abstract class DatabaseModel<D extends Database<?>> {

    private final String type;
    private final String baseUrl;

    public abstract D createDatabase(DatabaseHolder holder, Properties properties);

    public abstract String getModifierCommand(Modifier modifier);
}
