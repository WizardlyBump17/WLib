package com.wizardlybump17.wlib.command.rework;

import com.wizardlybump17.wlib.command.rework.node.input.LiteralAllowedInput;
import com.wizardlybump17.wlib.command.rework.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import com.wizardlybump17.wlib.command.rework.result.SuccessResult;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import com.wizardlybump17.wlib.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Command {

    private final @NotNull LiteralCommandNode root;

    public Command(@NotNull LiteralCommandNode root) {
        this.root = root;
    }

    public @NotNull LiteralCommandNode getRoot() {
        return root;
    }

    public @NotNull CommandResult execute(@NotNull CommandSender<?> sender, @NotNull String execution) {
        List<String> strings = StringUtil.parseQuotedStrings(execution);
        return SuccessResult.INSTANCE;
    }
}
