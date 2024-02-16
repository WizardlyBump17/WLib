package com.wizardlybump17.wlib.command.sender.factory;

import com.wizardlybump17.wlib.command.sender.PlayerSender;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class PlayerSenderFactory extends CommandSenderFactory<Player, PlayerSender> {

    public PlayerSenderFactory() {
        super(Player.class);
    }

    @Override
    public @NonNull PlayerSender create(@NonNull Player handle) {
        return new PlayerSender(handle);
    }
}
