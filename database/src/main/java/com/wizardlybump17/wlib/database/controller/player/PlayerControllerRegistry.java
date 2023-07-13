package com.wizardlybump17.wlib.database.controller.player;

import com.wizardlybump17.wlib.object.Registry;

/**
 * <p>
 *     A registry for {@link PlayerController}s.
 *     It is used to automatically load and unload players in the Spigot implementation of WLib.
 * </p>
 */
public class PlayerControllerRegistry extends Registry<Class<? extends PlayerController<?, ?, ?>>, PlayerController<?, ?, ?>> {

    /**
     * <p>The main instance of this class.</p>
     */
    public static final PlayerControllerRegistry INSTANCE = new PlayerControllerRegistry();
}
