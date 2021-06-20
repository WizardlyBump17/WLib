package com.wizardlybump17.wlib.item;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;

import java.util.Map;

public enum WMaterial {

    AIR,
    
    SKULL_ITEM(
            new MaterialData(0, "SKELETON_SKULL"),
            new MaterialData(1, "WITHER_SKELETON_SKULL"),
            new MaterialData(2, "ZOMBIE_HEAD"),
            new MaterialData(3, "PLAYER_HEAD"),
            new MaterialData(4, "CREEPER_HEAD"),
            new MaterialData(5, "DRAGON_HEAD")),
    SKELETON_SKULL(new MaterialData(0, "SKULL_ITEM")) {
        @Override
        public WMaterialData of(byte data) {
            try {
                return new WMaterialData(Material.valueOf(name()), data);
            } catch (IllegalArgumentException e) {
                return new WMaterialData(Material.valueOf("SKULL_ITEM"), (byte) 0);
            }
        }
    },
    WITHER_SKELETON_SKULL(new MaterialData(0, "SKULL_ITEM")) {
        @Override
        public WMaterialData of(byte data) {
            try {
                return new WMaterialData(Material.valueOf(name()), data);
            } catch (IllegalArgumentException e) {
                return new WMaterialData(Material.valueOf("SKULL_ITEM"), (byte) 1);
            }
        }
    },
    ZOMBIE_HEAD(new MaterialData(0, "SKULL_ITEM")) {
        @Override
        public WMaterialData of(byte data) {
            try {
                return new WMaterialData(Material.valueOf(name()), data);
            } catch (IllegalArgumentException e) {
                return new WMaterialData(Material.valueOf("SKULL_ITEM"), (byte) 2);
            }
        }
    },
    PLAYER_HEAD(new MaterialData(0, "SKULL_ITEM")) {
        @Override
        public WMaterialData of(byte data) {
            try {
                return new WMaterialData(Material.valueOf(name()), data);
            } catch (IllegalArgumentException e) {
                return new WMaterialData(Material.valueOf("SKULL_ITEM"), (byte) 3);
            }
        }
    },
    CREEPER_HEAD(new MaterialData(0, "SKULL_ITEM")) {
        @Override
        public WMaterialData of(byte data) {
            try {
                return new WMaterialData(Material.valueOf(name()), data);
            } catch (IllegalArgumentException e) {
                return new WMaterialData(Material.valueOf("SKULL_ITEM"), (byte) 4);
            }
        }
    },
    DRAGON_HEAD(new MaterialData(0, "SKULL_ITEM")) {
        @Override
        public WMaterialData of(byte data) {
            try {
                return new WMaterialData(Material.valueOf(name()), data);
            } catch (IllegalArgumentException e) {
                return new WMaterialData(Material.valueOf("SKULL_ITEM"), (byte) 3, ImmutableMap.of("head-base64", DRAGON_HEAD_BASE64));
            }
        }
    },

    STAINED_GLASS_PANE(
            new MaterialData(0, "WHITE_STAINED_GLASS_PANE"),
            new MaterialData(1, "ORANGE_STAINED_GLASS_PANE"),
            new MaterialData(2, "MAGENTA_STAINED_GLASS_PANE"),
            new MaterialData(3, "LIGHT_BLUE_STAINED_GLASS_PANE"),
            new MaterialData(4, "YELLOW_STAINED_GLASS_PANE"),
            new MaterialData(5, "LIME_STAINED_GLASS_PANE"),
            new MaterialData(6, "PINK_STAINED_GLASS_PANE"),
            new MaterialData(7, "GRAY_STAINED_GLASS_PANE"),
            new MaterialData(8, "LIGHT_GRAY_STAINED_GLASS_PANE"),
            new MaterialData(9, "CYAN_STAINED_GLASS_PANE"),
            new MaterialData(10, "PURPLE_STAINED_GLASS_PANE"),
            new MaterialData(11, "BLUE_STAINED_GLASS_PANE"),
            new MaterialData(12, "BROWN_STAINED_GLASS_PANE"),
            new MaterialData(13, "GREEN_STAINED_GLASS_PANE"),
            new MaterialData(14, "RED_STAINED_GLASS_PANE"),
            new MaterialData(15, "BLACK_STAINED_GLASS_PANE")),

    MAP,
    EMPTY_MAP(new MaterialData(0, "MAP")) {
        @Override
        public WMaterialData of(byte data) {
            try {
                return new WMaterialData(Material.valueOf(name()), data);
            } catch (IllegalArgumentException e) {
                return new WMaterialData(Material.valueOf("MAP"), data);
            }
        }
    },
    FILLED_MAP(new MaterialData(0, "MAP")) {
        @Override
        public WMaterialData of(byte data) {
            try {
                return new WMaterialData(Material.valueOf(name()), data);
            } catch (IllegalArgumentException e) {
                return new WMaterialData(Material.valueOf("MAP"), data);
            }
        }
    };

    private static final String DRAGON_HEAD_BASE64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTFjZDZkMmQwM2YxMzVlN2M2YjVkNmNkYWUxYjNhNjg3NDNkYjRlYjc0OWZhZjczNDFlOWZiMzQ3YWEyODNiIn19fQ==";

    private final MaterialData[] related;

    WMaterial(MaterialData... related) {
        this.related = related;
    }

    public WMaterialData of(byte data) {
        try {
            return new WMaterialData(Material.valueOf(name()), data);
        } catch (IllegalArgumentException e) {
            for (MaterialData materialData : related)
                if (materialData.data == data) {
                    try {
                        return new WMaterialData(Material.valueOf(materialData.result), (byte) 0);
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            System.err.println("tried to get illegal material: " + name() + ':' + data);
            return new WMaterialData(Material.AIR, (byte) 0);
        }
    }

    @Data
    private static class MaterialData {
        private final int data;
        private final String result;
    }

    @Data
    @AllArgsConstructor
    public static class WMaterialData {
        final Material type;
        byte data;
        Map<String, Object> args;

        public WMaterialData(Material type, byte data) {
            this(type, data, null);
        }
    }
}
