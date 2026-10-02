package com.wizardlybump17.wlib.adapter.v1_21_R5.player;

import com.wizardlybump17.wlib.adapter.v1_21_R5.BaseAdapter;
import com.wizardlybump17.wlib.util.ReflectionUtil;
import org.bukkit.conversations.Conversation;
import org.bukkit.craftbukkit.conversations.ConversationTracker;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PlayerAdapter extends com.wizardlybump17.wlib.adapter.player.PlayerAdapter implements BaseAdapter {

    public static final @NotNull Field CONVERSATION_TRACKER = ReflectionUtil.getField("conversationTracker", CraftPlayer.class);
    public static final @NotNull Field CONVERSATION_QUEUE = ReflectionUtil.getField("conversationQueue", ConversationTracker.class);

    @Override
    public @Nullable Conversation getConversation(@NotNull Player player) {
        return getConversationQueue(player).getFirst();
    }

    @Override
    public @NotNull List<Conversation> getConversationQueue(@NotNull Player player) {
        return ReflectionUtil.getFieldValue(CONVERSATION_QUEUE, ReflectionUtil.getFieldValue(CONVERSATION_TRACKER, player));
    }

    @Override
    public @NotNull List<Conversation> abandonConversations(@NotNull Player player, @NotNull Predicate<Conversation> filter) {
        List<Conversation> queue = getConversationQueue(player);
        List<Conversation> removed = new ArrayList<>(queue);
        queue.removeIf(filter);
        removed.removeAll(queue);
        return removed;
    }
}
