package com.wizardlybump17.wlib.util.bukkit;

import com.wizardlybump17.wlib.util.MapUtils;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

@UtilityClass
public class PersistentDataContainerUtil {

    public static final @NonNull PersistentDataAdapterContext BASE_CONTEXT = new ItemStack(Material.ARROW).getItemMeta().getPersistentDataContainer().getAdapterContext();
    public static final @NonNull Map<Class<?>, PersistentDataType<?, ?>> DEFAULT_TYPES = Collections.unmodifiableMap(MapUtils.mapOf(
            String.class, PersistentDataType.STRING,
            Byte.class, PersistentDataType.BYTE,
            Short.class, PersistentDataType.SHORT,
            Integer.class, PersistentDataType.INTEGER,
            Long.class, PersistentDataType.LONG,
            Float.class, PersistentDataType.FLOAT,
            Double.class, PersistentDataType.DOUBLE,
            byte[].class, PersistentDataType.BYTE_ARRAY,
            int[].class, PersistentDataType.INTEGER_ARRAY,
            PersistentDataContainer.class, PersistentDataType.TAG_CONTAINER,
            PersistentDataContainer[].class, PersistentDataType.TAG_CONTAINER_ARRAY
    ));

    /**
     * <p>
     *     Iterates the given {@link Map} and creates a new {@link PersistentDataContainer} with its keys and values.<br>
     *     The types of the values are fetched from the default types.<br>
     *     If a value type is not found in the default types, an {@link IllegalArgumentException} will be thrown.
     * </p>
     * <p>
     *     Basically, it calls the {@link #containerOf(Map, Iterable)} method with an empty {@link java.util.List}
     * </p>
     * @param map the {@link Map} to iterate
     * @return a new {@link PersistentDataContainer} with the keys and values of the given {@link Map}
     */
    public static @NonNull PersistentDataContainer containerOf(@NonNull Map<NamespacedKey, Object> map) {
        return containerOf(map, Collections.emptyList());
    }

    /**
     * <p>
     *     Iterates the given {@link Map} and creates a new {@link PersistentDataContainer} with its keys and values.<br>
     *     The types of the values are fetched from the given {@link Iterable} of {@link PersistentDataType}s.<br>
     *     If a value type is not found in the given types, an {@link IllegalArgumentException} will be thrown.
     * </p>
     * @param map the {@link Map} to iterate
     * @param types the {@link Iterable} of {@link PersistentDataType}s to fetch the types of the values
     * @return a new {@link PersistentDataContainer} with the keys and values of the given {@link Map}
     */
    public static @NonNull PersistentDataContainer containerOf(@NonNull Map<NamespacedKey, Object> map, @NonNull Iterable<PersistentDataType<?, ?>> types) {
        PersistentDataContainer container = BASE_CONTEXT.newPersistentDataContainer();
        map.forEach((key, value) -> {
            PersistentDataType<Object, Object> type = getType(value, types);
            if (type == null)
                throw new IllegalArgumentException("The type " + value.getClass().getName() + " is not supported");
            container.set(key, type, value);
        });
        return container;
    }

    /**
     * <p>
     *     Gets the default {@link PersistentDataType} for the given object.<br>
     *     If the object's class is not in the {@link #DEFAULT_TYPES} {@link Map}, {@code null} will be returned.
     * </p>
     * @param object the object to get the default type
     * @return the default {@link PersistentDataType} for the given object
     * @param <T> the primitive type
     * @param <Z> the complex type
     */
    @SuppressWarnings("unchecked")
    public static <T, Z> @Nullable PersistentDataType<T, Z> getDefaultType(@NonNull Object object) {
        return (PersistentDataType<T, Z>) DEFAULT_TYPES.get(object.getClass());
    }

    /**
     * <p>
     *     Gets the {@link PersistentDataType} for the given object from the given {@link Iterable} of {@link PersistentDataType}s.<br>
     *     It first tries to get the default type for the given object, if it's not {@code null}, it will be returned.<br>
     *     If the object's class is not in the given types, {@code null} will be returned.
     * </p>
     * @param object the object to get the type
     * @param types the {@link Iterable} of {@link PersistentDataType}s to get the type
     * @return the {@link PersistentDataType} for the given object
     * @param <T> the primitive type
     * @param <Z> the complex type
     */
    @SuppressWarnings("unchecked")
    public static <T, Z> @Nullable PersistentDataType<T, Z> getType(@NonNull Object object, @NonNull Iterable<PersistentDataType<?, ?>> types) {
        PersistentDataType<Object, Object> defaultType = getDefaultType(object);
        if (defaultType != null)
            return (PersistentDataType<T, Z>) defaultType;

        for (PersistentDataType<?, ?> type : types) {
            if (object.getClass() == type.getPrimitiveType())
                return (PersistentDataType<T, Z>) type;
        }
        return null;
    }
}
