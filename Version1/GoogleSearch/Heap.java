/*
Author:       Yang Li
Project Name: SERP Engine Simulator
Package:      Google Search
Class:        Heap.java
 */

package GoogleSearch;

import java.util.ArrayList;

public class Heap {

    /* Create the ArrayList and the size */
    public ArrayList<Node> arr;
    public int size;

    /* Constructor */
    public Heap(ArrayList<Node> arr) {
        this.arr = arr;
        this.size = arr.size();
    }

    /* Max_heapify method */
    public void max_heapify(int index) {
        int left = 2 * index + 1;
        int right = 2 * index + 2;

        int largest;

        if (left < size && arr.get(left).getPageRank() > arr.get(index).getPageRank())
            largest = left;
        else largest = index;
        if (right < size && arr.get(right).getPageRank() > arr.get(largest).getPageRank())
            largest = right;
        if (largest != index) {
            swap(arr, index, largest);
            max_heapify(largest);
        }
    }

    /* Build_max_heap method */
    public void build_max_heap() {
        this.size = arr.size();
        for (int i = (arr.size() / 2) - 1; i >= 0; i--)
            max_heapify(i);
    }

    /* Heap_sort method */
    public void heap_sort() {
        build_max_heap();
        for (int i = arr.size() - 1; i >= 1; i--) {
            swap(arr, i, 0);
            this.size = this.size - 1;
            max_heapify(0);
        }
    }

    /* get the parent method */
    public int getParent(int i) {
        int p = (i-1) / 2;
        return p;
    }

    /* Heap_maximum method */
    public Node heap_maximum() {
        return arr.get(0);
    }

    /* Heap_extract_max method */
    public Node heap_extract_max(){
        if(arr.size() < 1) {
            System.out.println("heap underflow");
        }

        Node max = heap_maximum();

        arr.set(0, arr.get(arr.size() - 1));
        arr.remove(arr.size() - 1);
        this.size = arr.size();

        max_heapify(0);

        return max;
    }

    /* Heap_increase_key method */
    public void heap_increase_key(int i, Node key){
        if(key.getPageRank() < arr.get(i).getPageRank()){
            System.out.println("new key is smaller than current key");
        }
        arr.set(arr.indexOf(arr.get(i)), key);
        while(i > 0 && arr.get(getParent(i)).getPageRank() < arr.get(i).getPageRank()) {
            swap(arr, getParent(i), i);
            i = getParent(i);
        }
    }

    /* Max_heap_insert method */
    public void max_heap_insert(ArrayList<Node> arr, Node key) {
        this.size = this.size + 1;
        int negInf = (int)(Double.NEGATIVE_INFINITY);
        Node zero = new Node(0, 0, 0, 0, negInf, "", "", 0);

        arr.set(arr.size(), zero);
        heap_increase_key(this.size, key);
    }

    /* Swap method that swapping the two selected Node */
    public static void swap(ArrayList<Node> arr, int i, int j){
        Node temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }
}
