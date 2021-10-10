package com.wizardlybump17.wlib.database;

import com.wizardlybump17.wlib.util.MapUtils;

import java.sql.PreparedStatement;
import java.util.Map;
import java.util.Properties;

public class MySQLDatabase extends Database {

    private static final Map<String, String> COMMAND_REPLACEMENTS = MapUtils.mapOf("AUTOINCREMENT", "AUTO_INCREMENT");

    protected MySQLDatabase(Properties properties, DatabaseHolder holder) {
        super(holder, properties);
    }

    @Override
    public final String getJdbcUrl() {
        return "jdbc:mysql://" + properties.getProperty("host", "localhost") + ':' +
                properties.getProperty("port", "3306") + '/' +
                properties.getProperty("database", getHolder().getName().toLowerCase());
    }

    @Override
    public Database update(String command, Object... replacements) {
        return super.update(replaceCommand(command), replacements);
    }

    @Override
    public PreparedStatement returnUpdate(String command, Object... replacements) {
        return super.returnUpdate(replaceCommand(command), replacements);
    }

    private static String replaceCommand(String command) {
        command = command.toLowerCase();
        for (Map.Entry<String, String> entry : COMMAND_REPLACEMENTS.entrySet())
            command = command.replace(entry.getKey().toLowerCase(), entry.getValue().toLowerCase());
        return command;
    }

    public static String getType() {
        return "mysql";
    }
}
