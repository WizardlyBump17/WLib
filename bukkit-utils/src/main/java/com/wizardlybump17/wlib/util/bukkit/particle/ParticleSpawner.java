package com.wizardlybump17.wlib.util.bukkit.particle;

import com.wizardlybump17.wlib.util.builder.MapBuilder;
import com.wizardlybump17.wlib.util.bukkit.ConfigUtil;
import com.wizardlybump17.wlib.util.bukkit.config.ConfigWrapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Data
@AllArgsConstructor
@SerializableAs("WLib:Particle")
@Builder
public class ParticleSpawner implements ConfigurationSerializable {

    private @NonNull Particle type;
    private int count;
    private double offsetX;
    private double offsetY;
    private double offsetZ;
    private double xAdd;
    private double yAdd;
    private double zAdd;
    private double extra;
    private @Nullable Object data;
    private int delay;

    @Override
    public @NonNull Map<String, Object> serialize() {
        return new MapBuilder<String, Object>(new LinkedHashMap<>())
                .put("type", type.name())
                .putIf("count", count, count != 1)
                .putIfNotEmpty(
                        "offset", new MapBuilder<String, Double>(new LinkedHashMap<>())
                                .putIf("x", offsetX, offsetX != 0)
                                .putIf("y", offsetY, offsetY != 0)
                                .putIf("z", offsetZ, offsetZ != 0)
                                .build()
                )
                .putIfNotEmpty(
                        "add", new MapBuilder<String, Double>(new LinkedHashMap<>())
                                .putIf("x", xAdd, xAdd != 0)
                                .putIf("y", yAdd, yAdd != 0)
                                .putIf("z", zAdd, zAdd != 0)
                                .build()
                )
                .putIf("extra", extra, extra != 0)
                .putIfNotNull("data", data)
                .putIf("delay", delay, delay != 0)
                .build();
    }

    public void spawn(@NonNull Location location) {
        World world = Objects.requireNonNull(location.getWorld(), "The world cannot be null");
        spawn(world, location.getX(), location.getY(), location.getZ());
    }

    public void spawn(@NonNull World world, double x, double y, double z) {
        world.spawnParticle(
                type,
                x + xAdd, y + yAdd, z + zAdd,
                count,
                offsetX, offsetY, offsetZ,
                extra,
                data instanceof ConfigWrapper<?> wrapper ? wrapper.unwrap() : data
        );
    }

    public void spawn(@NonNull Player player) {
        Location location = player.getLocation();
        player.spawnParticle(
                type,
                location.getX() + xAdd, location.getY() + yAdd, location.getZ() + zAdd,
                count,
                offsetX, offsetY, offsetZ,
                extra,
                data instanceof ConfigWrapper<?> wrapper ? wrapper.unwrap() : data
        );
    }

    /**
     * <p>
     * Spawns the particle exactly on the given {@link Location}, without using the {@link #getXAdd()}, or {@link #getYAdd()}, or {@link #getZAdd()}.
     * </p>
     *
     * @param location where to spawn the particle
     */
    public void spawnExact(@NotNull Location location) {
        location.getWorld().spawnParticle(
                type,
                location.getX(), location.getY(), location.getZ(),
                count,
                offsetX, offsetY, offsetZ,
                extra,
                data instanceof ConfigWrapper<?> wrapper ? wrapper.unwrap() : data
        );
    }

    /**
     * <p>
     * Spawns the particle exactly where the given {@link Player} is without using the {@link #getXAdd()}, or {@link #getYAdd()}, or {@link #getZAdd()}.
     * </p>
     *
     * @param player the {@link Player} to send the particle
     */
    public void spawnExact(@NotNull Player player) {
        Location location = player.getLocation();
        player.spawnParticle(
                type,
                location.getX(), location.getY(), location.getZ(),
                count,
                offsetX, offsetY, offsetZ,
                extra,
                data instanceof ConfigWrapper<?> wrapper ? wrapper.unwrap() : data
        );
    }

    /**
     * <p>
     * Spawns the particle on the given {@link Location} only for the given {@link Player}
     * without using the {@link #getXAdd()}, or {@link #getYAdd()}, or {@link #getZAdd()}.
     * </p>
     *
     * @param player the {@link Player} to send the particle
     * @param location where to spawn the particle
     */
    public void spawnExact(@NotNull Player player, @NotNull Location location) {
        player.spawnParticle(
                type,
                location.getX(), location.getY(), location.getZ(),
                count,
                offsetX, offsetY, offsetZ,
                extra,
                data instanceof ConfigWrapper<?> wrapper ? wrapper.unwrap() : data
        );
    }

    /**
     * <p>
     * Spawns the particle on the given {@link Location}, but it rotates the {@link #getXAdd()}, {@link #getYAdd()} and {@link #getZAdd()} using the {@link Location#getDirection()}.
     * </p>
     *
     * @param location where to spawn the particle
     */
    public void spawnRotating(@NotNull Location location) {
        Location spawnLocation = location.getDirection()
                .multiply(new Vector(xAdd, yAdd, zAdd))
                .toLocation(location.getWorld());
        spawnExact(spawnLocation);
    }

    /**
     * <p>
     * Spawns the particle where the given {@link Player} is,
     * but it rotates the {@link #getXAdd()}, {@link #getYAdd()} and {@link #getZAdd()} using the {@link Location#getDirection()}.
     * </p>
     *
     * @param player the {@link Player} to send the particle
     */
    public void spawnRotating(@NotNull Player player) {
        Location location = player.getLocation();
        Location spawnLocation = location.getDirection()
                .multiply(new Vector(xAdd, yAdd, zAdd))
                .toLocation(location.getWorld());
        spawnExact(player, spawnLocation);
    }

    public static @NonNull ParticleSpawner deserialize(@NonNull Map<@NonNull String, @Nullable Object> map) {
        Map<String, Object> offset = ConfigUtil.get("offset", map, Collections.emptyMap());
        Map<String, Object> add = ConfigUtil.get("add", map, Collections.emptyMap());
        return new ParticleSpawner(
                Particle.valueOf(ConfigUtil.<String>get("type", map).toUpperCase()),
                ConfigUtil.get("count", map, 1),
                ConfigUtil.<Number>get("x", offset, 0.0).doubleValue(),
                ConfigUtil.<Number>get("y", offset, 0.0).doubleValue(),
                ConfigUtil.<Number>get("z", offset, 0.0).doubleValue(),
                ConfigUtil.<Number>get("x", add, 0.0).doubleValue(),
                ConfigUtil.<Number>get("y", add, 0.0).doubleValue(),
                ConfigUtil.<Number>get("z", add, 0.0).doubleValue(),
                ConfigUtil.get("extra", map, 0.0),
                ConfigUtil.get("data", map, (Object) null),
                ConfigUtil.get("delay", map, 0)
        );
    }
}
