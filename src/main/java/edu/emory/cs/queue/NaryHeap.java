package edu.emory.cs.queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NaryHeap<T extends Comparable<T>> extends AbstractPriorityQueue<T> {

    private final int N;
    private List<T> keys;

    public NaryHeap() {
        this(3, Comparator.naturalOrder());
    }


    public NaryHeap(int n, Comparator<T> comparator) {
        super(comparator);
        keys = new ArrayList<>();
        N = n;
    }

    public int size() {
        return keys.size();
    }

    private int getRootIndex() {
        return 0;
    }

    private int getLastIndex() {
        return size() - 1;
    }

    private int getParentIndex(int k) {
        return (k - 1) / N;
    }

    private int getLeftMostIndex(int k) {
        return k * N + 1;
    }

    private int getMaxIndex(int leftMostIndex) {
        int rightMostIndex = Math.min(size(), leftMostIndex + N); // exclusive
        int max = leftMostIndex;

        for (int i = leftMostIndex + 1; i < rightMostIndex; i++) {
            if (comparator.compare(keys.get(max), keys.get(i)) < 0) {
                max = i;
            }
        }
        return max;
    }

    public void add(T key) {
        keys.add(key);
        swim(getLastIndex());
    }

    public void swim(int k) {
        while (k > getRootIndex() && comparator.compare(keys.get(getParentIndex(k)), keys.get(k)) < 0) {
            Collections.swap(keys, getParentIndex(k), k);
            k = getParentIndex(k);
        }
    }

    @Override
    public T remove() {
        if (isEmpty()) return null;
        Collections.swap(keys, getRootIndex(), getLastIndex());
        T max = keys.remove(getLastIndex());
        sink(getRootIndex());
        return max;
    }

    public void sink(int k) {
        int i, max;

        while ((i = getLeftMostIndex(k)) < this.size()) {
            max = getMaxIndex(i);
            if (comparator.compare(keys.get(k), keys.get(max)) >= 0) break;
            Collections.swap(keys, k, max);
            k = max;
        }
    }
}