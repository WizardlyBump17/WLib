package com.wizardlybump17.wlib.command.sender;

import org.bukkit.command.CommandSender;

public class GenericSender extends AbstractSender<CommandSender> {

    public GenericSender(CommandSender handle) {
        super(handle);
    }

    @Override
    public com.wizardlybump17.wlib.command.CommandSender<?> toGeneric() {
        return this;
    }

    public static boolean isGeneric() {
        return true;
    }
}
