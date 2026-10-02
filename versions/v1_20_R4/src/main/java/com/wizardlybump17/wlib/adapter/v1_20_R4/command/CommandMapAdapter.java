package com.wizardlybump17.wlib.adapter.v1_20_R4.command;

import com.wizardlybump17.wlib.adapter.v1_20_R4.BaseAdapter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

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
}
