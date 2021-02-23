package com.wizardlybump17.wlib.inventory.item;

import com.wizardlybump17.wlib.inventory.action.ItemClickAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
@Data
public class ItemButton {

    @Setter
    private ItemStack itemStack;
    private final ItemClickAction itemClickAction;

    public ItemButton(ItemStack itemStack) {
        this(itemStack, null);
    }
}
