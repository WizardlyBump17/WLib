package com.wizardlybump17.wlib.bungee.command.sender;

import com.wizardlybump17.wlib.command.CommandSender;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.TextComponent;

@RequiredArgsConstructor
public class GenericSender implements CommandSender<net.md_5.bungee.api.CommandSender> {

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

    @Override
    public GenericSender toGeneric() {
        return this;
    }

    public static boolean isGeneric() {
        return true;
    }
}
