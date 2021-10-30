package com.wizardlybump17.wlib.item;

public enum FilterType {

    /**
     * Checks the material type. Accepts regex
     */
    MATERIAL,
    /**
     * Checks the item display name. Accepts regex
     */
    DISPLAY_NAME,
    /**
     * Checks the lore. Don't accept regex yet
     */
    LORE,
    /**
     * Checks the enchantments. Don't accept regex
     */
    ENCHANTMENTS,
    /**
     * Checks the item flags. Don't accept regex
     */
    FLAGS,
    /**
     * Checks the nbt tags. Don't accept regex
     */
    NBT_TAGS,
    /**
     * Checks if the item is or not glowing. You want regex for a boolean?
     */
    GLOW,
    /**
     * Checks if the item is or not unbreakable. *true* :sunglasses:
     */
    UNBREAKABLE
}
