package com.wizardlybump17.wlib.command.registered;

import com.wizardlybump17.commands.executor.MethodCommandExecutor;
import com.wizardlybump17.commands.manager.CommandManager;
import com.wizardlybump17.commands.registered.RegisteredMethodCommand;
import lombok.NonNull;

public class RegisteredBukkitMethodCommand extends RegisteredMethodCommand {

    public RegisteredBukkitMethodCommand(@NonNull MethodCommandExecutor executor) {
        super(executor);
    }

    @Override
    public boolean onRegister(@NonNull CommandManager commandManager) {
        return true;
    }
}
