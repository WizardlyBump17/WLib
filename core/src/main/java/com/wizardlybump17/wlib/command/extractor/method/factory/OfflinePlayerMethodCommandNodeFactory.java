package com.wizardlybump17.wlib.command.extractor.method.factory;

import com.wizardlybump17.wlib.command.annotation.Command;
import com.wizardlybump17.wlib.command.annotation.NonNullInput;
import com.wizardlybump17.wlib.command.input.AllowedOfflinePlayerInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.OfflinePlayerCommandNode;
import com.wizardlybump17.wlib.command.suggestion.OfflinePlayerSuggester;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class OfflinePlayerMethodCommandNodeFactory extends MethodCommandNodeFactory {

    @Override
    public @NotNull CommandNode<?> create(@NotNull Object object, @NotNull Method method, @NotNull Command commandAnnotation, @NotNull Parameter parameter, @NotNull String name, @Nullable CommandNode<?> root) {
        return new OfflinePlayerCommandNode(
                name,
                root == null ? List.of() : List.of(root),
                parameter.isAnnotationPresent(NonNullInput.class) ? AllowedOfflinePlayerInputs.anyNotNull() : AllowedOfflinePlayerInputs.anyNullable(),
                OfflinePlayerSuggester.online(),
                null,
                null
        );
    }

    @Override
    public @NotNull Class<?> @NotNull [] getSupportedTypes() {
        return new Class[] {OfflinePlayer.class, Player.class};
    }

    @Override
    public boolean isStrict() {
        return false;
    }
}
