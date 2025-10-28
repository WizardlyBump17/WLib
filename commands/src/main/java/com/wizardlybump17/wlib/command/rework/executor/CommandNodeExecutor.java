package com.wizardlybump17.wlib.command.rework.executor;

import com.wizardlybump17.wlib.command.rework.context.CommandContext;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import org.jetbrains.annotations.NotNull;

public interface CommandNodeExecutor<T> {

//    @NotNull CommandExecutor<String> TEST_EXECUTOR = context -> {
//        System.out.println(context);
//        return CommandResult.successful("");
//    };

    @NotNull CommandResult<T> execute(@NotNull CommandContext context);
}
