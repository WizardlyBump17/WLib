package com.wizardlybump17.wlib.database;

import com.wizardlybump17.wlib.database.model.MySQLDatabaseModel;
import lombok.NonNull;

import java.sql.PreparedStatement;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class MySQLDatabase extends Database<MySQLDatabaseModel> {

    public static final Pattern AUTOINCREMENT_PATTERN = Pattern.compile("AUTOINCREMENT", Pattern.CASE_INSENSITIVE);

    public MySQLDatabase(MySQLDatabaseModel model, Properties properties, DatabaseHolder<?> holder) {
        super(model, holder, properties);
    }

    public MySQLDatabase(@NonNull MySQLDatabaseModel model, @NonNull Properties properties, @NonNull DatabaseHolder<?> holder, @NonNull Logger logger) {
        super(model, holder, properties, logger);
    }

    @Override
    public final String getJdbcUrl() {
        return getModel().getBaseUrl()
                .replace("{host}", getProperties().getProperty("host", "localhost"))
                .replace("{port}", getProperties().getProperty("port", "3306"))
                .replace("{database}", getProperties().getProperty("database", getHolder().getName()));
    }

    @Override
    public Database<MySQLDatabaseModel> update(String command, Object... replacements) {
        return super.update(replaceCommand(command), replacements);
    }

    @Override
    public PreparedStatement returnUpdate(String command, Object... replacements) {
        return super.returnUpdate(replaceCommand(command), replacements);
    }

    private static String replaceCommand(String command) {
        return AUTOINCREMENT_PATTERN.matcher(command).replaceAll("AUTO_INCREMENT");
    }
}
