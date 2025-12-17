package com.wizardlybump17.wlib.command;

import com.wizardlybump17.wlib.command.exception.SuggesterException;
import com.wizardlybump17.wlib.command.manager.CommandManager;
import com.wizardlybump17.wlib.command.result.CommandResult;
import com.wizardlybump17.wlib.command.result.SuccessResult;
import com.wizardlybump17.wlib.command.result.error.*;
import com.wizardlybump17.wlib.command.sender.BukkitCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WLibCommandExecutor implements CommandExecutor, TabCompleter {

    private final @NotNull CommandManager commandManager;
    private final @NotNull Logger logger;

    public WLibCommandExecutor(@NotNull CommandManager commandManager, @NotNull Logger logger) {
        this.commandManager = commandManager;
        this.logger = logger;
    }

    public @NotNull CommandManager getCommandManager() {
        return commandManager;
    }

    public @NotNull Logger getLogger() {
        return logger;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        com.wizardlybump17.wlib.command.sender.CommandSender<?> wlibSender = new BukkitCommandSender(sender);

        String wlibArgs = command.getName() + " " + String.join(" ", args);

        CommandResult<?> result = commandManager.execute(wlibSender, wlibArgs);
        switch (result) {
            case SuccessResult<?> successResult -> {}
            case ExceptionResult<?> exceptionResult -> {
                sender.sendMessage("§cAn internal error occurred while executing this command: " + exceptionResult.exception() + ".");
                logger.log(Level.SEVERE, "Error while " + sender + " tried to execute " + wlibArgs, exceptionResult.exception());
            }
            case OutOfRangeInputResult<?> outOfRangeInputResult -> sender.sendMessage("§cInvalid input at index " + outOfRangeInputResult.lastInputIndex() + ".");
            case ExtraArgumentsResult<?> extraArgumentsResult -> sender.sendMessage("§cExtra arguments provided at index " + extraArgumentsResult.lastInputIndex() + ".");
            case InsufficientArgumentsResult<?> insufficientArgumentsResult -> sender.sendMessage("§cInsufficient arguments provided.");
            case ParseInputExceptionResult<?> parseInputExceptionResult -> sender.sendMessage("§cInvalid input at index " + parseInputExceptionResult.lastInputIndex() + ": " + parseInputExceptionResult.exception().getMessage());
            case CommandNodeExecutorNotFoundResult<?> notFoundResult -> sender.sendMessage("§cNo executor found for this command.");
            case GenericErrorResult<?> genericErrorResult -> sender.sendMessage("§cAn error occurred while executing the command: " + genericErrorResult.message() + ".");
            case NoPermissionResult<?> noPermissionResult -> sender.sendMessage("§cYou do not have permission to execute this command.");
            case CommandNotFoundResult<?> notFoundResult -> sender.sendMessage("§cCommand not found.");
            case InvalidSenderResult<?> invalidSenderResult -> sender.sendMessage("§cYou can not execute this command.");
            default -> {}
        }

        return false;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        com.wizardlybump17.wlib.command.sender.CommandSender<?> wlibSender = new BukkitCommandSender(sender);

        String[] wlibArgs = new String[args.length + 1];
        wlibArgs[0] = command.getName();
        System.arraycopy(args, 0, wlibArgs, 1, args.length);

        String current = args.length == 1 ? args[0] : args[args.length - 1];
        String currentLowerCase = current.toLowerCase();

        try {
            return commandManager.getSuggestions(wlibSender, wlibArgs)
                    .stream()
                    .filter(suggestion -> suggestion.toLowerCase().startsWith(currentLowerCase))
                    .toList();
        } catch (SuggesterException e) {
            logger.log(Level.SEVERE, "Error while getting suggestions for " + sender + ": " + Arrays.toString(wlibArgs), e);
            return List.of();
        }
    }
}
