package com.wizardlybump17.wlib.inventory.item;

import com.wizardlybump17.wlib.adapter.WMaterial;
import com.wizardlybump17.wlib.item.Item;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@Data
@RequiredArgsConstructor
public class ItemButton {

    public static final ItemButton BLACK_STAINED_GLASS_PANE = new ItemButton(
            Item.builder()
                    .type(WMaterial.BLACK_STAINED_GLASS_PANE)
                    .displayName(" ")
                    .build()
    );

    @NotNull
    private final Supplier<ItemStack> item;
    @Nullable
    private final ClickAction clickAction;

    public ItemButton(@NotNull ItemStack item) {
        this(item, null);
    }

    public ItemButton(@NotNull ItemStack item, @Nullable ClickAction clickAction) {
        this.item = () -> item;
        this.clickAction = clickAction;
    }

    public ItemButton(@NotNull Supplier<ItemStack> item) {
        this(item, null);
    }
}
