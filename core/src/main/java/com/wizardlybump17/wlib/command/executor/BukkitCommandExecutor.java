package com.wizardlybump17.wlib.command.executor;

import com.wizardlybump17.commands.manager.CommandManager;
import com.wizardlybump17.commands.result.ExceptionResult;
import com.wizardlybump17.wlib.command.sender.factory.CommandSenderFactory;
import com.wizardlybump17.wlib.command.sender.factory.CommandSenderFactoryRegistry;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

@Data
public class BukkitCommandExecutor implements CommandExecutor {

    private final @NonNull CommandManager commandManager;

    @SuppressWarnings("unchecked")
    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        CommandSenderFactory<CommandSender, ?> factory = (CommandSenderFactory<CommandSender, ?>) CommandSenderFactoryRegistry.INSTANCE.get(sender.getClass());
        commandManager
                .execute(
                        factory.get(sender),
                        command.getName() + " " + String.join(" ", args)
                )
                .ifPresent(result -> {
                    if (result instanceof ExceptionResult exceptionResult)
                        commandManager.getLogger().log(Level.SEVERE, "Error while executing the command " + result.command(), exceptionResult.exception());
                });
        return false;
    }
}
