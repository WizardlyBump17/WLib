package com.wizardlybump17.wlib.command.sender.factory;

import lombok.Data;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class CommandSenderFactory<I extends CommandSender, O extends com.wizardlybump17.commands.sender.CommandSender<I>> {

    private final @NonNull Class<I> bukkitSenderClass;
    private final @NonNull Map<I, O> cache = new HashMap<>();

    public abstract @NonNull O create(@NonNull I handle);

    public @NonNull O get(@NonNull I handle) {
        return cache.computeIfAbsent(handle, this::create);
    }
}
