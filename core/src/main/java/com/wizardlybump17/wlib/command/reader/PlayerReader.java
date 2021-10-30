package com.wizardlybump17.wlib.command.reader;

import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerReader extends ArgsReader<Player> {

    @Override
    public Class<Player> getType() {
        return Player.class;
    }

    @Override
    public Player read(String string) {
        return Bukkit.getPlayerExact(string);
    }
}
