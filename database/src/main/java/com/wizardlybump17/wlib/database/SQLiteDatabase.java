package com.wizardlybump17.wlib.database;

import com.wizardlybump17.wlib.database.model.SQLiteDatabaseModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.io.File;
import java.sql.PreparedStatement;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode(callSuper = true)
public class SQLiteDatabase extends Database<SQLiteDatabaseModel> {

    public static final Pattern AUTOINCREMENT_PATTERN = Pattern.compile("AUTO_INCREMENT", Pattern.CASE_INSENSITIVE);

    private final File file;

    public SQLiteDatabase(SQLiteDatabaseModel model, Properties properties, DatabaseHolder<?> holder) {
        super(model, holder, properties);
        file = new File(holder.getDataFolder(), properties.getProperty("database", "database.db"));
    }

    public SQLiteDatabase(@NonNull SQLiteDatabaseModel model, @NonNull Properties properties, @NonNull DatabaseHolder<?> holder, @NonNull Logger logger) {
        super(model, holder, properties, logger);
        file = new File(holder.getDataFolder(), properties.getProperty("database", "database.db"));
    }

    @Override
    public void open(Consumer<Database<SQLiteDatabaseModel>> callback) {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            getLogger().log(Level.SEVERE, "The SQLite driver was not found", e);
            return;
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Error while connecting to the SQLite database", e);
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
