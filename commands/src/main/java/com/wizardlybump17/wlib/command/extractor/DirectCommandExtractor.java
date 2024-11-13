package com.wizardlybump17.wlib.command.extractor;

import com.wizardlybump17.wlib.command.CommandManager;
import com.wizardlybump17.wlib.command.holder.CommandHolder;
import com.wizardlybump17.wlib.command.registered.RegisteredCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DirectCommandExtractor implements CommandExtractor {

    @Override
    public @NotNull List<RegisteredCommand> extract(@NotNull CommandManager manager, @NotNull CommandHolder<?> holder, @NotNull Object object) {
        return object instanceof RegisteredCommand command ? List.of(command) : List.of();
    }
}
