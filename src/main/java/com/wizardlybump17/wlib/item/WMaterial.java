package com.wizardlybump17.wlib.item;

import lombok.Data;
import org.bukkit.Material;

public enum WMaterial {

    AIR,
    
    SKULL_ITEM(
            new MaterialData(0, "SKELETON_SKULL"),
            new MaterialData(1, "WITHER_SKELETON_SKULL"),
            new MaterialData(2, "ZOMBIE_HEAD"),
            new MaterialData(3, "PLAYER_HEAD"),
            new MaterialData(4, "CREEPER_HEAD"),
            new MaterialData(5, "DRAGON_HEAD")),

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
                        return new WMaterialData(Material.valueOf(materialData.result), data);
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
    public static class WMaterialData {
        final Material type;
        final byte data;
    }
}
