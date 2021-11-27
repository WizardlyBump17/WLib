package com.wizardlybump17.wlib.adapter.v1_8_R3;

import com.wizardlybump17.wlib.adapter.MessageType;
import com.wizardlybump17.wlib.util.CollectionUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.conversations.Conversation;
import org.bukkit.craftbukkit.v1_8_R3.conversations.ConversationTracker;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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
        return ((HumanEntity) entity).getItemInHand();
    }

    @Override
    public ItemStack getItemInOffHand() {
        return getItemInMainHand();
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

            case TITLE: {
                String[] lines = message.split("\n");

                PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, new ChatComponentText(lines[0]), 10, 70, 20);
                PacketPlayOutTitle subtitlePacket;
                if (lines.length >= 2)
                    subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, new ChatComponentText(lines[1]), 10, 70, 20);
                else
                    subtitlePacket = null;

                sendPacket(titlePacket, subtitlePacket);
                return;
            }

            case ACTION_BAR:
                sendPacket(new PacketPlayOutChat(new ChatComponentText(message), (byte) 2));
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
