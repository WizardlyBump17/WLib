package com.wizardlybump17.wlib.task;

import com.wizardlybump17.wlib.WLib;
import com.wizardlybump17.wlib.config.ConfigInfo;
import com.wizardlybump17.wlib.config.Path;
import com.wizardlybump17.wlib.database.cache.ControllerCache;
import com.wizardlybump17.wlib.database.controller.Controller;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;
import java.util.logging.Logger;

@EqualsAndHashCode(callSuper = true)
@Data
@ConfigInfo(name = "configs/save-controllers.yml", holderType = WLib.class)
public class SaveControllersTask extends BukkitRunnable {

    @Path("log")
    public static boolean log = true;
    @Path("delay")
    public static int delay = 5 * 60 * 20; // 5 minutes

    private final Logger logger;

    @Override
    public void run() {
        if (log)
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
