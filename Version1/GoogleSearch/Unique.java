/*
Author:       Yang Li
Project Name: SERP Engine Simulator
Package:      Google Search
Class:        Unique.java
 */

package GoogleSearch;

import java.lang.String;

public class Unique {

    //Create the keyword and the count variables
    private String keyword;
    private int count;

    //Constructor that only taking parameter of the keyword and set the count always start at 1
    public Unique(String keyword){
        this.keyword = keyword;
        this.count = 1;
    }

    //ALl the getters and setters
    public int getCount() {
        return count;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    //Method that count increase one
    public void countMore(){
        count = count + 1;
    }
}
