package com.wizardlybump17.wlib.util;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class CollectionUtil<E> {

    private static final String[] EMPTY_STRING_ARRAY = new String[]{};

    private final Collection<E> collection;

    public CollectionUtil<String> replace(String old, String replacement) {
        List<String> result = new ArrayList<>(collection.size());
        for (E e : collection)
            result.add(e.toString().replace(old, replacement));
        return new CollectionUtil<>(result);
    }

    public CollectionUtil<String> replace(char old, char replacement) {
        List<String> result = new ArrayList<>(collection.size());
        for (E e : collection)
            result.add(e.toString().replace(old, replacement));
        return new CollectionUtil<>(result);
    }

    @SuppressWarnings("unchecked")
    public <C extends Collection<E>> C getCollection() {
        return (C) collection;
    }

    public E getIf(Predicate<E> predicate) {
        for (E e : collection)
            if (predicate.test(e))
                return e;
        return null;
    }

    /**
     * Will breaks the string into a list. The list will have strings with the specified length
     * @param string the string to be broke
     * @param length the length that the parts will have
     * @param prefix the prefix
     * @return the list
     */
    public static List<String> breakLines(String string, int length, String prefix) {
        if (prefix == null)
            prefix = "";
        List<String> result = new ArrayList<>();
        int current = 0;
        while (current < string.length())
            result.add(prefix + string.substring(current, current + length > string.length() ? (current = string.length()) : (current += length)));
        return result;
    }

    /**
     * Will breaks the string into a list. The list will have strings with the specified length.
     * If spaces is true, then it will split the string by spaces and will append each part to the final list.
     * If the current split part is bigger or equals the length it will break to the next list index
     * @param string the string to be broke
     * @param length the length that the parts will have
     * @param prefix the prefix
     * @param spaces if it needs to split the string by the spaces
     * @return the list
     */
    public static List<String> breakLines(String string, int length, String prefix, boolean spaces) {
        if (spaces) {
            List<String> result = new ArrayList<>();

            final String[] split = string.split(" ");

            List<String> tempList = new ArrayList<>(split.length + 1);
            for (String s : split) {
                tempList.add(s);

                final String join = String.join(" ", tempList.toArray(EMPTY_STRING_ARRAY));
                if (join.length() >= length) {
                    result.add(prefix + join);
                    tempList.clear();
                }
            }

            return result;
        }

        return breakLines(string, length, prefix);
    }

    public static List<String> breakLines(String string, int length, String prefix, boolean spaces, String... firstPrefixes) {
        if (!spaces)
            return breakLines(string, length, prefix, firstPrefixes);

        final List<String> strings = breakLines(string, length, prefix, true);

        for (int i = 0; i < firstPrefixes.length && i < strings.size(); i++)
            strings.set(i, firstPrefixes[i] + strings.get(i).substring(prefix.length()));

        return strings;
    }

    public static List<String> breakLines(String string, int length, String prefix, String... firstPrefixes) {
        List<String> result = new ArrayList<>(string.length() / length + 1);
        int current = 0;
        while (current < string.length())
            result.add(prefix + string.substring(current, current + length > string.length() ? (current = string.length()) : (current += length)));

        for (int i = 0; i < firstPrefixes.length && i < result.size(); i++)
            result.set(i, firstPrefixes[i] + result.get(i).substring(prefix.length()));

        return result;
    }

    public static <T> boolean equals(Collection<T> collection1, Collection<T> collection2) {
        if (collection1 == collection2)
            return true;
        if (collection1 == null || collection2 == null)
            return false;
        if (collection1.size() != collection2.size())
            return false;
        return collection1.containsAll(collection2);
    }

    /**
     * Joins all collections into one {@link List}
     * @param collections the collections
     * @param <T> the type of the collection
     * @return the joined {@link List}
     */
    @SafeVarargs
    public static <T> List<T> join(Collection<T>... collections) {
        int size = 0;
        for (Collection<T> collection : collections)
            size += collection.size();

        List<T> list = new ArrayList<>(size);
        for (Collection<T> collection : collections)
            list.addAll(collection);

        return list;
    }

    /**
     * Joins all collections into one {@link List}
     * @param collections the collections
     * @param <T> the type of the collection
     * @return the joined {@link List}
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> join(Collection<Collection<T>> collections) {
        return join(collections.toArray(new Collection[0]));
    }

    /**
     * Creates a new {@link Collection} with the given elements
     * @param supplier the collection supplier
     * @param elements the elements
     * @return a new {@link Collection} with the given elements
     * @param <E> the element type
     * @param <T> the collection type
     */
    public static <E, T extends Collection<E>> T listOf(Supplier<T> supplier, E... elements) {
        T collection = supplier.get();
        collection.addAll(Arrays.asList(elements));
        return collection;
    }

    /**
     * Creates a new {@link ArrayList} with the given elements.<br>
     * It calls {@link #listOf(Supplier, Object[])} with a {@link ArrayList} supplier
     * @param elements the elements
     * @return a new {@link ArrayList} with the given elements
     * @param <E> the element type
     */
    @SafeVarargs
    public static <E> List<E> listOf(E... elements) {
        return listOf((Supplier<? extends List<E>>) ArrayList::new, elements);
    }

    /**
     * Deep clones the given collection<br>
     * It will clone (if possible) all the elements of the collection using the {@link ObjectUtil#clone(Object)}
     * @param supplier the collection supplier
     * @param original the original collection
     * @return the cloned collection
     * @param <E> the element type
     * @param <T> the collection type
     */
    public static <E, T extends Collection<E>> T deepClone(Supplier<T> supplier, T original) {
        T clone = supplier.get();
        for (E e : original)
            clone.add(ObjectUtil.clone(e));
        return clone;
    }
}
