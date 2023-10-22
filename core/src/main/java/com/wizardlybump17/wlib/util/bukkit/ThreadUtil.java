package com.wizardlybump17.wlib.util.bukkit;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@UtilityClass
public class ThreadUtil {

    /**
     * <p>
     *     Runs the given {@link Runnable} in the main thread.
     *     If it is not in the main thread, it will call the {@link org.bukkit.scheduler.BukkitScheduler#runTask(Plugin, Runnable)} to run it in the next tick.
     * </p>
     * @param plugin the plugin
     * @param runnable the runnable
     */
    public static void runSync(@NonNull Plugin plugin, @NonNull Runnable runnable) {
        if (Bukkit.isPrimaryThread())
            runnable.run();
        else
            Bukkit.getScheduler().runTask(plugin, runnable);
    }

    /**
     * <p>
     *     Runs the given {@link Runnable} asynchronously.
     *     If it is in the main thread, it will call the {@link org.bukkit.scheduler.BukkitScheduler#runTaskAsynchronously(Plugin, Runnable)} to run it in the next tick.
     * </p>
     * @param plugin the plugin
     * @param runnable the runnable
     */
    public static void runAsync(@NonNull Plugin plugin, @NonNull Runnable runnable) {
        if (!Bukkit.isPrimaryThread())
            runnable.run();
        else
            Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }
}
