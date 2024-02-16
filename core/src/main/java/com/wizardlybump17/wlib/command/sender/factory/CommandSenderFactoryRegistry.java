package com.wizardlybump17.wlib.command.sender.factory;

import com.wizardlybump17.wlib.object.Registry;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandSenderFactoryRegistry extends Registry<Class<?>, CommandSenderFactory<?, ?>> {

    public static final @NonNull CommandSenderFactoryRegistry INSTANCE = new CommandSenderFactoryRegistry();

    static {
        INSTANCE.add(new PlayerSenderFactory());
    }

    public void add(CommandSenderFactory<?, ?> factory) {
        put(factory.getBukkitSenderClass(), factory);
    }
}
