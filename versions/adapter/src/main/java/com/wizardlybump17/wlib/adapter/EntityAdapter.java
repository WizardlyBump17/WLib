package com.wizardlybump17.wlib.adapter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Getter
public abstract class EntityAdapter {

    protected static final Map<UUID, EntityAdapter> ENTITY_CACHE = new HashMap<>();

    protected final Entity entity;

    public abstract ItemStack getItemInMainHand();
    public abstract ItemStack getItemInOffHand();
    public abstract List<Conversation> getConversations();
    public abstract boolean abandonConversation(Predicate<Conversation> predicate);

    public abstract void sendMessage(MessageType type, String message);
    public abstract void sendPacket(Object... packets);

    public static void deleteFromCache(Entity entity) {
        ENTITY_CACHE.remove(entity.getUniqueId());
    }
}
