package com.wizardlybump17.wlib.command.factory;

import com.wizardlybump17.commands.factory.DirectCommandFactory;
import lombok.NonNull;

import java.util.logging.Logger;

public class BukkitDirectCommandFactory extends DirectCommandFactory {

    public BukkitDirectCommandFactory(@NonNull Logger logger) {
        super(logger);
    }
}
