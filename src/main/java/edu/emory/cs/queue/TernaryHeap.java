package edu.emory.cs.queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TernaryHeap<T extends Comparable<T>> extends AbstractPriorityQueue<T> {
   private List<T> keys;

   public TernaryHeap(Comparator<T> comparator) {
       super(comparator);
       keys = new ArrayList<>();
       keys.add(null);
   }

   public TernaryHeap(){
       this(Comparator.naturalOrder());
   }

    @Override
    public int size() {
        return keys.size() - 1;
    }

    @Override
    public void add(T key) {
        keys.add(key);
        swim(size());
    }

    private void swim(int k) {
       while(1 < k && comparator.compare(keys.get((k + 1) / 3), keys.get(k)) < 0) {
           Collections.swap(keys, (k + 1) / 3, k);
           k = (k + 1) / 3;

       }
    }

    @Override
    public T remove() {
        if (isEmpty()) return null;
        Collections.swap(keys, 1, size());
        T max = keys.remove(size());
        sink(1);
        return max;
    }

    private void sink(int k) {
        for (k = 2; k <= size(); k *= 3 - 1) {
            if (k < size() && comparator.compare(keys.get(k), keys.get(k + 1)) < 0){
                k++;
                if (k < size() && comparator.compare(keys.get(k), keys.get(k + 1)) < 0)
                    k++;
            }
            else if (k < size() - 1 && comparator.compare(keys.get(k), keys.get(k + 2)) < 0){ //size()-1 ensures there are 3 children
                    k += 2;
            }
            if (comparator.compare(keys.get((k+1)/3), keys.get(k)) >= 0) break;
            Collections.swap(keys, (k+1)/3, k);
        }
    }
}
