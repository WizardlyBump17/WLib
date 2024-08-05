package com.wizardlybump17.wlib.adapter.v1_21_R1.player;

import com.wizardlybump17.wlib.util.ReflectionUtil;
import lombok.NonNull;
import org.bukkit.conversations.Conversation;
import org.bukkit.craftbukkit.conversations.ConversationTracker;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PlayerAdapter extends com.wizardlybump17.wlib.adapter.player.PlayerAdapter {

    public static final @NonNull Field CONVERSATION_TRACKER = ReflectionUtil.getField("conversationTracker", CraftPlayer.class);
    public static final @NonNull Field CONVERSATION_QUEUE = ReflectionUtil.getField("conversationQueue", ConversationTracker.class);

    @Override
    public @Nullable Conversation getConversation(@NonNull Player player) {
        return getConversationQueue(player).getFirst();
    }

    @Override
    public @NonNull List<Conversation> getConversationQueue(@NonNull Player player) {
        return ReflectionUtil.getFieldValue(CONVERSATION_QUEUE, ReflectionUtil.getFieldValue(CONVERSATION_TRACKER, player));
    }

    @Override
    public @NonNull List<Conversation> abandonConversations(@NonNull Player player, @NonNull Predicate<Conversation> filter) {
        List<Conversation> queue = getConversationQueue(player);
        List<Conversation> removed = new ArrayList<>(queue);
        queue.removeIf(filter);
        removed.removeAll(queue);
        return removed;
    }
}
