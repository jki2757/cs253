package edu.emory.cs.queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LazyPriorityQueue<T extends Comparable<T>> extends AbstractPriorityQueue<T> {
    private List<T> keys;

    public LazyPriorityQueue(Comparator<T> comparator) {
        super(comparator);
        keys = new ArrayList<>();
    }

    public LazyPriorityQueue() {
        this(Comparator.naturalOrder());
    }

    // adds a key to the back of the list
    @Override
    public void add(T key) {
        keys.add(key);
    }

    /*finds the key with the highest priority, and removes it from the list
    @return the key with the highest priority if exists; otherwise, {@code null}.
     */
    @Override
    public T remove() {
        if(isEmpty()) return null;
        T max = Collections.max(keys, comparator);
        keys.remove(max);
        return max;
    }

    @Override
    public int size() {
        return keys.size();
    }
}
