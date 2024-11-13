package com.wizardlybump17.wlib.command.executor;

import com.wizardlybump17.wlib.command.CommandSender;
import com.wizardlybump17.wlib.command.exception.CommandException;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface CommandExecutor {

    void execute(@NotNull CommandSender<?> sender, @NotNull Map<String, Object> args) throws CommandException;
}
