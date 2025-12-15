package com.wizardlybump17.wlib.command.node;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.AllowedInputs;
import com.wizardlybump17.wlib.command.input.AllowedPlayerInputs;
import com.wizardlybump17.wlib.command.suggestion.PlayerSuggester;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.UUID;

public class PlayerCommandNode extends CommandNode<Player> {

    public PlayerCommandNode(@NotNull String name, @NotNull @Unmodifiable List<CommandNode<?>> children, @NotNull AllowedInputs<Player> allowedInputs, @Nullable PlayerSuggester suggester, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, suggester, executor, permission);
    }

    @Override
    public @Nullable Player parse(@Nullable String input) throws InputParsingException {
        if (input == null)
            throw new InputParsingException("The input cannot be null");

        Player player = Bukkit.getPlayerExact(input);
        if (player != null)
            return player;

        if (input.length() == 36) {
            try {
                return Bukkit.getPlayer(UUID.fromString(input));
            } catch (IllegalArgumentException e) {
                throw new InputParsingException("Could not parse as UUID to get an Player: " + input, e);
            }
        }

        return null;
    }

    @Override
    public @NotNull AllowedPlayerInputs getAllowedInputs() {
        return (AllowedPlayerInputs) super.getAllowedInputs();
    }

    @Override
    public @Nullable PlayerSuggester getSuggester() {
        return (PlayerSuggester) super.getSuggester();
    }

    @Override
    public @NotNull PlayerCommandNode withChildren(@NotNull List<CommandNode<?>> children) {
        return new PlayerCommandNode(getName(), children, getAllowedInputs(), getSuggester(), getExecutor(), getPermission());
    }

    @Override
    public @NotNull PlayerCommandNode withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return new PlayerCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), executor, getPermission());
    }

    @Override
    public @NotNull PlayerCommandNode withPermission(@Nullable String permission) {
        return new PlayerCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), getExecutor(), permission);
    }
}
