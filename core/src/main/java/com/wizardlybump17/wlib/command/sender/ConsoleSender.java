package com.wizardlybump17.wlib.command.sender;

import com.wizardlybump17.wlib.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class ConsoleSender extends AbstractSender<ConsoleCommandSender> {

    public ConsoleSender(ConsoleCommandSender handle) {
        super(handle);
    }

    @Override
    public CommandSender<?> toGeneric() {
        return new GenericSender(getHandle());
    }
}
