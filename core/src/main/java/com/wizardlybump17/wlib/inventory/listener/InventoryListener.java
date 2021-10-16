package com.wizardlybump17.wlib.inventory.listener;

import com.wizardlybump17.wlib.inventory.CustomInventory;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;

import java.util.function.Predicate;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InventoryListener<T extends Event> {

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

    public static <T extends Event> InventoryListener<T> builder() {
        return new InventoryListener<>();
    }

    public InventoryListener<T> listenerPredicate(Predicate<T> predicate) {
        listenerPredicate = predicate;
        return this;
    }

    public InventoryListener<T> event(Class<T> event) {
        this.event = event;
        return this;
    }

    public InventoryListener<T> priority(EventPriority priority) {
        this.priority = priority;
        return this;
    }

    public InventoryListener<T> ignoreCancelled(boolean ignore) {
        ignoreCancelled = ignore;
        return this;
    }

    public InventoryListener<T> listener(InventoryEventListener<T> listener) {
        this.listener = listener;
        return this;
    }
}
