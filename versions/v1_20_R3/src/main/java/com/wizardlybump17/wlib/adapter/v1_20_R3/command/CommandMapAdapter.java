package com.wizardlybump17.wlib.adapter.v1_20_R3.command;

import com.wizardlybump17.wlib.util.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CommandMapAdapter extends com.wizardlybump17.wlib.adapter.command.CommandMapAdapter {

    public static final @NotNull CommandMap COMMAND_MAP = ReflectionUtil.getFieldValue(ReflectionUtil.getField("commandMap", Bukkit.getServer().getClass()), Bukkit.getServer());
    public static final @NotNull Map<String, Command> COMMANDS = ReflectionUtil.getFieldValue(ReflectionUtil.getField("knownCommands", SimpleCommandMap.class), COMMAND_MAP);

    @Override
    public @NotNull CommandMap getCommandMap() {
        return COMMAND_MAP;
    }

    @Override
    public void unregisterCommand(@NotNull String command) {
        Command removed = COMMANDS.remove(command);
        if (removed != null)
            removed.unregister(COMMAND_MAP);
    }

    @Override
    public @NotNull Map<String, Command> getCommands() {
        return COMMANDS;
    }
}
