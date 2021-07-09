package com.wizardlybump17.wlib.adapter;

import com.wizardlybump17.wlib.util.ArrayUtils;
import com.wizardlybump17.wlib.util.MapUtils;
import lombok.Getter;
import org.bukkit.Material;

import java.util.Map;

@Getter
public enum WMaterial {

    AIR,

    SKULL_ITEM(ArrayUtils.arrayOfRange(0, 5)),
    SKELETON_SKULL("SKULL_ITEM"),
    WITHER_SKELETON_SKULL(1, "SKULL_ITEM"),
    ZOMBIE_HEAD(2, "SKULL_ITEM"),
    PLAYER_HEAD(3, "SKULL_ITEM"),
    CREEPER_HEAD(4, "SKULL_ITEM"),
    DRAGON_HEAD(5, "SKULL_ITEM"),

    STAINED_GLASS_PANE(ArrayUtils.arrayOfRange(0, 15)),
    WHITE_STAINED_GLASS_PANE("STAINED_GLASS_PANE"),
    ORANGE_STAINED_GLASS_PANE(1, "STAINED_GLASS_PANE"),
    MAGENTA_STAINED_GLASS_PANE(2, "STAINED_GLASS_PANE"),
    LIGHT_BLUE_STAINED_GLASS_PANE(3, "STAINED_GLASS_PANE"),
    YELLOW_STAINED_GLASS_PANE(4, "STAINED_GLASS_PANE"),
    LIME_STAINED_GLASS_PANE(5, "STAINED_GLASS_PANE"),
    PINK_STAINED_GLASS_PANE(6, "STAINED_GLASS_PANE"),
    GRAY_STAINED_GLASS_PANE(7, "STAINED_GLASS_PANE"),
    LIGHT_GRAY_STAINED_GLASS_PANE(8, "STAINED_GLASS_PANE"),
    CYAN_STAINED_GLASS_PANE(9, "STAINED_GLASS_PANE"),
    PURPLE_STAINED_GLASS_PANE(10, "STAINED_GLASS_PANE"),
    BLUE_STAINED_GLASS_PANE(11, "STAINED_GLASS_PANE"),
    BROWN_STAINED_GLASS_PANE(12, "STAINED_GLASS_PANE"),
    GREEN_STAINED_GLASS_PANE(13, "STAINED_GLASS_PANE"),
    RED_STAINED_GLASS_PANE(14, "STAINED_GLASS_PANE"),
    BLACK_STAINED_GLASS_PANE(15, "STAINED_GLASS_PANE"),

    MAP,
    EMPTY_MAP("MAP"),
    FILLED_MAP("MAP"),

