package com.wizardlybump17.wlib.command.suggestion;

import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface PlayerSuggester extends Suggester<Player> {

    @Override
    default @NotNull String getStringRepresentation(@NotNull Player value) {
        return value.getName();
    }

    static @NotNull Online online() {
        return Online.INSTANCE;
    }

    final class Online implements PlayerSuggester {

        private static final @NotNull Online INSTANCE = new Online();

        private Online() {
        }

        @Override
        public @NotNull List<Player> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current, @NotNull CommandNode<?> currentNode) {
            return new ArrayList<>(Bukkit.getOnlinePlayers());
        }

        @Override
        public String toString() {
            return "PlayerSuggester$Online{}";
        }
    }
}
