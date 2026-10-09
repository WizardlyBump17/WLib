package com.wizardlybump17.wlib.command.manager.listener;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.manager.CommandManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CommandManagerListener {

    void onRegister(@NotNull String identifier, @NotNull Command command, @Nullable Object holder, @NotNull CommandManager manager);

    void onPreClear(@NotNull CommandManager manager);

    void onPostClear(@NotNull CommandManager manager);

    void onUnregister(@NotNull String identifier, @NotNull Command command, @Nullable Object holder, @NotNull CommandManager manager);
}
