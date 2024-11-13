package com.wizardlybump17.wlib.command;

import com.wizardlybump17.wlib.command.exception.CommandException;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;

@RequiredArgsConstructor
public class BukkitCommandExecutor implements TabExecutor, com.wizardlybump17.wlib.command.holder.CommandExecutor {

    private final CommandManager manager;

    @Override
    public boolean onCommand(@NotNull org.bukkit.command.CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        try {
            execute(new com.wizardlybump17.wlib.command.sender.CommandSender(sender), command.getName(), args);
        } catch (CommandException e) {
            manager.getHolder().getLogger().log(Level.SEVERE, "Error while executing a command", e);
        }
        return false;
    }

    @Override
    public void execute(com.wizardlybump17.wlib.command.CommandSender<?> sender, String commandName, String[] args) throws CommandException {
        String commandExecution = commandName + " " + String.join(" ", args);
        manager.execute(sender, commandExecution);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull org.bukkit.command.CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
