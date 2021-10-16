package com.wizardlybump17.wlib.inventory.item;

import com.wizardlybump17.wlib.item.Item;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@Getter
public class UpdatableItem extends ItemButton {

    private final UpdateAction updateAction;

    public UpdatableItem(ItemStack itemStack, ClickAction clickAction, UpdateAction updateAction) {
        super(itemStack, clickAction);
        this.updateAction = updateAction;
    }

    public UpdatableItem(ItemStack itemStack, UpdateAction updateAction) {
        this(itemStack, null, updateAction);
    }

    public static UpdatableItem backAndForth(List<ItemStack> items, ClickAction clickAction, boolean reverseMode) {
        if (reverseMode)
            return new UpdatableItem(
                    items.get(0),
                    clickAction,
                    new UpdateAction() {

                        final ListIterator<ItemStack> iterator = items.listIterator();

                        int direction = 0;

                        @Override
                        public void update(Item.ItemBuilder builder) {
                            if (!iterator.hasNext()) {
                                direction = -1;
                                iterator.previous();
                            }
                            if (!iterator.hasPrevious()) {
                                direction = 1;
                                iterator.next();
                            }
                            builder.copy(direction == -1 ? iterator.previous() : iterator.next());
                        }
                    }
            );
        return new UpdatableItem(
                items.get(0),
                clickAction,
                new UpdateAction() {

                    Iterator<ItemStack> iterator = items.iterator();

                    @Override
                    public void update(Item.ItemBuilder builder) {
                        if (!iterator.hasNext())
                            iterator = items.iterator();
                        builder.copy(iterator.next());
                    }
                }
        );
    }

    public interface UpdateAction {

        /**
         * @param builder the item builder of this item. Useful for who wants to edit the item
         */
        void update(Item.ItemBuilder builder);
    }
}
