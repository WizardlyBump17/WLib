package com.wizardlybump17.wlib.adapter.v1_18_R2;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.mojang.datafixers.util.Pair;
import lombok.SneakyThrows;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSetSlot;
import net.minecraft.network.protocol.game.PacketPlayOutWindowItems;
import net.minecraft.network.syncher.DataWatcher;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EquipmentSlot;
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
                PacketType.fromClass(PacketPlayOutEntityEquipment.class),
                PacketType.fromClass(PacketPlayOutWindowItems.class)
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
        if (handle instanceof PacketPlayOutWindowItems)
            windowItems(((PacketPlayOutWindowItems) handle));
    }

    private boolean isValidItem(ItemStack itemStack) {
        final net.minecraft.world.item.ItemStack copy = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = copy.t();
        return tag != null && tag.e(GLOW_TAG);
    }

    private ItemStack fixItem(ItemStack itemStack) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private void windowItems(PacketPlayOutWindowItems handle) {
        final List<net.minecraft.world.item.ItemStack> items = handle.c();
        for (int i = 0; i < items.size(); i++) {
            final CraftItemStack itemStack = CraftItemStack.asCraftMirror(items.get(i));
            if (isValidItem(itemStack))
                items.set(i, CraftItemStack.asNMSCopy(fixItem(itemStack)));
        }
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private void entityEquipment(PacketPlayOutEntityEquipment handle) {
        final Field field = handle.getClass().getDeclaredField("c");
        field.setAccessible(true);

        List<Pair<EquipmentSlot, net.minecraft.world.item.ItemStack>> items = (List<Pair<EquipmentSlot, net.minecraft.world.item.ItemStack>>) field.get(handle);

        for (Pair<EquipmentSlot, net.minecraft.world.item.ItemStack> item : items) {
            final CraftItemStack stack = CraftItemStack.asCraftMirror(item.getSecond()).clone();
            if (!isValidItem(stack))
                continue;

            final Field second = item.getClass().getDeclaredField("second");
            second.setAccessible(true);

            second.set(item, CraftItemStack.asNMSCopy(fixItem(stack)));
        }
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private void entityMetadata(World world, PacketContainer packet, PacketPlayOutEntityMetadata handle) {
        final Entity entity;
        try {
            entity = packet.getEntityModifier(world).read(0);
        } catch (RuntimeException ignored) {
            return;
        }

        if (!(entity instanceof org.bukkit.entity.Item))
            return;

        final ItemStack itemStack = ((org.bukkit.entity.Item) entity).getItemStack().clone();
        if (!isValidItem(itemStack))
            return;

        final Field field = handle.getClass().getDeclaredField("b");
        field.setAccessible(true);
        List<DataWatcher.Item<?>> items = new ArrayList<>((List<DataWatcher.Item<?>>) field.get(handle));

        final DataWatcher.Item<?> item;

        int index;
        if (items.size() == 9)
            item = items.get(index = 4).d(); //new item
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
        final Field field = packet.getClass().getDeclaredField("f");
        field.setAccessible(true);

        final net.minecraft.world.item.ItemStack stack = (net.minecraft.world.item.ItemStack) field.get(packet);
        final CraftItemStack newItemStack = CraftItemStack.asCraftMirror(stack);
        if (!isValidItem(newItemStack))
            return;

        field.set(packet, CraftItemStack.asNMSCopy(fixItem(newItemStack)));
    }
}