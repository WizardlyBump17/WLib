package com.wizardlybump17.wlib.database;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class MySQLDatabase<K extends JavaPlugin> extends Database<K> {

    private final String host, database;
    private final int port;

    public MySQLDatabase(K plugin, String host, int port, String database) {
        super(plugin);
        this.host = host;
        this.port = port;
        this.database = database;
    }

    @Override
    public String getUrl() {
        return "jdbc:mysql://" + host + ":" + port + "/" + database;
    }
}
