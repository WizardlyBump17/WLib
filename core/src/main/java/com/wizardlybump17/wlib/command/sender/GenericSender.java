package com.wizardlybump17.wlib.command.sender;

import com.wizardlybump17.commands.sender.CommandSender;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GenericSender implements CommandSender<org.bukkit.command.CommandSender> {

    @NotNull
    @Override
    public org.bukkit.command.CommandSender getHandle() {
        return null;
    }

    @Override
    public void sendMessage(@Nullable String message) {

    }

    @Override
    public void sendMessage(@Nullable String @NonNull ... messages) {

    }

    @Override
    public boolean hasPermission(@Nullable String permission) {
        return false;
    }
}
