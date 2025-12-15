package com.wizardlybump17.wlib.command.bukkit;

import com.wizardlybump17.wlib.command.WLibCommandExecutor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@ApiStatus.Internal
public class InternalBukkitCommand extends Command {

    private final @NotNull BukkitCommand command;
    private WLibCommandExecutor executor;

    public InternalBukkitCommand(@NotNull BukkitCommand command) {
        super(command.getName());
        this.command = command;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (executor == null)
            return false;

        executor.onCommand(sender, this, commandLabel, args);
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args, @Nullable Location location) throws IllegalArgumentException {
        if (executor == null)
            return List.of();
        return executor.onTabComplete(sender, this, alias, args);
    }

    public @NotNull BukkitCommand getCommand() {
        return command;
    }

    public WLibCommandExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(WLibCommandExecutor executor) {
        this.executor = executor;
    }
}
