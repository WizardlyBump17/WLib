package com.wizardlybump17.wlib.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;

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
}
