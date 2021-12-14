package com.wizardlybump17.wlib.inventory.listener;

import com.wizardlybump17.wlib.inventory.paginated.PaginatedInventory;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode
@Builder
@Getter
public class InventoryListener<T extends Event> implements Listener {

    @NotNull
    private final Plugin plugin;
    @NotNull
    private final Class<T> eventClass;
    private final EventPriority priority;
    private final boolean ignoreCancelled;
    @NotNull
    private final ListenerConsumer<T> consumer;

    @SuppressWarnings("unchecked")
    public void fire(Event event, PaginatedInventory inventory) {
        if (eventClass.isInstance(event))
            consumer.fire((T) event, inventory);
    }

    public static class InventoryListenerBuilder<T extends Event> {

        public InventoryListener<T> build() {
            return new InventoryListener<>(plugin, eventClass, priority == null ? EventPriority.NORMAL : priority, ignoreCancelled, consumer);
        }
    }
}
