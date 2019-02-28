/*
Author:       Yang Li
Project Name: SERP Engine Simulator 2.0
Package:      Google Search 2.0
Class:        Quick.java
 */

package GoogleSearch2;

import java.util.ArrayList;

public class Quick {

    /* Create the ArrayList and the size */
    public ArrayList<Node> arr;

    /* Constructor */
    public Quick(ArrayList<Node> arr) {
        this.arr = arr;
    }

    /* Quick Sort */
    void QuickSort(ArrayList<Node> arr, int p, int r){
        if(p < r) {
            int q = Partition(arr, p, r);
            QuickSort(arr, p, q - 1);
            QuickSort(arr, q + 1, r);
        }
    }

    /* Partition method */
    int Partition(ArrayList<Node>arr, int p, int r){
        int x = arr.get(r).getTotalScore(); //the pivot
        int index = p - 1;
        for(int j = p; j <= r - 1; j++){
            if(arr.get(j).getTotalScore() <= x){
                index = index + 1;
                swap(arr, index, j);
            }
        }
        swap(arr, index+1, r);

        return index + 1;
    }

    /* Swap method that swapping the two selected Node */
    public static void swap(ArrayList<Node> arr, int i, int j){
        Node temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }
}
