package com.wizardlybump17.wlib.command.completer;

import com.wizardlybump17.wlib.command.CommandSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerArgumentCompleter implements ArgumentCompleter {

    @Override
    public List<String> complete(CommandSender<?> sender, String[] args) {
        List<String> players = new ArrayList<>(Bukkit.getOnlinePlayers().size());
        for (Player player : Bukkit.getOnlinePlayers())
            players.add(player.getName());
        return players;
    }
}
