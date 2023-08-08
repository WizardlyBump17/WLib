package com.wizardlybump17.wlib.database.controller.player;

import com.wizardlybump17.wlib.object.Registry;

/**
 * <p>
 *     A registry for {@link PlayerLoadable}s.
 *     It is used to automatically load and unload players in the Spigot implementation of WLib.
 * </p>
 */
public class PlayerLoadableRegistry extends Registry<Class<? extends PlayerLoadable>, PlayerLoadable> {

    /**
     * <p>The main instance of this class.</p>
     */
    public static final PlayerLoadableRegistry INSTANCE = new PlayerLoadableRegistry();
}
