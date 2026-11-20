package com.wizardlybump17.wlib.bungee.command.sender;

import com.wizardlybump17.wlib.bungee.util.collector.ComponentCollector;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ConnectedPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
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
    public void sendMessage(@Nullable Object message) {
        switch (message) {
            case null -> handle.sendMessage(new TextComponent("null"));
            case BaseComponent component -> handle.sendMessage(component);
            default -> handle.sendMessage(new TextComponent(String.valueOf(message)));
        }
    }

    @Override
    public void sendMessage(@Nullable Object @Nullable ... messages) {
        if (messages == null) {
            sendMessage((Object) null);
            return;
        }

        List<BaseComponent> components = new ArrayList<>(messages.length);
        for (Object message : messages) {
            switch (message) {
                case null -> components.add(new TextComponent("null"));
                case BaseComponent component -> components.add(component);
                case String string -> components.add(new TextComponent(string));
                default -> components.add(new TextComponent(String.valueOf(message)));
            }
        }

        handle.sendMessage(components.stream().collect(ComponentCollector.NEW_LINE));
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

    @Override
    public @NotNull UUID getId() throws IllegalStateException {
        if (handle instanceof ProxiedPlayer player)
            return player.getUniqueId();
        throw new IllegalStateException(handle + " does not have an ID");
    }
}
