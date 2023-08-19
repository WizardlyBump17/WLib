package com.wizardlybump17.wlib.command.sender;

import lombok.NonNull;
import org.bukkit.command.CommandSender;

public class GenericSender extends AbstractSender<CommandSender> {

    public GenericSender(CommandSender handle) {
        super(handle);
    }

    @Override
    @NonNull
    public GenericSender toGeneric() {
        return this;
    }

    public static boolean isGeneric() {
        return true;
    }
}
