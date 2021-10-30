package com.wizardlybump17.wlib.command.sender;

import com.wizardlybump17.wlib.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerSender extends AbstractSender<Player> {

    public PlayerSender(Player handle) {
        super(handle);
    }

    @Override
    public CommandSender<?> toGeneric() {
        return new GenericSender(getHandle());
    }
}
