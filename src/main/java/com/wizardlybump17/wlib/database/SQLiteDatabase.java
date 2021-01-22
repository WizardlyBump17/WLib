package com.wizardlybump17.wlib.database;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

@Getter
public class SQLiteDatabase<K extends JavaPlugin> extends Database<K> {

    private final File file;

    public SQLiteDatabase(K plugin, File file) {
        super(plugin);
        this.file = file;
        try {
            setupFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupFile() throws IOException {
        file.mkdirs();
        if (!file.exists()) file.createNewFile();
    }

    @Override
    public String getUrl() {
        return "jdbc:sqlite:" + file;
    }
}
