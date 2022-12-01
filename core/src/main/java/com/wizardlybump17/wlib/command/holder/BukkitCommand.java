package com.wizardlybump17.wlib.command.holder;

import com.wizardlybump17.wlib.command.BukkitCommandExecutor;
import com.wizardlybump17.wlib.command.CommandManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.PluginCommand;

@RequiredArgsConstructor
public class BukkitCommand implements Command {

    private final PluginCommand command;
    @Getter
    private CommandExecutor executor;

    @Override
    public void setExecutor(CommandExecutor executor) {
        if (executor instanceof org.bukkit.command.CommandExecutor bukkitExecutor)
            command.setExecutor(bukkitExecutor);
        this.executor = executor;
    }

    @Override
    public CommandExecutor getDefaultExecutor(CommandManager manager, String name) {
         return new BukkitCommandExecutor(manager);
    }
}
