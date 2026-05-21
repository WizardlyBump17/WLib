package com.wizardlybump17.wlib.command.suggestion;

import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface OfflinePlayerSuggester extends Suggester<OfflinePlayer> {

    @Override
    default @NotNull String getStringRepresentation(@NotNull OfflinePlayer value) {
        String name = value.getName();
        return name == null ? value.getUniqueId().toString() : name;
    }

    static @NotNull Online online() {
        return Online.INSTANCE;
    }

    static @NotNull Cached cached() {
        return Cached.INSTANCE;
    }

    final class Online implements OfflinePlayerSuggester {

        private static final @NotNull Online INSTANCE = new Online();

        private Online() {
        }

        @Override
        public @NotNull List<OfflinePlayer> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current, @NotNull CommandNode<?> currentNode) {
            return Bukkit.getOnlinePlayers()
                    .stream()
                    .map(OfflinePlayer.class::cast)
                    .toList();
        }
    }

    final class Cached implements OfflinePlayerSuggester {

        private static final @NotNull Cached INSTANCE = new Cached();

        private Cached() {
        }

        @Override
        public @NotNull List<OfflinePlayer> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current, @NotNull CommandNode<?> currentNode) {
            return List.of(Bukkit.getOfflinePlayers());
        }
    }
}
