/*
Author:       Yang Li
Project Name: SERP Engine Simulator 2.0
Package:      Google Search 2.0
Class:        Node.java
 */

package GoogleSearch2;

import java.lang.String;

public class Node {

    //Create variables for 4 scores, pagerank, url, title, index and company name
    private int score1;
    private int score2;
    private int score3;
    private int score4;
    private int totalScore;
    private int pageRank;
    private String url;
    private String title;
    private int position;
    private String companyName;
    private String keyword;
    private int count;

    //for BST: Left child, right child and its parent Node
    private Node left;
    private Node right;
    private Node parent;

    //Construct those variables
    public Node(int score1, int score2, int score3, int score4, int totalScore, int pageRank, String url, String title, int position, String companyName, String keyword){
        this.score1 = score1;
        this.score2 = score2;
        this.score3 = score3;
        this.score4 = score4;
        this.totalScore = totalScore;
        this.pageRank = pageRank;
        this.url = url;
        this.title = title;
        this.position = position;
        this.companyName = companyName;
        this.keyword = keyword;
        this.count = 1;
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

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
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

    public void setCount(int count) {
        this.count = count;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public int getTotalScore() {
        return totalScore;
    }

    public int getPageRank(){
        return this.pageRank;
    }

    public String getUrl(){
        return this.url;
    }

    public String getTitle(){
        return this.title;
    }

    public int getPosition() {
        return position;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public Node getParent() {
        return parent;
    }

    public String getCompanyName() {
        return companyName;
    }

    public int getCount() {
        return count;
    }

    public String getKeyword() {
        return keyword;
    }

    //Method that count increase one
    public void countMore(){
        count = count + 1;
    }
}