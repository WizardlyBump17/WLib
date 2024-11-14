package com.wizardlybump17.wlib.command.registered;

import com.wizardlybump17.wlib.adapter.command.CommandMapAdapter;
import com.wizardlybump17.wlib.command.CommandManager;
import com.wizardlybump17.wlib.command.args.ArgsNode;
import com.wizardlybump17.wlib.command.data.CommandData;
import com.wizardlybump17.wlib.command.exception.CommandException;
import com.wizardlybump17.wlib.command.executor.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Level;

public class RegisteredPluginCommand extends RegisteredCommand {

    private final @NotNull String fallback;

    public RegisteredPluginCommand(@NotNull CommandData command, @NotNull List<ArgsNode> nodes, @NotNull CommandExecutor executor, @NotNull String fallback) {
        super(command, nodes, executor);
        this.fallback = fallback;
    }

    public @NotNull String getFallback() {
        return fallback;
    }

    @Override
    public void onRegister(@NotNull CommandManager manager) {
        CommandMap commandMap = CommandMapAdapter.getInstance().getCommandMap();
        commandMap.register(getCommand().getName(), fallback, new Command(getCommand().getName()) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
                try {
                    RegisteredPluginCommand.this.execute(new com.wizardlybump17.wlib.command.sender.CommandSender(sender), String.join(" ", args));
                } catch (CommandException e) {
                    manager.getHolder().getLogger().log(Level.SEVERE, "Error while executing a command.", e);
                }
                return false;
            }
        });
    }

    @Override
    public void onUnregister(@NotNull CommandManager manager) {
        CommandMapAdapter.getInstance().unregisterCommand(fallback + ":" + getCommand().getName());
    }
}
