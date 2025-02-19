package com.wizardlybump17.wlib.command.reader;

import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public @NonNull List<@NonNull String> autoComplete(@NonNull CommandSender<?> sender, @NonNull String current) {
        OfflinePlayer[] players = Bukkit.getOfflinePlayers();
        List<String> suggestions = new ArrayList<>(players.length);
        for (OfflinePlayer player : players)
            suggestions.add(player.getName());
        return suggestions;
    }
}
