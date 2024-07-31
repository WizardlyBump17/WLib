package com.wizardlybump17.wlib.bungee.command;

import com.wizardlybump17.wlib.command.holder.Command;
import com.wizardlybump17.wlib.command.holder.CommandHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class BungeeCommandHolder implements CommandHolder<Plugin> {

    private final Plugin plugin;
    private final Map<String, Command> commands = new HashMap<>();

    @Override
    public Command getCommand(String name) {
        if (commands.containsKey(name))
            return commands.get(name);
        BungeeCommand command = new BungeeCommand();
        commands.put(name, command);
        return command;
    }

    @Override
    public Plugin getHandle() {
        return plugin;
    }

    @Override
    public @NonNull Logger getLogger() {
        return plugin.getLogger();
    }
}
