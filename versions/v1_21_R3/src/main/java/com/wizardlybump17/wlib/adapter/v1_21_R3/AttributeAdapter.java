package com.wizardlybump17.wlib.adapter.v1_21_R3;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AttributeAdapter extends com.wizardlybump17.wlib.adapter.AttributeAdapter {

    public static final @NotNull Map<String, Attribute> ATTRIBUTES;

    static {
        Map<String, Attribute> attributes = new HashMap<>();
        attributes.put("GENERIC_MAX_HEALTH", Attribute.MAX_HEALTH);
        attributes.put("GENERIC_FOLLOW_RANGE", Attribute.FOLLOW_RANGE);
        attributes.put("GENERIC_KNOCKBACK_RESISTANCE", Attribute.KNOCKBACK_RESISTANCE);
        attributes.put("GENERIC_MOVEMENT_SPEED", Attribute.MOVEMENT_SPEED);
        attributes.put("GENERIC_FLYING_SPEED", Attribute.FLYING_SPEED);
        attributes.put("GENERIC_ATTACK_DAMAGE", Attribute.ATTACK_DAMAGE);
        attributes.put("GENERIC_ATTACK_KNOCKBACK", Attribute.ATTACK_KNOCKBACK);
        attributes.put("GENERIC_ATTACK_SPEED", Attribute.ATTACK_SPEED);
        attributes.put("GENERIC_ARMOR", Attribute.ARMOR);
        attributes.put("GENERIC_ARMOR_TOUGHNESS", Attribute.ARMOR_TOUGHNESS);
        attributes.put("GENERIC_FALL_DAMAGE_MULTIPLIER", Attribute.FALL_DAMAGE_MULTIPLIER);
        attributes.put("GENERIC_LUCK", Attribute.LUCK);
        attributes.put("GENERIC_MAX_ABSORPTION", Attribute.MAX_ABSORPTION);
        attributes.put("GENERIC_SAFE_FALL_DISTANCE", Attribute.SAFE_FALL_DISTANCE);
        attributes.put("GENERIC_SCALE", Attribute.SCALE);
        attributes.put("GENERIC_STEP_HEIGHT", Attribute.STEP_HEIGHT);
        attributes.put("GENERIC_GRAVITY", Attribute.GRAVITY);
        attributes.put("GENERIC_JUMP_STRENGTH", Attribute.JUMP_STRENGTH);
        attributes.put("PLAYER_BLOCK_INTERACTION_RANGE", Attribute.BLOCK_INTERACTION_RANGE);
        attributes.put("PLAYER_ENTITY_INTERACTION_RANGE", Attribute.ENTITY_INTERACTION_RANGE);
        attributes.put("PLAYER_BLOCK_BREAK_SPEED", Attribute.BLOCK_BREAK_SPEED);
        attributes.put("ZOMBIE_SPAWN_REINFORCEMENTS", Attribute.SPAWN_REINFORCEMENTS);
        ATTRIBUTES = Collections.unmodifiableMap(attributes);
    }

    @Override
    public Attribute getAttribute(@NotNull String name) {
        Attribute converted = ATTRIBUTES.get(name.toUpperCase());
        if (converted != null)
            return converted;
        return Registry.ATTRIBUTE.get(NamespacedKey.fromString(name.toLowerCase()));
    }

    @Override
    public @NotNull Map<String, Object> serialize(@NotNull Multimap<Attribute, AttributeModifier> attributes) {
        Map<String, Object> result = new TreeMap<>();
        for (Attribute attribute : attributes.keySet())
            result.put(attribute.getKey().toString(), List.copyOf(attributes.get(attribute)));
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> deserialize(@NotNull Map<String, ? extends Object> serialized) {
        Multimap<Attribute, AttributeModifier> result = TreeMultimap.create(
                Comparator.comparing(attribute -> attribute.getKey().toString()),
                Comparator.comparing(AttributeModifier::getKey)
                        .thenComparing(AttributeModifier::getAmount)
                        .thenComparing(AttributeModifier::getOperation)
        );
        serialized.forEach((attribute, modifiers) -> {
            if (modifiers != null)
                for (AttributeModifier modifier : ((Collection<AttributeModifier>) modifiers))
                    result.put(getAttribute(attribute), modifier);
        });
        return result;
    }
}