    MONSTER_EGG(ArrayUtils.arrayOfRange(0, 120)),
    BAT_SPAWN_EGG(65, MapUtils.mapOf("spawn-egg", "BAT"), "MONSTER_EGG"),
    BEE_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "BEE"), "MONSTER_EGG"),
    BLAZE_SPAWN_EGG(61, MapUtils.mapOf("spawn-egg", "BLAZE"), "MONSTER_EGG"),
    CAT_SPAWN_EGG(98, MapUtils.mapOf("spawn-egg", "CAT"), "MONSTER_EGG"),
    CAVE_SPIDER_SPAWN_EGG(59, MapUtils.mapOf("spawn-egg", "CAVE_SPIDER"), "MONSTER_EGG"),
    CHICKEN_SPAWN_EGG(93, MapUtils.mapOf("spawn-egg", "CHICKEN"), "MONSTER_EGG"),
    COD_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "COD"), "MONSTER_EGG"),
    COW_SPAWN_EGG(92, "MONSTER_EGG"),
    CREEPER_SPAWN_EGG(50, "MONSTER_EGG"),
    DOLPHIN_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "DOLPHIN"), "MONSTER_EGG"),
    DONKEY_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "DONKEY"), "MONSTER_EGG"),
    DROWNED_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "DROWNED"), "MONSTER_EGG"),
    ELDER_GUARDIAN_SPAWN_EGG(68, MapUtils.mapOf("spawn-egg", "ELDER-GUARDIAN"), "MONSTER_EGG"),
    ENDERMAN_SPAWN_EGG(58, "MONSTER_EGG"),
    ENDERMITE_SPAWN_EGG(67, "MONSTER_EGG"),
    EVOKER_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "EVOKER"), "MONSTER_EGG"),
    FOX_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "FOX"), "MONSTER_EGG"),
    GHAST_SPAWN_EGG(56, "MONSTER_EGG"),
    GUARDIAN_SPAWN_EGG(68, "MONSTER_EGG"),
    HOGLIN_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "HOGLIN"), "MONSTER_EGG"),
    HORSE_SPAWN_EGG(100, "MONSTER_EGG"),
    HUSK_SPAWN_EGG(54, MapUtils.mapOf("spawn-egg", "HUSK"), "MONSTER_EGG"),
    LLAMA_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "LLAMA"), "MONSTER_EGG"),
    MAGMA_CUBE_SPAWN_EGG(62, "MONSTER_EGG"),
    MOOSHROOM_SPAWN_EGG(96, "MONSTER_EGG"),
    MULE_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "MULE"), "MONSTER_EGG"),
    OCELOT_SPAWN_EGG(98, "MONSTER_EGG"),
    PANDA_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "PANDA"), "MONSTER_EGG"),
    PARROT_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "PARROT"), "MONSTER_EGG"),
    PHANTOM_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "PHANTOM"), "MONSTER_EGG"),
    PIG_SPAWN_EGG(90, "MONSTER_EGG"),
    PIGLIN_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "PIGLIN"), "MONSTER_EGG"),
    PIGLIN_BRUTE_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "PIGLIN_BRUTE"), "MONSTER_EGG"),
    PILLAGER_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "PILLAGER"), "MONSTER_EGG"),
    POLAR_BEAR_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "POLAR_BEAR"), "MONSTER_EGG"),
    PUFFERFISH_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "PUFFERFISH"), "MONSTER_EGG"),
    RABBIT_SPAWN_EGG(101, "MONSTER_EGG"),
    RAVAGER_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "RAVAGER"), "MONSTER_EGG"),
    SALMON_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "SALMON"), "MONSTER_EGG"),
    SHEEP_SPAWN_EGG(91, "MONSTER_EGG"),
    SHULKER_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "SHULKER"), "MONSTER_EGG"),
    SILVERFISH_SPAWN_EGG(60, "MONSTER_EGG"),
    SKELETON_SPAWN_EGG(51, "MONSTER_EGG"),
    SKELETON_HORSE_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "SKELETON_HORSE"), "MONSTER_EGG"),
    SLIME_SPAWN_EGG(55, "MONSTER_EGG"),
    SPIDER_SPAWN_EGG(52, "MONSTER_EGG"),
    SQUID_SPAWN_EGG(94, "MONSTER_EGG"),
    STRAY_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "STRAY"), "MONSTER_EGG"),
    STRIDER_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "STRIDER"), "MONSTER_EGG"),
    TRADER_LLAMA_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "TRADER"), "MONSTER_EGG"),
    TROPICAL_FISH_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "TROPICAL"), "MONSTER_EGG"),
    TURTLE_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "TURTLE"), "MONSTER_EGG"),
    VEX_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "VEX"), "MONSTER_EGG"),
    VILLAGER_SPAWN_EGG(120, "MONSTER_EGG"),
    VINDICATOR_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "VINDICATOR"), "MONSTER_EGG"),
    WANDERING_TRADER_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "WANDERING_TRADER"), "MONSTER_EGG"),
    WITCH_SPAWN_EGG(66, "MONSTER_EGG"),
    WITHER_SKELETON_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "WITHER_SKELETON"), "MONSTER_EGG"),
    WOLF_SPAWN_EGG(95, "MONSTER_EGG"),
    ZOGLIN_SPAWN_EGG(MapUtils.mapOf("spawn-egg", "ZOGLIN"), "MONSTER_EGG"),
    ZOMBIE_SPAWN_EGG(54, "MONSTER_EGG"),
    ZOMBIE_HORSE_SPAWN_EGG(100, MapUtils.mapOf("spawn-egg", "ZOMBIE_HORSE"), "MONSTER_EGG"),
    ZOMBIE_VILLAGER_SPAWN_EGG(54, MapUtils.mapOf("spawn-egg", "ZOMBIE_VILLAGER"), "MONSTER_EGG"),
    ZOMBIFIED_PIGLIN_SPAWN_EGG(57, "MONSTER_EGG"),

    CLAY_BRICK("BRICK");

    private final Material material;
    private final int data;
    private final String[] related;
    private final int[] acceptedData;
    private final Map<String, Object> itemData;

    WMaterial(int data, String... related) {
        this.data = data;
        this.related = related;
        this.acceptedData = new int[0];
        material = getBukkitMaterial();
        itemData = null;
    }

    WMaterial(String... related) {
        this(-1, related);
    }

    WMaterial(int data, Map<String, Object> itemData, String... related) {
        this.data = data;
        this.related = related;
        acceptedData = new int[0];
        material = getBukkitMaterial();
        this.itemData = itemData;
    }

    WMaterial(Map<String, Object> itemData, String... related) {
        this(-1, itemData, related);
    }

    WMaterial(int[] acceptedData, String... related) {
        this(acceptedData, null, related);
    }

    WMaterial(int[] acceptedData, Map<String, Object> itemData, String... related) {
        this.acceptedData = acceptedData;
        this.data = -1;
        this.related = related;
        material = getBukkitMaterial();
        this.itemData = itemData;
    }

    private Material getBukkitMaterial() {
        try {
            return Material.valueOf(name());
        } catch (IllegalArgumentException e) {
            return fromRelated();
        }
    }

    private Material fromRelated() {
        for (String s : related) {
            try {
                return Material.valueOf(s);
            } catch (IllegalArgumentException ignored) {}
        }
        return Material.AIR;
    }
}
