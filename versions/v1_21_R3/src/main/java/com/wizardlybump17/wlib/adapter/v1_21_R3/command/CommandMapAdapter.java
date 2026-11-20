package com.wizardlybump17.wlib.adapter.v1_21_R3.command;

import com.wizardlybump17.wlib.adapter.v1_21_R3.BaseAdapter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class CommandMapAdapter extends com.wizardlybump17.wlib.adapter.command.CommandMapAdapter implements BaseAdapter {

    @Override
    public @NotNull CommandMap getCommandMap() {
        return Bukkit.getCommandMap();
    }

    @Override
    public void unregisterCommand(@NotNull String command) {
        Map<String, Command> commands = getCommands();
        CommandMap commandMap = getCommandMap();

        Command removed = commands.remove(command);
        if (removed != null)
            removed.unregister(commandMap);
    }

    @Override
    public @NotNull Map<String, Command> getCommands() {
        return getCommandMap().getKnownCommands();
    }

    @Override
    public @NotNull Set<String> getMinecraftVersions() {
        return Set.of("1.20.6");
    }
}
