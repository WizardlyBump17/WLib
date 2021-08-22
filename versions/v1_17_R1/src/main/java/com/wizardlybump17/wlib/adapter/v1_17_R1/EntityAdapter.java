package com.wizardlybump17.wlib.adapter.v1_17_R1;

import com.wizardlybump17.wlib.util.CollectionUtil;
import org.bukkit.conversations.Conversation;
import org.bukkit.craftbukkit.v1_17_R1.conversations.ConversationTracker;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
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

    @Override
    public List<Conversation> getConversations() {
        if (!(entity instanceof Player))
            return null;

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
        return null;
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
