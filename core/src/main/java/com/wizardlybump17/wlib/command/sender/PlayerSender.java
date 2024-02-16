package com.wizardlybump17.wlib.command.sender;

import com.wizardlybump17.commands.sender.CommandSender;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@Data
public class PlayerSender implements CommandSender<Player> {

    private final @NonNull Player handle;

    @Override
    public void sendMessage(@Nullable String message) {
        if (message != null)
            handle.sendMessage(message);
    }

    @Override
    public void sendMessage(@Nullable String @NonNull ... messages) {
        sendMessage(String.join("\n", messages));
    }

    @Override
    public boolean hasPermission(@Nullable String permission) {
        return permission == null || handle.hasPermission(permission);
    }
}
