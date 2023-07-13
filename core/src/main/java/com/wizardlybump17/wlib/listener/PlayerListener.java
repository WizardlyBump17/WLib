package com.wizardlybump17.wlib.listener;

import com.wizardlybump17.wlib.WLib;
import com.wizardlybump17.wlib.controller.player.reason.UnloadReasons;
import com.wizardlybump17.wlib.database.controller.player.PlayerController;
import com.wizardlybump17.wlib.database.controller.player.PlayerControllerRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public record PlayerListener(WLib plugin) implements Listener {

    @EventHandler
    public void loadPlayer(PlayerJoinEvent event) {
        UUID id = event.getPlayer().getUniqueId();
        for (PlayerController<?, ?, ?> controller : PlayerControllerRegistry.INSTANCE.getValues())
            controller.loadPlayer(id);
    }

    @EventHandler
    public void unloadPlayer(PlayerQuitEvent event) {
        UUID id = event.getPlayer().getUniqueId();
        for (PlayerController<?, ?, ?> controller : PlayerControllerRegistry.INSTANCE.getValues())
            controller.unloadPlayer(id, UnloadReasons.QUIT);
    }
}
