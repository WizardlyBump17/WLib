package com.wizardlybump17.wlib.command.rework;

import com.wizardlybump17.wlib.command.rework.node.input.LiteralAllowedInput;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Command {

    private final @NotNull LiteralAllowedInput root;

    public Command(@NotNull LiteralAllowedInput root) {
        this.root = root;
    }

    public @NotNull LiteralAllowedInput getRoot() {
        return root;
    }

    public void execute(@NotNull CommandSender<?> sender, @NotNull String execution) {
    }
}
