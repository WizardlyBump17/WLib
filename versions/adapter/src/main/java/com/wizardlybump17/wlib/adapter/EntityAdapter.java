package com.wizardlybump17.wlib.adapter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Getter
public abstract class EntityAdapter {

    protected final Entity entity;

    public abstract ItemStack getItemInMainHand();
    public abstract ItemStack getItemInOffHand();
    public abstract List<Conversation> getConversations();
    public abstract boolean abandonConversation(Predicate<Conversation> predicate);
}
