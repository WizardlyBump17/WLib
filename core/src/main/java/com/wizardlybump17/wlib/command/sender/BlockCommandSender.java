package com.wizardlybump17.wlib.command.sender;

public class BlockCommandSender extends AbstractSender<org.bukkit.command.BlockCommandSender> {

    public BlockCommandSender(org.bukkit.command.BlockCommandSender handle) {
        super(handle);
    }

    @Override
    public GenericSender toGeneric() {
        return new GenericSender(getHandle());
    }
}
