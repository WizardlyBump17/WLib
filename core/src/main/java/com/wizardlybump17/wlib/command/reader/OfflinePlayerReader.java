package com.wizardlybump17.wlib.command.reader;

import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class OfflinePlayerReader extends ArgsReader<OfflinePlayer> {

    @Override
    public Class<OfflinePlayer> getType() {
        return OfflinePlayer.class;
    }

    @Override
    public OfflinePlayer read(String string) {
        Player player = Bukkit.getPlayerExact(string);
        if (player != null)
            return player;

        try {
            UUID uuid = UUID.fromString(string);
            return Bukkit.getOfflinePlayer(uuid);
        } catch (IllegalArgumentException ignored) {
            for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers())
                if (string.equalsIgnoreCase(offlinePlayer.getName()))
                    return offlinePlayer;
        }

        return null;
    }
}
