package com.wizardlybump17.wlib.inventory.paginated;

import com.wizardlybump17.wlib.inventory.CustomInventory;
import com.wizardlybump17.wlib.inventory.CustomInventoryHolder;
import com.wizardlybump17.wlib.inventory.ItemButton;
import lombok.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PaginatedInventory {
    
    private final List<CustomInventory> inventories = new ArrayList<>();
    private final Map<Player, Integer> playerPages = new HashMap<>();

    public void addInventory(CustomInventory inventory) {
        inventories.add(inventory);
    }

    public void show(Player player, int page) {
        playerPages.put(player, page);
        player.openInventory(inventories.get(page).getBukkitInventory());
    }

    public void showNextPage(Player player) {
        show(player, playerPages.getOrDefault(player, -1) + 1);
    }

    public void showPreviousPage(Player player) {
        show(player, playerPages.getOrDefault(player, 1) - 1);
    }

    public static PaginatedInventoryBuilder builder() {
        return new PaginatedInventoryBuilder();
    }

    public static class PaginatedInventoryBuilder {

        private List<ItemButton> content;
        private String title, shape;
        private final Map<Character, ItemButton> shapeReplacements = new HashMap<>();
        private ItemStack nextPage, previousPage;

        public PaginatedInventoryBuilder title(String title) {
            this.title = title;
            return this;
        }

        public PaginatedInventoryBuilder shape(String shape) {
            this.shape = shape;
            return this;
        }

        public PaginatedInventoryBuilder item(ItemButton item) {
            if (content == null)
                content = new ArrayList<>();
            content.add(item);
            return this;
        }

        public PaginatedInventoryBuilder content(List<ItemButton> content) {
            this.content = content;
            return this;
        }

        public PaginatedInventoryBuilder shapeReplacement(char character, ItemButton item) {
            shapeReplacements.put(character, item);
            return this;
        }

        public PaginatedInventoryBuilder nextPage(ItemStack item) {
            nextPage = item;
            return this;
        }

        public PaginatedInventoryBuilder previousPage(ItemStack item) {
            previousPage = item;
            return this;
        }
        
        public PaginatedInventory build() {
            if (title == null || shape == null)
                throw new NullPointerException();
            PaginatedInventory paginatedInventory = new PaginatedInventory();
            
            char[] shapeChar = shape.toCharArray();
            int presetItems = 0;
            for (char c : shapeChar)
                if (c != 'x')
                    presetItems++;


            int inventoriesAmount = content.size() == 0
                    ? 1
                    : (int) Math.ceil((double) content.size() / (shape.length() - presetItems));

            int currentItem = 0;
            for (int i = 0; i < inventoriesAmount; i++) {
                CustomInventory inventory = new CustomInventory(title.replace("{page}", Integer.toString(i + 1)), shape.length());
                CustomInventoryHolder holder = inventory.getOwner();

                for (int slot = 0; slot < shape.length(); slot++) {
                    char currentChar = shapeChar[slot];

                    if (currentChar != 'x' && currentChar != '>' && currentChar != '<' && shapeReplacements.containsKey(currentChar))
                        continue;

                    if (currentChar == 'x' && content.size() > 0 && currentItem < content.size()) {
                        holder.setButton(slot, content.get(currentItem++));
                        continue;
                    }

                    if (currentChar == '<') {
                        holder.setButton(slot, new ItemButton(
                                previousPage,
                                event -> paginatedInventory.showPreviousPage((Player) event.getWhoClicked())));
                        continue;
                    }

                    if (currentChar == '>') {
                        holder.setButton(slot, new ItemButton(
                                nextPage,
                                event -> paginatedInventory.showNextPage((Player) event.getWhoClicked())));
                        continue;
                    }

                    holder.setButton(slot, shapeReplacements.get(currentChar));
                }

                paginatedInventory.addInventory(inventory);
            }

            return paginatedInventory;
        }
    }
}
