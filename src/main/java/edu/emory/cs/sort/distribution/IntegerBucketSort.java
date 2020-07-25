package edu.emory.cs.sort.distribution;

import java.util.function.Function;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class IntegerBucketSort extends BucketSort<Integer> {
    private final int GAP;

    /**
     * @param min the minimum integer (inclusive).
     * @param max the maximum integer (exclusive).
     */
    public IntegerBucketSort(int min, int max) {
        super(max - min);
        GAP = -min;
    }

    @Override
    public void sort(Integer[] array, int beginIndex, int endIndex) {
        sort(array, beginIndex, endIndex, null);
    }

    @Override
    protected int getBucketIndex(Integer key, Function<Integer, Integer> f) {
        return key + GAP;
    }
}