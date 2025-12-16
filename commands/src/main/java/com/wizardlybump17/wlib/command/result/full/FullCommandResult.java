package com.wizardlybump17.wlib.command.result.full;

import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record FullCommandResult(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull CommandNode<?> lastNode, @NotNull CommandResult<?> result) {
}
