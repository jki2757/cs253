package edu.emory.cs.queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EagerPriorityQueue<T extends Comparable<T>> extends AbstractPriorityQueue<T> {
    private List<T> keys;

    public EagerPriorityQueue(Comparator<T> comparator) {
        super(comparator);
        keys = new ArrayList<>();
    }

    public EagerPriorityQueue() {
        this(Comparator.naturalOrder());
    }

    @Override
    public void add(T key) {
        // binary search (if not found, index < 0)
        int index = Collections.binarySearch(keys, key, comparator);
        // if not found, the appropriate index is {@code -(index +1)}
        if (index < 0) index = -(index + 1);
        keys.add(index, key);
    }

    /**
     * Remove the last key in the list.
     * @return the key with the highest priority if exists; otherwise, {@code null}.
     */
    @Override
    public T remove() {
        return isEmpty() ? null : keys.remove(keys.size() - 1);
    }

    @Override
    public int size() {
        return keys.size();
    }
}

