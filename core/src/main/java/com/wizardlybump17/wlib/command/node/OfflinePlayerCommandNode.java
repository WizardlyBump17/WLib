package com.wizardlybump17.wlib.command.node;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.AllowedInputs;
import com.wizardlybump17.wlib.command.suggestion.Suggester;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.UUID;

public class OfflinePlayerCommandNode extends CommandNode<OfflinePlayer> {

    public OfflinePlayerCommandNode(@NotNull String name, @NotNull @Unmodifiable List<CommandNode<?>> children, @NotNull AllowedInputs<OfflinePlayer> allowedInputs, @Nullable Suggester<OfflinePlayer> suggester, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, suggester, executor, permission);
    }

    @Override
    public @Nullable OfflinePlayer parse(@Nullable String input) throws InputParsingException {
        if (input == null)
            throw new InputParsingException("The input cannot be null");

        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(input);
        if (player != null)
            return player;

        if (input.length() == 36) {
            try {
                return Bukkit.getOfflinePlayer(UUID.fromString(input));
            } catch (IllegalArgumentException e) {
                throw new InputParsingException("Could not parse as UUID to get an OfflinePlayer: " + input, e);
            }
        }

        return null;
    }

    @Override
    public @NotNull OfflinePlayerCommandNode withChildren(@NotNull List<CommandNode<?>> children) {
        return new OfflinePlayerCommandNode(getName(), children, getAllowedInputs(), getSuggester(), getExecutor(), getPermission());
    }

    @Override
    public @NotNull OfflinePlayerCommandNode withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return new OfflinePlayerCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), executor, getPermission());
    }

    @Override
    public @NotNull OfflinePlayerCommandNode withPermission(@Nullable String permission) {
        return new OfflinePlayerCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), getExecutor(), permission);
    }
}
