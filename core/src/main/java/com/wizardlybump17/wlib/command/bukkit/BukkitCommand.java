package com.wizardlybump17.wlib.command.bukkit;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
import org.jetbrains.annotations.NotNull;

public class BukkitCommand extends Command {

    private final @NotNull InternalBukkitCommand internalCommand;

    public BukkitCommand(@NotNull LiteralCommandNode root) {
        super(root);
        internalCommand = new InternalBukkitCommand(this);
    }

    public @NotNull InternalBukkitCommand getInternalCommand() {
        return internalCommand;
    }
}
