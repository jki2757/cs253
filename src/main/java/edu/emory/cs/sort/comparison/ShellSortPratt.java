package edu.emory.cs.sort.comparison;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ShellSortPratt<T extends Comparable<T>> extends ShellSort<T> {
    public ShellSortPratt(){
        this(Comparator.naturalOrder());
    }

    public ShellSortPratt(Comparator<T> comparator){
        this(comparator, 1000);
    }

    public ShellSortPratt(Comparator<T> comparator, int n){
        super(comparator, n);
    }

    @Override
    protected void populateSequence(int n) {
        n /= 3;
        this.sequence = new ArrayList<>(n);

        for (int t = sequence.size() + 1; t < n; t *= 3) {
            for (int h = t; h < n; h *= 2) {
                if (h <= n) {
                    sequence.add(h);
                }
            }
        }
        Collections.sort(sequence);
    }

    @Override
    protected int getSequenceStartIndex(int n) {
        int index = Collections.binarySearch(sequence, n / 3);
        if (index < 0) index = -(index + 1);
        if (index == sequence.size()) index--;
        return index;
    }
}
