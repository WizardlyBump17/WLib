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
     * Checks the lore. Accepts regex
     */
    LORE,
    /**
     * Checks the enchantments. Accepts regex
     */
    ENCHANTMENTS,
    /**
     * Checks the item flags. Accepts regex
     */
    FLAGS,
    /**
     * Checks if the item is or not glowing
     */
    GLOW,
    /**
     * Checks if the item is or not unbreakable
     */
    UNBREAKABLE,
    /**
     * Checks if the item amount is equal to the given amount. Accepts regex
     */
    AMOUNT
}
