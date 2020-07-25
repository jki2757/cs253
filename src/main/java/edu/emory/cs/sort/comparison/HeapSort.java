package edu.emory.cs.sort.comparison;

import edu.emory.cs.sort.AbstractSort;

import java.util.Comparator;


public class HeapSort<T extends Comparable<T>> extends AbstractSort<T> {
     public HeapSort(){
         this(Comparator.naturalOrder());
     }

     public HeapSort(Comparator<T> comparator){
         super(comparator);
     }

     @Override
     public void sort(T[] array, int beginIndex, int endIndex){
         //heapify
         for(int k = getParentIndex(beginIndex, endIndex); k >= beginIndex; k--)
            sink(array, k, beginIndex, endIndex);


         //swap
         while(endIndex > beginIndex + 1){
            swap(array, beginIndex, --endIndex);
            sink(array, beginIndex, beginIndex, endIndex);
         }
     }

    //The buildMaxHeap() operation is run once, and is O(n) in performance.
    //The siftDown() function is O(log n), and is called n times.
    //Therefore, the performance of this algorithm is O(n + n log n) = O(n log n).

     public void sink(T[] array, int k, int beginIndex, int endIndex){
        for(int i = getLeftChildIndex(beginIndex, k); i < endIndex; k = i, i = getLeftChildIndex(beginIndex, k)) {
            if (i + 1 < endIndex && compareTo(array, i, i + 1) < 0) i++;
            if (compareTo(array, k, i) >= 0) break;
            swap(array, k, i);
        }
     }

     private int getParentIndex(int beginIndex, int k) {
        return beginIndex + (k - beginIndex) / 2 - 1;
     }

     private int getLeftChildIndex(int beginIndex, int k){
         return beginIndex + 2 * (k - beginIndex) + 1;
     }
}

