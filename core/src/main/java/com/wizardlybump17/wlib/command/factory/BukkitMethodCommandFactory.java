package com.wizardlybump17.wlib.command.factory;

import com.wizardlybump17.commands.factory.MethodCommandFactory;
import com.wizardlybump17.commands.registered.RegisteredMethodCommand;
import lombok.NonNull;

import java.util.List;
import java.util.logging.Logger;

public class BukkitMethodCommandFactory extends MethodCommandFactory {

    public BukkitMethodCommandFactory(@NonNull Logger logger) {
        super(logger);
    }

    @Override
    public @NonNull List<RegisteredMethodCommand> createCommands(@NonNull Object object) {
        return super.createCommands(object);
    }
}
