package com.wizardlybump17.wlib.command.manager.listener;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.manager.CommandManager;
import org.jetbrains.annotations.NotNull;

public interface CommandManagerListener {

    void onRegister(@NotNull String identifier, @NotNull Command command, @NotNull CommandManager manager);

    void onClear(@NotNull CommandManager manager);
}
