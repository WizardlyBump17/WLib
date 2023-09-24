package com.wizardlybump17.wlib.command.reader;

import com.wizardlybump17.wlib.command.CommandSender;
import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerReader extends ArgsReader<Player> {

    @Override
    public Class<Player> getType() {
        return Player.class;
    }

    @Override
    public Player read(String string) {
        return Bukkit.getPlayerExact(string);
    }

    @Override
    public @NonNull List<@NonNull String> autoComplete(@NonNull CommandSender<?> sender, @NonNull String current) {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        List<String> suggestions = new ArrayList<>(players.size());
        for (Player player : players)
            suggestions.add(player.getName());
        return suggestions;
    }
}
