package com.wizardlybump17.wlib.command.sender;

import com.wizardlybump17.wlib.util.bukkit.collector.ComponentCollector;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BukkitCommandSender implements CommandSender<org.bukkit.command.CommandSender> {

    public static final @NotNull BukkitCommandSender CONSOLE = new BukkitCommandSender(Bukkit.getConsoleSender());
    private static final @NotNull Map<UUID, BukkitCommandSender> SENDERS_BY_ID = new ConcurrentHashMap<>();

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

    @Override
    public void sendMessage(@Nullable Object message) {
        switch (message) {
            case null -> handle.sendMessage("null");
            case ComponentLike component -> handle.sendMessage(component);
            case String string -> handle.sendMessage(string);
            default -> handle.sendMessage(String.valueOf(message));
        }
    }

    @Override
    public void sendMessage(@Nullable Object @Nullable ... messages) {
        if (messages == null) {
            sendMessage((Object) null);
            return;
        }

        List<Component> components = new ArrayList<>(messages.length);
        for (Object message : messages) {
            switch (message) {
                case null -> components.add(Component.text("null"));
                case ComponentLike componentLike -> components.add(componentLike.asComponent());
                case String string -> components.add(Component.text(string));
                default -> components.add(Component.text(String.valueOf(message)));
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

    @Override
    public @NotNull UUID getId() throws IllegalStateException {
        if (handle instanceof Entity entity)
            return entity.getUniqueId();
        throw new IllegalStateException(handle + " does not have an ID");
    }

    @ApiStatus.Internal
    public static void clearCache() {
        SENDERS_BY_ID.clear();
    }

    public static @NotNull BukkitCommandSender from(@NotNull org.bukkit.command.CommandSender sender) {
        return switch (sender) {
            case ConsoleCommandSender ignored -> BukkitCommandSender.CONSOLE;
            case Entity entity -> SENDERS_BY_ID.computeIfAbsent(entity.getUniqueId(), $ -> new BukkitCommandSender(sender));
            default -> new BukkitCommandSender(sender);
        };
    }

    @ApiStatus.Internal
    public static void removeFromCache(@NotNull UUID id) {
        SENDERS_BY_ID.remove(id);
    }
}
