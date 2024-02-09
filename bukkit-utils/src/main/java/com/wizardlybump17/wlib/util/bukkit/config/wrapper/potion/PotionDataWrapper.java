package com.wizardlybump17.wlib.util.bukkit.config.wrapper.potion;

import com.wizardlybump17.wlib.util.bukkit.ConfigUtil;
import com.wizardlybump17.wlib.util.bukkit.config.ConfigWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Data
@AllArgsConstructor
@SerializableAs("WLib:Wrapper:PotionData")
public class PotionDataWrapper implements ConfigWrapper<PotionData> {

    private @NonNull PotionType type;
    private boolean extended;
    private boolean upgraded;

    @Override
    public PotionData unwrap() {
        return new PotionData(type, extended, upgraded);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        return Map.of(
                "type", type.name(),
                "extended", extended,
                "upgraded", upgraded
        );
    }

    public static @NonNull PotionDataWrapper deserialize(@NonNull Map<String, Object> map) {
        return new PotionDataWrapper(
                ConfigUtil.<String, PotionType>map("type", map, string -> PotionType.valueOf(string.toUpperCase())),
                ConfigUtil.get("extended", map, false),
                ConfigUtil.get("upgraded", map, false)
        );
    }

    public static @NonNull PotionDataWrapper fromBukkit(@NonNull PotionData bukkit) {
        return new PotionDataWrapper(
                bukkit.getType(),
                bukkit.isExtended(),
                bukkit.isUpgraded()
        );
    }
}
