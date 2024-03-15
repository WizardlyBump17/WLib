package com.wizardlybump17.wlib.adapter.v1_19_R1.player;

import com.wizardlybump17.wlib.util.ReflectionUtil;
import lombok.NonNull;
import org.bukkit.conversations.Conversation;
import org.bukkit.craftbukkit.v1_19_R1.conversations.ConversationTracker;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
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
        List<Conversation> queue = getConversationQueue(player);
        return queue.isEmpty() ? null : queue.get(0);
    }

    @Override
    public @NonNull List<Conversation> getConversationQueue(@NonNull Player player) {
        return getConversationQueue(getConversationTracker(player));
    }

    @Override
    public @NonNull List<Conversation> abandonConversations(@NonNull Player player, @NonNull Predicate<Conversation> filter) {
        List<Conversation> queue = getConversationQueue(player);
        List<Conversation> cache = List.copyOf(queue);
        List<Conversation> abandoned = new ArrayList<>(queue.size());

        for (Conversation conversation : cache) {
            if (!filter.test(conversation))
                continue;

            conversation.abandon();
            abandoned.add(conversation);
        }

        return abandoned;
    }

    public static @NonNull ConversationTracker getConversationTracker(@NonNull Player player) {
        return ReflectionUtil.getFieldValue(CONVERSATION_TRACKER, player);
    }

    public static @NonNull List<Conversation> getConversationQueue(@NonNull ConversationTracker tracker) {
        return ReflectionUtil.getFieldValue(CONVERSATION_QUEUE, tracker);
    }
}
