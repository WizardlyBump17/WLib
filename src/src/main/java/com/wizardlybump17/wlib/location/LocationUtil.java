package com.wizardlybump17.wlib.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.util.Vector;

@AllArgsConstructor
@Getter
public class LocationUtil {

    private final Location location;

    public String toString(boolean yawAndPitch) {
        return
                "world: " + location.getWorld().getName()
                        + ", x: " + location.getX()
                        + ", y: " + location.getY()
                        + ", z: " + location.getZ()
                        + (yawAndPitch ? ", yaw: " + location.getYaw()
                        + ", pitch: " + location.getPitch() : "");
    }

    public boolean isBetween(Location loc1, Location loc2) {
        return location.toVector().isInAABB(getMin(loc1, loc2).toVector(), getMax(loc1, loc2).toVector());
    }

    public static Location getMin(Location loc1, Location loc2) {
        return new Location(
                loc1.getWorld(),
                Math.min(loc1.getX(), loc2.getX()),
                Math.min(loc1.getY(), loc2.getY()),
                Math.min(loc1.getZ(), loc2.getZ()));
    }

    public static Location getMax(Location loc1, Location loc2) {
        return new Location(
                loc1.getWorld(),
                Math.max(loc1.getX(), loc2.getX()),
                Math.max(loc1.getY(), loc2.getY()),
                Math.max(loc1.getZ(), loc2.getZ()));
    }
}
