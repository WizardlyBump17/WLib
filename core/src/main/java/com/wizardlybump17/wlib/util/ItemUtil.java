package com.wizardlybump17.wlib.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@UtilityClass
public class ItemUtil {

    public static String toBase64(ItemStack item) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(item);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStack fromBase64(String base64) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(base64));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack item = (ItemStack) dataInput.readObject();
            dataInput.close();
            return item;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toBase64(InventoryView view) {
        Inventory inventory = view.getTopInventory();
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeUTF(view.getTitle());
            dataOutput.writeByte(inventory.getSize());
            dataOutput.writeUTF(inventory.getType().name());

            for (ItemStack item : inventory)
                dataOutput.writeObject(item);

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads the given Base64 string and returns an inventory.<br>
     * No holder is used for the inventory
     * @param base64 the Base64 string
     * @return the inventory
     */
    public static Inventory fromBase64Inventory(String base64) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(base64));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            String title = dataInput.readUTF();
            int size = dataInput.readByte();

            InventoryType type = InventoryType.valueOf(dataInput.readUTF());
            Inventory inventory;
            if (type == InventoryType.CHEST)
                inventory = Bukkit.createInventory(null, size, title);
            else
                inventory = Bukkit.createInventory(null, type, title);

            for (int i = 0; i < size; i++)
                inventory.setItem(i, (ItemStack) dataInput.readObject());

            dataInput.close();
            return inventory;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
