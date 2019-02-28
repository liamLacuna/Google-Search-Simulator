/*
Author:       Yang Li
Project Name: SERP Engine Simulator 2.0
Package:      Google Search 2.0
Class:        UniqueSort.java
 */

package GoogleSearch2;

import java.util.ArrayList;

public class UniqueSort {
    //Create an ArrayList
    private ArrayList<Node> arr;

    //Constructor the ArrayList
    public UniqueSort(ArrayList<Node> arr){
        this.arr = arr;
    }

    //Sorting method using insertion sort
    public void sortUnique(){
        for(int j = 1; j < arr.size(); j++){
            int key = arr.get(j).getCount();

            int i = j - 1;
            while (i >= 0 && arr.get(i).getCount() > key){
                Node temp = arr.get(i + 1);
                arr.set(i + 1, arr.get(i));
                arr.set(i, temp);
                i = i - 1;
            }
            arr.get(i + 1).setCount(key);
        }
    }
}

