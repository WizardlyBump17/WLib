package com.wizardlybump17.wlib.item.handler;

import com.wizardlybump17.wlib.item.handler.model.ItemMetaHandlerModel;
import lombok.Data;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Data
public abstract class ItemMetaHandler<M extends ItemMetaHandlerModel<?>> {

    private final M model;
    private final @NotNull ItemMeta itemMeta;

    public abstract void serialize(Map<String, Object> map);

    public abstract void deserialize(Map<String, Object> map);

    public @NotNull ItemMeta getItemMeta() {
        return itemMeta;
    }
}
