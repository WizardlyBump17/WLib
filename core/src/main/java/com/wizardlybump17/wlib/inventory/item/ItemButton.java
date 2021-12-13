package com.wizardlybump17.wlib.inventory.item;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@Data
@RequiredArgsConstructor
public class ItemButton {

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
}
