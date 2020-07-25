package edu.emory.cs.queue;

import java.util.Comparator;
import java.util.NoSuchElementException;

public abstract class AbstractPriorityQueue<T extends Comparable<T>> {
    protected Comparator<T> comparator;

    public AbstractPriorityQueue(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    abstract public void add(T key);
//s
    abstract public T remove();

    abstract public int size();

    public boolean isEmpty() {
        return size() == 0;
    }
}
