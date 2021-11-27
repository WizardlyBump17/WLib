package com.wizardlybump17.wlib.adapter.v1_13_R2;

import com.wizardlybump17.wlib.adapter.MessageType;
import com.wizardlybump17.wlib.util.CollectionUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.Packet;
import net.minecraft.server.v1_13_R2.PlayerConnection;
import org.bukkit.conversations.Conversation;
import org.bukkit.craftbukkit.v1_13_R2.conversations.ConversationTracker;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class EntityAdapter extends com.wizardlybump17.wlib.adapter.EntityAdapter {
    
    public EntityAdapter(Entity entity) {
        super(entity);
    }

    @Override
    public ItemStack getItemInMainHand() {
        if (!(entity instanceof HumanEntity))
            return null;
        return ((HumanEntity) entity).getInventory().getItemInMainHand();
    }

    @Override
    public ItemStack getItemInOffHand() {
        if (!(entity instanceof HumanEntity))
            return null;
        return ((HumanEntity) entity).getInventory().getItemInOffHand();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Conversation> getConversations() {
        if (!(entity instanceof Player))
            return new ArrayList<>();

        try {
            CraftPlayer player = (CraftPlayer) entity;

            Field trackerField = player.getClass().getDeclaredField("conversationTracker");
            trackerField.setAccessible(true);
            ConversationTracker conversationTracker = (ConversationTracker) trackerField.get(player);

            Field queueField = conversationTracker.getClass().getDeclaredField("conversationQueue");
            queueField.setAccessible(true);
            LinkedList<Conversation> conversationQueue = (LinkedList<Conversation>) queueField.get(conversationTracker);
            return new ArrayList<>(conversationQueue);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void sendMessage(MessageType type, String message) {
        if (!(entity instanceof Player))
            return;

        Player player = (Player) this.entity;

        switch (type) {
            case CHAT:
                player.sendMessage(message);
                return;

            case TITLE:
                String[] lines = message.split("\n");
                player.sendTitle(lines[0], lines.length == 1 ? null : lines[1], 10, 70, 20);
                return;

            case ACTION_BAR:
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
        }
    }

    @Override
    public void sendPacket(Object... packets) {
        if (!(entity instanceof Player))
            return;

        EntityPlayer player = ((CraftPlayer) entity).getHandle();
        PlayerConnection connection = player.playerConnection;

        for (Object packet : packets) {
            if (!(packet instanceof Packet))
                continue;

            connection.sendPacket(((Packet<?>) packet));
        }
    }

    @Override
    public boolean abandonConversation(Predicate<Conversation> predicate) {
        if (!(entity instanceof Player))
            return false;

        Conversation conversation = new CollectionUtil<>(getConversations()).getIf(predicate);
        if (conversation == null)
            return false;
        ((Player) entity).abandonConversation(conversation);
        return true;
    }
}
