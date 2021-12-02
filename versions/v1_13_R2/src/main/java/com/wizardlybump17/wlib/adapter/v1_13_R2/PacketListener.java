package com.wizardlybump17.wlib.adapter.v1_13_R2;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import lombok.SneakyThrows;
import net.minecraft.server.v1_13_R2.DataWatcher;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_13_R2.PacketPlayOutSetSlot;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.wizardlybump17.wlib.adapter.NMSAdapter.GLOW_TAG;

public class PacketListener extends PacketAdapter {

    public PacketListener() {
        super(
                Bukkit.getPluginManager().getPlugin("WLib"),
                PacketType.fromClass(PacketPlayOutSetSlot.class),
                PacketType.fromClass(PacketPlayOutEntityMetadata.class),
                PacketType.fromClass(PacketPlayOutEntityEquipment.class)
        );
    }

    @SneakyThrows
    @Override
    public void onPacketSending(PacketEvent event) {
        final Object handle = event.getPacket().getHandle();
        if (handle instanceof PacketPlayOutSetSlot)
            setSlot((PacketPlayOutSetSlot) handle);
        if (handle instanceof PacketPlayOutEntityMetadata)
            entityMetadata(event.getPlayer().getWorld(), event.getPacket(), (PacketPlayOutEntityMetadata) handle);
        if (handle instanceof PacketPlayOutEntityEquipment)
            entityEquipment((PacketPlayOutEntityEquipment) handle);
    }

    private boolean isInvalid(ItemStack itemStack) {
        final net.minecraft.server.v1_13_R2.ItemStack copy = CraftItemStack.asNMSCopy(itemStack);
        return !copy.hasTag() || !copy.getTag().hasKey(GLOW_TAG);
    }

    private ItemStack fixItem(ItemStack itemStack) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @SneakyThrows
    public void entityEquipment(PacketPlayOutEntityEquipment handle) {
        final Field field = handle.getClass().getDeclaredField("c");
        field.setAccessible(true);

        net.minecraft.server.v1_13_R2.ItemStack item = (net.minecraft.server.v1_13_R2.ItemStack) field.get(handle);
        if (isInvalid(CraftItemStack.asCraftMirror(item)))
            return;

        field.set(handle, CraftItemStack.asNMSCopy(fixItem(CraftItemStack.asCraftMirror(item.cloneItemStack()))));
    }

    @SneakyThrows
    private void entityMetadata(World world, PacketContainer packet, PacketPlayOutEntityMetadata handle) {
        final Entity entity = packet.getEntityModifier(world).read(0);
        if (!(entity instanceof org.bukkit.entity.Item))
            return;

        final ItemStack itemStack = ((org.bukkit.entity.Item) entity).getItemStack().clone();
        if (isInvalid(itemStack))
            return;

        final Field field = handle.getClass().getDeclaredField("b");
        field.setAccessible(true);
        List<DataWatcher.Item<?>> items = new ArrayList<>((List<DataWatcher.Item<?>>) field.get(handle));

        final DataWatcher.Item<?> item;

        int index;
        if (items.size() == 8)
            item = items.get(index = 6).d(); //new item
        else
            item = items.get(index = 0).d(); //item merge
        final Field itemField = item.getClass().getDeclaredField("b");
        itemField.setAccessible(true);
        itemField.set(item, CraftItemStack.asNMSCopy(fixItem(itemStack)));
        items.set(index, item);

        field.set(handle, items);
    }

    @SneakyThrows
    private void setSlot(PacketPlayOutSetSlot packet) {
        final Field field = packet.getClass().getDeclaredField("c");
        field.setAccessible(true);

        final net.minecraft.server.v1_13_R2.ItemStack stack = ((net.minecraft.server.v1_13_R2.ItemStack) field.get(packet)).cloneItemStack();
        final CraftItemStack newItemStack = CraftItemStack.asCraftMirror(stack);
        if (isInvalid(newItemStack))
            return;

        field.set(packet, CraftItemStack.asNMSCopy(fixItem(newItemStack)));
    }
}
