/*
Author:       Yang Li
Project Name: SERP Engine Simulator
Package:      Google Search
Class:        Node.java
 */

package GoogleSearch;

import java.lang.String;

public class Node {

    // Create variables for 4 scores, pagerank, url, title and position
    private int score1;
    private int score2;
    private int score3;
    private int score4;
    private int pageRank;
    private String url;
    private String title;
    private int position;

    // Construct those variables
    public Node(int score1, int score2, int score3, int score4, int pageRank, String url, String title, int position) {
        this.score1 = score1;
        this.score2 = score2;
        this.score3 = score3;
        this.score4 = score4;
        this.pageRank = pageRank;
        this.url = url;
        this.title = title;
        this.position = position;
    }

    /* All the setters and getters */

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public void setScore3(int score3) {
        this.score3 = score3;
    }

    public void setScore4(int score4) {
        this.score4 = score4;
    }

    public void setPageRank(int pageRank) {
        this.pageRank = pageRank;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getScore1() {
        return score1;
    }

    public int getScore2() {
        return score2;
    }

    public int getScore3() {
        return score3;
    }

    public int getScore4() {
        return score4;
    }

    public int getPageRank() {
        return this.pageRank;
    }

    public String getUrl() {
        return this.url;
    }

    public String getTitle() {
        return this.title;
    }

    public int getPosition() {
        return position;
    }
}
