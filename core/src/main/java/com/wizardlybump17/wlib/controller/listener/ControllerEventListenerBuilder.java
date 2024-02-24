package com.wizardlybump17.wlib.controller.listener;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

import java.util.function.Consumer;

@Data
@Accessors(fluent = true, chain = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class ControllerEventListenerBuilder<E extends Event> {

    private Class<E> eventClass;
    private EventPriority priority = EventPriority.NORMAL;
    private boolean ignoreCancelled;
    private Consumer<E> consumer;

    public @NonNull ControllerEventListener<E> build() {
        return new ControllerEventListener<>() {
            @Override
            public void listen(@NonNull E event) {
                consumer.accept(event);
            }

            @Override
            public @NonNull Class<E> getEventClass() {
                return eventClass;
            }

            @Override
            public @NonNull EventPriority getPriority() {
                return priority;
            }

            @Override
            public boolean ignoreCancelled() {
                return ignoreCancelled;
            }
        };
    }
}
