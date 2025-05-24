package com.wizardlybump17.wlib.adapter.v1_18_R1.command;

import com.wizardlybump17.wlib.util.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Map;

public class CommandMapAdapter extends com.wizardlybump17.wlib.adapter.command.CommandMapAdapter {

    public static final @NotNull Field COMMAND_MAP = ReflectionUtil.getField("commandMap", Bukkit.getServer().getClass());
    public static final @NotNull Field COMMANDS = ReflectionUtil.getField("knownCommands", SimpleCommandMap.class);

    @Override
    public @NotNull CommandMap getCommandMap() {
        return ReflectionUtil.getFieldValue(COMMAND_MAP, Bukkit.getServer());
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
        return ReflectionUtil.getFieldValue(COMMANDS, getCommandMap());
    }
}
