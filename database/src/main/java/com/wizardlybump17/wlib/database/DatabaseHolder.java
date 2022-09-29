package com.wizardlybump17.wlib.database;

import java.io.File;
import java.util.logging.Logger;

/**
 * Represents a database holder, which is a class that holds a database
 * @param <H> the database holder type
 */
public interface DatabaseHolder<H> {

    String getName();

    File getDataFolder();

    Logger getLogger();

    H getHandle();
}
