package com.wizardlybump17.wlib.command.executor;

import com.wizardlybump17.wlib.command.context.CommandContext;
import com.wizardlybump17.wlib.command.result.CommandResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CommandNodeExecutor<T> {

//    @NotNull CommandExecutor<String> TEST_EXECUTOR = context -> {
//        System.out.println(context);
//        return CommandResult.successful("");
//    };

    @Nullable CommandResult<T> execute(@NotNull CommandContext context);
}
