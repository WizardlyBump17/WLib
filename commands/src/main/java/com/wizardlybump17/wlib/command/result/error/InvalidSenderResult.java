package com.wizardlybump17.wlib.command.result.error;

import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;

public record InvalidSenderResult<T>(@NotNull CommandSender<?> sender, @NotNull Class<? extends CommandSender<?>> expectedSender, @NotNull ErrorDetails errorDetails) implements UnsuccessResult<T> {

    public static final @NotNull String ID = "WLib:Unsuccess/InvalidSender";

    @Override
    public @NotNull String id() {
        return ID;
    }
}
