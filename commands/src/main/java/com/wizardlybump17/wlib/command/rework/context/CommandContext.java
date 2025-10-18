package com.wizardlybump17.wlib.command.rework.context;

import com.wizardlybump17.wlib.command.rework.Command;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;

public record CommandContext(@NotNull Command command, @NotNull CommandSender<?> sender) {
}
