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
import java.util.logging.Logger;

public class RegisteredBukkitCommand extends RegisteredCommand {

    private final @NotNull String fallback;

    public RegisteredBukkitCommand(@NotNull CommandData command, @NotNull List<ArgsNode> nodes, @NotNull CommandExecutor executor, @NotNull String fallback) {
        super(command, nodes, executor);
        this.fallback = fallback;
    }

    public @NotNull String getFallback() {
        return fallback;
    }

    @Override
    public void onRegister(@NotNull CommandManager manager) {
        CommandMap commandMap = CommandMapAdapter.getInstance().getCommandMap();
        String name = getCommand().getName();
        Logger logger = manager.getHolder().getLogger();

        commandMap.register(name, fallback, new Command(name) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
                try {
                    manager.execute(new com.wizardlybump17.wlib.command.sender.CommandSender(sender), name + " " + String.join(" ", args));
                } catch (CommandException e) {
                    logger.log(Level.SEVERE, "Error while executing a command.", e);
                }
                return false;
            }
        });
    }

    @Override
    public void onUnregister(@NotNull CommandManager manager) {
        CommandMapAdapter adapter = CommandMapAdapter.getInstance();

        String name = getCommand().getName();
        adapter.unregisterCommand(name);
        adapter.unregisterCommand(fallback + ":" + name);
    }
}
