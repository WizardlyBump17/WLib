package com.wizardlybump17.wlib.database.controller.player;

import com.wizardlybump17.wlib.database.controller.Controller;
import com.wizardlybump17.wlib.database.controller.player.reason.UnloadReason;
import lombok.NonNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * <p>
 *     An {@code interface} that represents a {@link Controller} that can load and unload its data by a player.
 * </p>
 */
public interface PlayerLoadable {

    /**
     * <p>
     *     Loads all data related to the player from the database and cache them.
     *     Called when the player joins the server.
     * </p>
     * @param id the player's id
     * @return a {@link CompletableFuture} that will be completed when all data is loaded
     */
    CompletableFuture<Void> loadPlayer(@NonNull UUID id);

    /**
     * <p>Unloads all data related to the player.</p>
     * @param id the player's id
     * @param reason the reason why the player is being unloaded
     * @return a {@link CompletableFuture} that will be completed when all data is unloaded
     */
    CompletableFuture<Void> unloadPlayer(@NonNull UUID id, @NonNull UnloadReason reason);
}
