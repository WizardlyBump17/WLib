package com.wizardlybump17.wlib.database.controller.player;

import com.wizardlybump17.wlib.database.controller.Controller;
import com.wizardlybump17.wlib.database.controller.player.reason.UnloadReason;
import com.wizardlybump17.wlib.database.dao.DAO;
import com.wizardlybump17.wlib.object.Cache;
import lombok.NonNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * <p>A {@link Controller} that uses {@link UUID} as key and it is focused on players.</p>
 * <p>
 *     You may need to use the {@link com.wizardlybump17.wlib.database.controller.player.PlayerControllerRegistry}
 *     in order to automatically load and unload a player in the Spigot implementation of WLib.
 * </p>
 * @param <V> the value type
 * @param <C> the {@link Cache} type
 * @param <D> the {@link DAO} type
 * @see com.wizardlybump17.wlib.database.controller.player.PlayerControllerRegistry
 */
public abstract class PlayerController<V, C extends Cache<UUID, V, ?>, D extends DAO<UUID, V>> extends Controller<UUID, V, C, D> implements PlayerLoadable {

    public PlayerController(@NonNull C cache, @NonNull D dao) {
        super(cache, dao);
    }

    /**
     * <p>
     *     Load the player from the database and cache it.
     *     Called when the player joins the server.
     * </p>
     * @param id the player's id
     * @return a {@link CompletableFuture} that will be completed when the player is loaded
     */
    @Override
    public abstract CompletableFuture<Void> loadPlayer(@NonNull UUID id);

    /**
     * <p>Unloads the player.</p>
     * @param id the player's id
     * @param reason the reason why the player is being unloaded
     * @return a {@link CompletableFuture} that will be completed when the player is unloaded
     */
    @Override
    public abstract CompletableFuture<Void> unloadPlayer(@NonNull UUID id, @NonNull UnloadReason reason);
}
