package com.wizardlybump17.wlib.task;

import com.wizardlybump17.wlib.database.cache.ControllerCache;
import com.wizardlybump17.wlib.database.controller.Controller;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;
import java.util.logging.Logger;

@EqualsAndHashCode(callSuper = true)
@Data
public class SaveControllersTask extends BukkitRunnable {

    public static final int DELAY = 5 * 60 * 20; // 5 minutes

    private final Logger logger;

    @Override
    public void run() {
        logger.info("Saving controllers...");

        for (Controller<?, ?, ?, ?> controller : ControllerCache.INSTANCE.getCache()) {
            controller.save().exceptionally(throwable -> {
                logger.log(
                        Level.SEVERE,
                        "An error occurred while trying to save the %s".formatted(controller.getClass().getName()),
                        throwable
                );
                return null;
            });
        }
    }
}
