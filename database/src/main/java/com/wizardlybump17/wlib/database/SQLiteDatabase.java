package com.wizardlybump17.wlib.database;

import com.wizardlybump17.wlib.util.MapUtils;
import lombok.Getter;
import org.sqlite.JDBC;

import java.io.File;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;

@Getter
public class SQLiteDatabase extends Database {

    private static final Map<String, String> COMMAND_REPLACEMENTS = MapUtils.mapOf("AUTO_INCREMENT", "AUTOINCREMENT");

    private final File file;

    protected SQLiteDatabase(Properties properties, DatabaseHolder holder) {
        super(holder, properties);
        if (properties.getOrDefault("use-plugin-folder", "true").toString().equalsIgnoreCase("true"))
            file = new File(holder.getDataFolder(), properties.getProperty("database", "database.db"));
        else
            file = new File(properties.getProperty("database", "database.db"));
    }

    @Override
    public void open(Consumer<Database> callback) {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        new JDBC();
        super.open(callback);
    }

    @Override
    public Database update(String command, Object... replacements) {
        return super.update(replaceCommand(command), replacements);
    }

    @Override
    public PreparedStatement returnUpdate(String command, Object... replacements) {
        return super.returnUpdate(replaceCommand(command), replacements);
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:sqlite:" + file;
    }

    public static String getType() {
        return "sqlite";
    }

    private static String replaceCommand(String command) {
        command = command.toLowerCase();
        for (Map.Entry<String, String> entry : COMMAND_REPLACEMENTS.entrySet())
            command = command.replace(entry.getKey().toLowerCase(), entry.getValue().toLowerCase());
        return command;
    }
}
