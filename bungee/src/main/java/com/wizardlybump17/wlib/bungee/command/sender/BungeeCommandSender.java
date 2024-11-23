package com.wizardlybump17.wlib.bungee.command.sender;

import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ConnectedPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@RequiredArgsConstructor
public class BungeeCommandSender implements com.wizardlybump17.wlib.command.sender.CommandSender<net.md_5.bungee.api.CommandSender> {

    private final net.md_5.bungee.api.CommandSender handle;

    @Override
    public net.md_5.bungee.api.CommandSender getHandle() {
        return handle;
    }

    @Override
    public void sendMessage(String message) {
        handle.sendMessage(TextComponent.fromLegacyText(message));
    }

    @Override
    public void sendMessage(String... messages) {
        handle.sendMessage(TextComponent.fromLegacyText(String.join("\n", messages)));
    }

    @Override
    public String getName() {
        return handle.getName();
    }

    @Override
    public boolean hasPermission(String permission) {
        return handle.hasPermission(permission);
    }

    public @NotNull ProxiedPlayer asProxiedPlayer() {
        return (ProxiedPlayer) handle;
    }

    public @NotNull ConnectedPlayer asConnectedPlayer() {
        return (ConnectedPlayer) handle;
    }

    @Override
    public boolean hasId(@NotNull UUID id) {
        return handle instanceof ProxiedPlayer player && player.getUniqueId().equals(id);
    }
}
