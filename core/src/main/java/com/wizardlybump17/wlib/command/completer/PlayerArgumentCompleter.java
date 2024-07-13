package com.wizardlybump17.wlib.command.completer;

import com.wizardlybump17.wlib.command.CommandSender;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerArgumentCompleter implements ArgumentCompleter {

    @Override
    public @NonNull List<String> complete(@NonNull CommandSender<?> sender, @NonNull String @NonNull [] args) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .toList();
    }
}
