package com.wizardlybump17.wlib.command.result.error;

import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;

public record InvalidSenderResult<T>(int lastInputIndex, @NotNull CommandNode<?> lastNode, @NotNull CommandSender<?> sender, @NotNull Class<? extends CommandSender<?>> expectedSender) implements UnsuccessResult<T> {
}
