package com.wizardlybump17.wlib.command.sender;

import com.wizardlybump17.wlib.WLib;
import com.wizardlybump17.wlib.util.bukkit.MiniMessageUtil;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class BukkitCommandSender implements CommandSender<org.bukkit.command.CommandSender> {

    private final @NotNull org.bukkit.command.CommandSender handle;

    public BukkitCommandSender(@NotNull org.bukkit.command.CommandSender handle) {
        this.handle = handle;
    }

    @Override
    public org.bukkit.command.CommandSender getHandle() {
        return handle;
    }

    @Override
    public void sendMessage(String message) {
        handle.sendMessage(message);
    }

    @Override
    public void sendMessage(String... message) {
        handle.sendMessage(String.join("\n", message));
    }

    public void sendMiniMessage(@NotNull String message, @NotNull Map<String, Object> placeholders) {
        WLib.getInstance().getAudiences().sender(handle).sendMessage(MiniMessageUtil.getMessage(message, placeholders));
    }

    public void sendMiniMessage(@NotNull String message) {
        sendMiniMessage(message, Map.of());
    }

    @Override
    public String getName() {
        return handle.getName();
    }

    @Override
    public boolean hasPermission(String permission) {
        return handle.hasPermission(permission);
    }

    public @NotNull BlockCommandSender asBlockCommand() {
        return (BlockCommandSender) handle;
    }

    public @NotNull ConsoleCommandSender asConsole() {
        return (ConsoleCommandSender) handle;
    }

    public @NotNull Player asPlayer() {
        return (Player) handle;
    }

    @Override
    public boolean hasId(@NotNull UUID id) {
        return handle instanceof Entity entity && entity.getUniqueId().equals(id);
    }
}
