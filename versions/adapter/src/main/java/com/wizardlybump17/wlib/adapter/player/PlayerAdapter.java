package com.wizardlybump17.wlib.adapter.player;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public abstract class PlayerAdapter {

    @Getter
    private static PlayerAdapter instance;

    public abstract @Nullable Conversation getConversation(@NonNull Player player);

    public abstract @NonNull List<Conversation> getConversationQueue(@NonNull Player player);

    public abstract @NonNull List<Conversation> abandonConversations(@NonNull Player player, @NonNull Predicate<Conversation> filter);

    public static void setInstance(PlayerAdapter instance) {
        if (PlayerAdapter.instance != null)
            throw new IllegalStateException("The PlayerAdapter instance is already set");
        PlayerAdapter.instance = instance;
    }
}
