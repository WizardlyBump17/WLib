package com.wizardlybump17.wlib.adapter.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class CommandMapAdapter {

    private static CommandMapAdapter instance;

    public abstract @NotNull CommandMap getCommandMap();

    public abstract void unregisterCommand(@NotNull String command);

    public abstract @NotNull Map<String, Command> getCommands();

    public static CommandMapAdapter getInstance() {
        return instance;
    }

    public static void setInstance(@NotNull CommandMapAdapter instance) {
        if (CommandMapAdapter.instance != null)
            throw new IllegalStateException("The CommandAdapter instance is already set.");
        CommandMapAdapter.instance = instance;
    }
}
