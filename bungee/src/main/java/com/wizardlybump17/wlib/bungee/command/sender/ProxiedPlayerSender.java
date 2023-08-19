package com.wizardlybump17.wlib.bungee.command.sender;

import com.wizardlybump17.wlib.command.CommandSender;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@RequiredArgsConstructor
public class ProxiedPlayerSender implements CommandSender<ProxiedPlayer> {

    private final ProxiedPlayer handle;

    @Override
    public ProxiedPlayer getHandle() {
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

    @Override
    public GenericSender toGeneric() {
        return new GenericSender(handle);
    }
}
