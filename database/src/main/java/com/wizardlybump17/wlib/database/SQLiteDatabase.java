package com.wizardlybump17.wlib.database;

import com.wizardlybump17.wlib.database.model.SQLiteDatabaseModel;
import com.wizardlybump17.wlib.util.MapUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.File;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode(callSuper = true)
public class SQLiteDatabase extends Database<SQLiteDatabaseModel> {

    public static final Pattern AUTOINCREMENT_PATTERN = Pattern.compile("AUTO_INCREMENT", Pattern.CASE_INSENSITIVE);

    private static final Map<String, String> COMMAND_REPLACEMENTS = MapUtils.mapOf("AUTO_INCREMENT", "AUTOINCREMENT");

    private final File file;

    public SQLiteDatabase(SQLiteDatabaseModel model, Properties properties, DatabaseHolder<?> holder) {
        super(model, holder, properties);
        file = new File(holder.getDataFolder(), properties.getProperty("database", "database.db"));
    }

    @Override
    public void open(Consumer<Database<SQLiteDatabaseModel>> callback) {
        try {
            if (!file.exists()) {
                if (!file.getParentFile().mkdirs())
                    throw new IllegalStateException("cannot create parent directories");
                if (!file.createNewFile())
                    throw new IllegalStateException("cannot create database file");
            }

            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            getHolder().getLogger().severe("SQLite driver not found");
            e.printStackTrace();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        super.open(callback);
    }

    @Override
    public Database<SQLiteDatabaseModel> update(String command, Object... replacements) {
        return super.update(replaceCommand(command), replacements);
    }

    @Override
    public PreparedStatement returnUpdate(String command, Object... replacements) {
        return super.returnUpdate(replaceCommand(command), replacements);
    }

    @Override
    public String getJdbcUrl() {
        return getModel().getBaseUrl()
                .replace("{database}", file.getAbsolutePath());
    }

    private static String replaceCommand(String command) {
        return AUTOINCREMENT_PATTERN.matcher(command).replaceAll("AUTOINCREMENT");
    }
}
