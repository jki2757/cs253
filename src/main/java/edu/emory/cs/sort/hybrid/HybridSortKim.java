package edu.emory.cs.sort.hybrid;

import java.lang.reflect.Array;
import java.util.*;

public class HybridSortKim<T extends Comparable<T>> implements HybridSort<T> {
    protected Comparator<T> comparator;
    protected List<Integer> sequence;
    private T[] temp;

    public HybridSortKim() {
        this(Comparator.naturalOrder());
    }

    public HybridSortKim(Comparator<T> comparator) {
        this.comparator = comparator;
        sequence = new ArrayList<>();
        populateSequence(1000);
    }

    public void ShellSortKnuth(T[] array, int beginIndex, int endIndex) {
        populateSequence(endIndex - beginIndex);
        for (int i = getSequenceStartIndex(endIndex - beginIndex); i >= 0; i--)
            InsertionSort(array, beginIndex, endIndex, sequence.get(i));
    }

    protected void populateSequence(int n) {
        n /= 3;
        this.sequence = new ArrayList<>(n);
        for (int t = sequence.size() + 1; ; t++) {
            int h = (int) ((Math.pow(3, t) - 1) / 2);
            if (h <= n) sequence.add(h);
            else break;
        }
    }

    protected int getSequenceStartIndex(int n) {
        int index = Collections.binarySearch(sequence, n / 3);
        if (index < 0) index = -(index + 1);
        if (index == sequence.size()) index--;
        return index;
    }

    public T[] sort(T[][] input) {
        int inc = 0;
        int dec = 0;

        for (int i = 0; i <= input.length - 1; i++) {
            for (int j = 0; j < (input[i].length - 1) / 4; j++) {
                if (input[i][j].compareTo(input[i][j + 1]) < 0) {
                    inc++;
                } else if (input[i][j].compareTo(input[i][j + 1]) > 0) {
                    dec++;
                }
            }

            if (inc / 3 > dec) {
                InsertionSort(input[i], 0, input[i].length, 1);
            } else if (inc < dec / 3) {
                ShellSortKnuth(input[i], 0, input[i].length);
            } else {
                sort(input[i], 0, input[i].length);
            }
            inc = 0;
            dec = 0;
        }

        T[] output = (T[]) Array.newInstance(input[0][0].getClass(), Arrays.stream(input).mapToInt(t -> t.length).sum());

        int beginIndex = 0;
        for (T[] t : input) {
            System.arraycopy(t, 0, output, beginIndex, t.length);
            beginIndex += t.length;
        }

        int length = 0;
        for (int i = 0, endIndex = input[0].length; i < input.length - 1; i++) {
            endIndex += input[i + 1].length;
            length = length + input[i].length;
            merge(output, 0, length, endIndex);
        }
        return output;
    }

    private void merge(T[] array, int beginIndex, int middleIndex, int endIndex) {
        int fst = beginIndex, snd = middleIndex;
        copy(array, beginIndex, endIndex);

        for (int k = beginIndex; k < endIndex; k++) {
            if (fst >= middleIndex)                    // no key left in the 1st half
                array[k] = temp[snd++];
            else if (snd >= endIndex)                  // no key left in the 2nd half
                array[k] = temp[fst++];
            else if (comparator.compare(temp[fst], temp[snd]) < 0)    // 1st key < 2nd key
                array[k] = temp[fst++];
            else
                array[k] = temp[snd++];
        }
    }

    private void copy(T[] array, int beginIndex, int endIndex) {
        int N = array.length;

        if (temp == null || temp.length < N)
            temp = Arrays.copyOf(array, N);
        else {
            N = endIndex - beginIndex;
            System.arraycopy(array, beginIndex, temp, beginIndex, N);
        }
    }

    public void InsertionSort(T[] array, int beginIndex, int endIndex, int h) {
        int begin_h = beginIndex + h;

        for (int i = begin_h; i < endIndex; i++) {
            for (int j = i; j >= begin_h && comparator.compare(array[j], array[j - h]) < 0; j -= h) {
                T t = array[j];
                array[j] = array[j - h];
                array[j - h] = t;
            }
        }
    }

    public void sort(T[] array, int beginIndex, int endIndex) {
        if (beginIndex < endIndex) {
            // at least one key in the range
            if (endIndex - beginIndex < 10) {
                InsertionSort(array, beginIndex, endIndex, 1);
            } else {
                int pivotIndex = partition(array, beginIndex, endIndex);
                // sort left partition
                sort(array, beginIndex, pivotIndex);
                // sort right partition
                sort(array, pivotIndex + 1, endIndex);
            }
        }
    }


    protected int partition(T[] array, int beginIndex, int endIndex) {
        int fst = beginIndex, snd = endIndex;
        while (true) {
            // Find where endIndex > fst > pivot
            while (++fst < endIndex && comparator.compare(array[beginIndex], array[fst]) >= 0) ;
            // Find where beginIndex < snd < pivot
            while (--snd > beginIndex && comparator.compare(array[beginIndex], array[snd]) <= 0) ;
            // pointers crossed
            if (fst >= snd) break;
            // exchange
            T t = array[fst];
            array[fst] = array[snd];
            array[snd] = t;
        }
        T t = array[beginIndex];
        array[beginIndex] = array[snd];
        array[snd] = t;
        return snd;
    }
}
