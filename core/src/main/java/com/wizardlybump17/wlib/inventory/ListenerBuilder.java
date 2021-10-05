package com.wizardlybump17.wlib.inventory;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;

import java.util.function.Predicate;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ListenerBuilder<T extends Event> {

    private Class<T> event;
    private InventoryEventListener<T> listener;
    private EventPriority priority = EventPriority.NORMAL;
    private Predicate<T> listenerPredicate;
    private boolean ignoreCancelled;

    @SuppressWarnings("unchecked")
    public void call(Event event, CustomInventory inventory) {
        if (listenerPredicate == null || listenerPredicate.test((T) event))
            listener.listen((T) event, inventory);
    }

    public void unregister() {
        HandlerList.unregisterAll(listener);
    }

    public static <T extends Event> ListenerBuilder<T> builder() {
        return new ListenerBuilder<>();
    }

    public ListenerBuilder<T> listenerPredicate(Predicate<T> predicate) {
        listenerPredicate = predicate;
        return this;
    }

    public ListenerBuilder<T> event(Class<T> event) {
        this.event = event;
        return this;
    }

    public ListenerBuilder<T> priority(EventPriority priority) {
        this.priority = priority;
        return this;
    }

    public ListenerBuilder<T> ignoreCancelled(boolean ignore) {
        ignoreCancelled = ignore;
        return this;
    }

    public ListenerBuilder<T> listener(InventoryEventListener<T> listener) {
        this.listener = listener;
        return this;
    }
}
