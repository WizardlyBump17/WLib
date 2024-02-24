package com.wizardlybump17.wlib.controller.listener;

import com.wizardlybump17.wlib.database.controller.listener.ControllerListener;
import lombok.NonNull;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public interface ControllerEventListener<E extends Event> extends ControllerListener, Listener {

    void listen(@NonNull E event);

    @NonNull Class<E> getEventClass();

    @NonNull EventPriority getPriority();

    boolean ignoreCancelled();

    static <E extends Event> @NonNull ControllerEventListenerBuilder<E> builder() {
        return new ControllerEventListenerBuilder<>();
    }
}
