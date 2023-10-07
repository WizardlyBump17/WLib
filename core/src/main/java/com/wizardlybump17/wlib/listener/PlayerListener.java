package com.wizardlybump17.wlib.listener;

import com.wizardlybump17.wlib.WLib;
import com.wizardlybump17.wlib.controller.player.reason.UnloadReasons;
import com.wizardlybump17.wlib.database.cache.ControllerCache;
import com.wizardlybump17.wlib.database.controller.Controller;
import com.wizardlybump17.wlib.database.controller.player.PlayerController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public record PlayerListener(WLib plugin) implements Listener {

    @EventHandler
    public void loadPlayer(PlayerJoinEvent event) {
        UUID id = event.getPlayer().getUniqueId();
        for (Controller<?, ?, ?, ?> controller : ControllerCache.INSTANCE.getCache())
            if (controller instanceof PlayerController<?, ?, ?> playerController)
                playerController.loadPlayer(id);
    }

    @EventHandler
    public void unloadPlayer(PlayerQuitEvent event) {
        UUID id = event.getPlayer().getUniqueId();
        for (Controller<?, ?, ?, ?> controller : ControllerCache.INSTANCE.getCache())
            if (controller instanceof PlayerController<?, ?, ?> playerController)
                playerController.unloadPlayer(id, UnloadReasons.QUIT);
    }
}
