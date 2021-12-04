package com.wizardlybump17.wlib.object;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class EnumSet<E extends Enum<E>> implements Set<E> {

    private static final Object PRESENT = new Object();

    private final Map<E, Object> map;

    public EnumSet(Class<E> clazz) {
        map = new EnumMap<>(clazz);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return map.keySet().toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return map.keySet().toArray(a);
    }

    @Override
    public boolean add(E e) {
        return map.put(e, PRESENT) == null;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o) != null;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return map.keySet().containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c)
            changed |= add(e);
        return changed;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        boolean changed = false;
        for (E e : map.keySet())
            if (!c.contains(e))
                changed |= remove(e);
        return changed;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        boolean changed = false;
        for (Object o : c)
            changed |= remove(o);
        return changed;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (E e : map.keySet()) {
            sb.append(e);
            sb.append(", ");
        }
        if (sb.length() > 1)
            sb.setLength(sb.length() - 2);
        sb.append(']');
        return sb.toString();
    }
}
