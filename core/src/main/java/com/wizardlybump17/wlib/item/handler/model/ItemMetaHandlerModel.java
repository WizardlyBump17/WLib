package com.wizardlybump17.wlib.item.handler.model;

import com.wizardlybump17.wlib.item.ItemBuilder;
import com.wizardlybump17.wlib.item.handler.ItemMetaHandler;
import lombok.Data;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Data
public abstract class ItemMetaHandlerModel<H extends ItemMetaHandler<?>> {

    private static boolean modelsInitialized;
    public static final Map<Material, ItemMetaHandlerModel<?>> MODELS = new EnumMap<>(Material.class);

    private final Set<Material> applicableMaterials;

    public abstract H createHandler(ItemBuilder builder);

    public boolean isApplicable(Material material) {
        return applicableMaterials.contains(material);
    }

    @Nullable
    public static ItemMetaHandlerModel<?> getApplicableModel(Material material) {
        return MODELS.get(material);
    }

    public static void registerModel(ItemMetaHandlerModel<?> model) {
        for (Material material : model.getApplicableMaterials())
            MODELS.put(material, model);
    }

    public static void initModels() {
        if (modelsInitialized)
            throw new IllegalStateException("Default models are already initialized");

        registerModel(new LeatherArmorMetaHandlerModel());
        registerModel(new SkullMetaHandlerModel());

        modelsInitialized = true;
    }
}
