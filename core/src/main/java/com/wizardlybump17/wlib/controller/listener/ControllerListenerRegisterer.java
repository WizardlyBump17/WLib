package com.wizardlybump17.wlib.controller.listener;

import com.wizardlybump17.wlib.database.controller.Controller;
import com.wizardlybump17.wlib.database.controller.listener.ControllerListener;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

@UtilityClass
public class ControllerListenerRegisterer {

    @SuppressWarnings("unchecked")
    public static void register(@NonNull Controller<?, ?, ?, ?> controller, @NonNull Plugin plugin) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        for (ControllerListener listener : controller.getListeners()) {
            if (listener instanceof ControllerEventListener<?> eventListener) {
                pluginManager.registerEvent(
                        eventListener.getEventClass(),
                        eventListener,
                        eventListener.getPriority(),
                        (a, event) -> ((ControllerEventListener<Event>) eventListener).listen(event),
                        plugin,
                        eventListener.ignoreCancelled()
                );
            }
        }
    }

    public static void unregister(@NonNull Controller<?, ?, ?, ?> controller) {
        for (ControllerListener listener : controller.getListeners()) {
            if (listener instanceof ControllerEventListener<?> eventListener)
                HandlerList.unregisterAll(eventListener);
        }
    }
}
