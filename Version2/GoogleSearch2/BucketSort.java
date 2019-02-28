/*
Author:       Yang Li
Project Name: SERP Engine Simulator 2.0
Package:      Google Search 2.0
Class:        BucketSort.java
 */

package GoogleSearch2;

import java.util.ArrayList;

public class BucketSort {

    /* Bucket Sort method */
    public ArrayList<Node> bucket_sort(ArrayList<Node> topOne){

        //Create buckets(ArrayList) and each bucket contain an ArrayList
        ArrayList<ArrayList<Node>> buckets = new ArrayList<>();

        //Create the size of the buckets since the alphabet only have 26 char
        int size = 26;

        //Initialize each bucket with a new ArrayList
        for(int i = 0; i < size; i++){
            buckets.add(i, new ArrayList<Node>());
        }

        //For every coming result of company name
        for(int i = 1; i < topOne.size(); i++){
            int asciiToInt = (int)topOne.get(i).getCompanyName().charAt(0)-97; //get the ASCII number for char

            //If the char is valid, add it to the targeted bucket
            if(asciiToInt >= 0)
                buckets.get(asciiToInt).add(topOne.get(i));
            else
                continue;
        }

        //For each bucket, do the insert sort and sort it in alphabet order
        for(int i = 0; i < size - 1; i++){
            insertSort(buckets.get(i));
        }

        //Create the final list to store the ordered data
        ArrayList<Node> buckets_final = new ArrayList<>();
        for(int i = 0; i < size - 1; i++){
            for(int j = 0; j < buckets.get(i).size(); j++){
                buckets_final.add(buckets.get(i).get(j));
            }
        }

        return buckets_final;
    }

    /* Insert Sort method */
    public void insertSort(ArrayList<Node> bucket){
        Node temp;
        for(int j = 1; j < bucket.size(); j++){
            temp = bucket.get(j);

            int i = j - 1;

            //since it's not Int, using compareTo method
            while (i >= 0 && bucket.get(i).getCompanyName().compareTo(temp.getCompanyName()) > 0){
                bucket.set(i+1, bucket.get(i));
                i = i - 1;
            }
            bucket.set(i + 1, temp);
        }
    }
}
