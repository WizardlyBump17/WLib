package com.wizardlybump17.wlib.item.handler.model;

import com.wizardlybump17.wlib.item.ItemBuilder;
import com.wizardlybump17.wlib.item.handler.ItemMetaHandler;
import lombok.Data;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public abstract class ItemMetaHandlerModel<H extends ItemMetaHandler<?>> {

    private static boolean modelsInitialized;
    public static final List<ItemMetaHandlerModel<?>> MODELS = new ArrayList<>();

    private final Set<Material> applicableMaterials;

    public abstract H createHandler(ItemBuilder builder);

    public boolean isApplicable(Material material) {
        return applicableMaterials.contains(material);
    }

    @Nullable
    public static ItemMetaHandlerModel<?> getApplicableModel(Material material) {
        for (ItemMetaHandlerModel<?> model : MODELS)
            if (model.isApplicable(material))
                return model;
        return null;
    }

    public static void initModels() {
        if (modelsInitialized)
            throw new IllegalStateException("Default models are already initialized");

        MODELS.add(new LeatherArmorMetaHandlerModel());

        modelsInitialized = true;
    }
}
