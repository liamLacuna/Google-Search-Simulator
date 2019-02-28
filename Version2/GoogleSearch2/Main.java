/*
Author:       Yang Li
Project Name: SERP Engine Simulator 2.0
Package:      Google Search 2.0
Class:        Main.java
 */

package GoogleSearch2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    //Create the final static String that store the Google Search Url
    public static final String GOOGLE_SEARCH_URL = "https://www.google.com/search";

    //Store the user search into private static String
    private static String search;

    //Create the display stage as window;
    Stage window;

    //Create five button
    Button searchButton; //the search button
    Button pageRankSearch; //direct to the search pageRank scene button
    Button displayTopUnique; //display the ten unique keyword button
    Button insert;
    Button delete;
    Button displayTopOneResult;

    TextField searchField; //The textField that let user enter their searches
    TextField pageRankSelected; //The textField that let user enter the targeted pagerank

    //insert and delete textField and button
    TextField insertURl;
    TextField insertScore1;
    TextField insertScore2;
    TextField insertScore3;
    TextField insertScore4;
    Button insertSubmit;

    TextField deleteURl;
    TextField deletePageRank;
    Button deleteSubmit;

    @Override
    public void start(Stage primaryStage) {

        //Setting the UI property of the variables above
        window = primaryStage;
        window.setTitle("Google Search Engine");

        searchButton = new Button("Search");
        searchButton.setPrefSize(300, 30);

        pageRankSearch = new Button("Search the pageRank");
        pageRankSearch.setPrefSize(300, 30);

        insert = new Button("Insert");
        insert.setPrefSize(300, 30);
        delete = new Button("Delete");
        delete.setPrefSize(300, 30);

        displayTopUnique = new Button("Display the top 10 Unique Search ");
        displayTopUnique.setPrefSize(300, 30);

        displayTopOneResult = new Button("Display the top 1 result ");
        displayTopOneResult.setPrefSize(300, 30);

        searchField = new TextField("Enter the search");
        searchField.setPrefSize(300, 30);

        pageRankSelected = new TextField("PageRank: ");
        searchField.setPrefSize(300, 30);

        //insert
        insertURl = new TextField("URL...");
        insertURl.setPrefSize(300, 30);

        insertScore1 = new TextField("Score1...");
        insertScore1.setPrefSize(300, 30);

        insertScore2 = new TextField("Score2...");
        insertScore2.setPrefSize(300, 30);

        insertScore3 = new TextField("Score3...");
        insertScore3.setPrefSize(300, 30);

        insertScore4 = new TextField("Score4...");
        insertScore4.setPrefSize(300, 30);

        insertSubmit = new Button("Submit");
        insertSubmit.setPrefSize(300, 30);

        //delete
        deleteURl = new TextField("URL: ");
        deleteURl.setPrefSize(300, 30);

        deletePageRank = new TextField("PageRank: ");
        deletePageRank.setPrefSize(300, 30);

        deleteSubmit = new Button("Submit");
        deleteSubmit.setPrefSize(300, 30);

        //Create an ArrayList of object "Unique" as uniqueArrayList
        ArrayList<Node> uniqueArrayList = new ArrayList<>();

        //Create a uniqueSort instance of the Class uniqueSort.java, put the uniqueArrayList into it and sort
        UniqueSort uniqueSort = new UniqueSort(uniqueArrayList);

        /*This main button "Search" fetch the data from Internet that return the 30 results,
        all the magic happen when this button clicked, main method of this class
         */
        searchButton.setOnAction((ActionEvent action) ->
                {
                    //store the value into the "Search" from textField.getText function
                    search = searchField.getText();

                    //The full url that return the result of user input with 30 links
                    String searchURL = GOOGLE_SEARCH_URL + "?q=" + search + "&num=30";

                    //Create an instance object of Node and store the "search" into the object
                    Node unique = new Node(0, 0, 0, 0, 0, 0, "", "", 0, "", search);

                    ListView<String> uniqueView = new ListView<>();
                    uniqueView.setPrefSize(900, 600);

                    /*Algorithm that increase the count if the keyword already existed,
                    if the keyword have't created, create a new one with count as one.
                    */
                    if(uniqueArrayList.size() == 0) uniqueArrayList.add(unique); //if empty, add one first
                    else {
                        boolean isUniq = true; //flag that checking the keyword is existed or not

                        //Travel the ArrayList that find if the keyword existed or not, if yes, count one more time
                        for (int i = 0; i < uniqueArrayList.size(); i++) {
                            if (uniqueArrayList.get(i).getKeyword().equals(unique.getKeyword())) {
                                uniqueArrayList.get(i).countMore();
                                isUniq = false;
                            }
                        }
                        if(isUniq) uniqueArrayList.add(unique); //if the keyword is unique, then create one
                    }

                    uniqueSort.sortUnique(); //Sort the count from highest to lowest

                    try {
                        //create a arraylist of object(Node.java) to store all the results
                        ArrayList<Node> nodeList = new ArrayList<>();

                        //Create a HeapTree(instance of Quick.java) for store the 30+ results
                        Quick quick = new Quick(nodeList);

                        //Create a BST (instance of BinarySearchTree.java)
                        BinarySearchTree bTree = new BinarySearchTree();

                        //Create the ArrayList to store the bucketsort results
                        ArrayList<Node> bucketList = new ArrayList<>();

                        //Create a BucketSort instance
                        BucketSort topOneResult = new BucketSort();

                        //Fetch the page
                        Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();

                        //Select the div, traverse the results and getting each result
                        Elements results = doc.select("h3.r > a");

                        int position = 0;
                        for (Element result : results) {

                            //getting the link, title from the div h3.
                            String linkHref = result.attr("href");
                            String url = linkHref.substring(7, linkHref.indexOf("&"));
                            String title = result.text();

                            //Random 4 numbers for 4 factors between 0 - 30 and sum up as pageRank
                            int pScore1 = (int) (Math.random() * 31);
                            int pScore2 = (int) (Math.random() * 31);
                            int pScore3 = (int) (Math.random() * 31);
                            int pScore4 = (int) (Math.random() * 31);
                            int pTotalScore = pScore1 + pScore2 + pScore3 + pScore4;

                            //for each result, create a node that store pageRank, url and title, position
                            Node link = new Node(0, 0, 0, 0, 0, 0, "", "", 0, "", "");
                            link.setScore1(pScore1);
                            link.setScore2(pScore2);
                            link.setScore3(pScore3);
                            link.setScore4(pScore4);
                            link.setTotalScore(pTotalScore);
                            link.setUrl(url);
                            link.setTitle(title);
                            link.setKeyword(search);
                            position++;
                            link.setPosition(position);
                            link.setCompanyName(getHostName(link.getUrl()));

                            //store all of the node into the ArrayList of all results
                            nodeList.add(link);
                            bTree.tree_insert(link);
                            bucketList.add(link);
                        }

                        //Quick Sort the 30 results and assign the pageRank
                        quick.QuickSort(nodeList, 0, nodeList.size() - 1);
                        int pr = 1;
                        for(int i = nodeList.size() - 1; i>=0; i--){
                            nodeList.get(i).setPageRank(pr);
                            pr++;
                        }

                        //Create the ArrayList that store the bucketsort results.
                        ArrayList<Node> listOfTop = topOneResult.bucket_sort(bucketList);

                        // Setting the three listViewL: all results, insert, delete, pageRank check
                        ListView<String> allView = new ListView<>();
                        ListView<String> insertedView = new ListView<>();
                        ListView<String> deletedView = new ListView<>();
                        ListView<String> pageRankView = new ListView<>();
                        ListView<String> topView = new ListView<>();

                        // Setting the size for the three ListView
                        allView.setPrefSize(900, 600);
                        insertedView.setPrefSize(900, 600);
                        deletedView.setPrefSize(900, 600);
                        pageRankView.setPrefSize(900, 600);
                        topView.setPrefSize(900, 600);

                        //for loop traverse the whole ArrayList, add String with Link, title and 4 factors of pageRank (30 results)
                        int rank = 1;
                        for(int i = nodeList.size() - 1; i >= 0; i--){

                            String tempLink = "[" + (rank) + "]" + "Title: " + nodeList.get(i).getTitle() + "  || Link: " +
                                    nodeList.get(i).getUrl() + "|| Index: " + nodeList.get(i).getPosition();
                            String tempScore = "PageRank: " + nodeList.get(i).getPageRank() + " |TotalScore: " + nodeList.get(i).getTotalScore() + " ||with Factor1: " + nodeList.get(i).getScore1() + " |Factor2: " +
                                    nodeList.get(i).getScore2() + " |Factor3: " + nodeList.get(i).getScore3() + " |Factor4: " + nodeList.get(i).getScore4();
                            allView.getItems().add(tempLink);
                            allView.getItems().add(tempScore);
                            rank++;
                        }

                        //Create the verticle box, horizontal box, and scene for display the 30 results listView once the search Button clicked
                        VBox vboxTop = new VBox(searchField, searchButton, displayTopUnique, pageRankSelected, pageRankSearch, insert, delete, displayTopOneResult);
                        HBox hboxTop = new HBox(vboxTop, allView);
                        Scene sceneDisplayTop = new Scene(hboxTop, 1200, 600);
                        primaryStage.setScene(sceneDisplayTop);
                        primaryStage.show();

                        pageRankSearch.setOnAction((ActionEvent) -> {

                            //get the pageRank number by the textField
                            int prField = Integer.parseInt(pageRankSelected.getText());

                            int tScore = 0; //create a score that about to search
                            for(int i = 0; i < nodeList.size(); i++) {
                                if(nodeList.get(i).getPageRank() == prField){
                                    //get the totalScore by the pageRank
                                    tScore = nodeList.get(i).getTotalScore();
                                }
                            }

                            //Create the node and using the tree search to get the right Node
                            Node temp;
                            temp = bTree.tree_search(bTree.getRoot(), tScore);
                            String tempLink ="Title: " + temp.getTitle() + "  || Link: " +
                                    temp.getUrl();
                            String tempScore = "PageRank: " + temp.getPageRank() + " |TotalScore: " + temp.getTotalScore() + " ||with Factor1: " + temp.getScore1() + " |Factor2: " +
                                    temp.getScore2() + " |Factor3: " + temp.getScore3() + " |Factor4: " + temp.getScore4() + " ||original index: " + temp.getPosition();

                            pageRankView.getItems().add(tempLink);
                            pageRankView.getItems().add(tempScore);

                            //Create the verticle box, horizontal box, and scene for display the result by the selected pageRank
                            VBox pageRankResultVBox = new VBox(searchField, searchButton, displayTopUnique, insert, delete);
                            HBox pageRankResultHBox = new HBox(pageRankResultVBox, pageRankView);
                            Scene pageRankResultScene = new Scene(pageRankResultHBox, 1200, 600);
                            primaryStage.setScene(pageRankResultScene);
                            primaryStage.show();
                        });

                        insert.setOnAction((ActionEvent) -> {
                            //Create the verticle box, horizontal box, and scene that allow users to enter the url and 4 scores
                            VBox insertVBox = new VBox(searchField, searchButton, displayTopUnique, insert, delete, insertURl, insertScore1, insertScore2, insertScore3, insertScore4, insertSubmit);
                            HBox insertHBox = new HBox(insertVBox, allView);
                            Scene insertScene = new Scene(insertHBox, 1200, 600);
                            primaryStage.setScene(insertScene);
                            primaryStage.show();
                        });

                        delete.setOnAction((ActionEvent) -> {
                            //Create the verticle box, horizontal box, and scene that allow users to delete the URL by pageRank
                            VBox deleteVBox = new VBox(searchField, searchButton, displayTopUnique, insert, delete, deleteURl, deletePageRank, deleteSubmit);
                            HBox deleteHBox = new HBox(deleteVBox, allView);
                            Scene deleteScene = new Scene(deleteHBox, 1200, 600);
                            primaryStage.setScene(deleteScene);
                            primaryStage.show();
                        });

                        insertSubmit.setOnAction((ActionEvent) -> {

                            // getting the data: url, 4 scores
                            String urlInserted = insertURl.getText();
                            int score1Inserted = Integer.parseInt(insertScore1.getText());
                            int score2Inserted = Integer.parseInt(insertScore2.getText());
                            int score3Inserted = Integer.parseInt(insertScore3.getText());
                            int score4Inserted = Integer.parseInt(insertScore4.getText());
                            int totalInsert = score1Inserted + score2Inserted + score3Inserted + score4Inserted;

                            //Create the Node that having the data above
                            Node insertNode = new Node(score1Inserted, score2Inserted, score3Inserted, score4Inserted, totalInsert, 0, urlInserted, "", 31, "", "");

                            //Insert the Node using BST
                            bTree.tree_insert(insertNode);

                            //Create the ArrayList to inorder walk the tree
                            ArrayList<Node> newTree = new ArrayList<>();
                            Node current = bTree.getRoot();
                            ArrayList<Node> inOrderTree = inOrder(current, newTree);

                            //reAssign pageRank
                            int newPR = 1;
                            for(int i = newTree.size() - 1; i>=0; i--){
                                newTree.get(i).setPageRank(newPR);
                                newPR++;
                            }

                            //for loop traverse the whole ArrayList, add String with Link, title and 4 factors of pageRank (30 results) after insert
                            int newRank = 1;
                            for(int i = newTree.size() - 1; i >= 0; i--){
                                String tempLink = "[" + (newRank) + "]" + "Title: " + inOrderTree.get(i).getTitle() + "  || Link: " +
                                        inOrderTree.get(i).getUrl() + "|| Index: " + inOrderTree.get(i).getPosition();
                                String tempScore = "PageRank: " + inOrderTree.get(i).getPageRank() + " |TotalScore: " + inOrderTree.get(i).getTotalScore() +
                                        " ||with Factor1: " + inOrderTree.get(i).getScore1() + " |Factor2: " +
                                        inOrderTree.get(i).getScore2() + " |Factor3: " + inOrderTree.get(i).getScore3() + " |Factor4: " + inOrderTree.get(i).getScore4();
                                insertedView.getItems().add(tempLink);
                                insertedView.getItems().add(tempScore);
                                newRank++;
                            }

                            //Create the verticle box, horizontal box, and scene for display the 30 results listView after insert once the insert button clicked
                            VBox insertNewVBox = new VBox(searchField, searchButton, displayTopUnique, insert, insertSubmit, delete);
                            HBox insertNewHBox = new HBox(insertNewVBox, insertedView);
                            Scene insertNewScene = new Scene(insertNewHBox, 1200, 600);
                            primaryStage.setScene(insertNewScene);
                            primaryStage.show();
                        });

                        deleteSubmit.setOnAction((ActionEvent) -> {

                            //getting the url, pagerank by the textFields
                            String urlDeleted = deleteURl.getText();
                            int pageRankDeleted = Integer.parseInt(deletePageRank.getText());

                            /* Create the Node the store the delete variables
                                User can either choose delete the Node by the pagerank or the url
                             */
                            Node deleteNode;
                            for(int i = 0; i < nodeList.size(); i++){
                                if(urlDeleted.length() != 0) {
                                    if (nodeList.get(i).getUrl().equals(urlDeleted)) {
                                        deleteNode = nodeList.get(i);
                                        bTree.tree_delete(deleteNode);
                                        break;
                                    }
                                }
                                if(pageRankDeleted >= 0){
                                    if(nodeList.get(i).getPageRank() == pageRankDeleted){
                                        deleteNode = nodeList.get(i);
                                        bTree.tree_delete(deleteNode);
                                        break;
                                    }
                                }
                            }

                            //Create the ArrayList to inorder walk the tree
                            ArrayList<Node> newTree = new ArrayList<>();
                            Node current = bTree.getRoot();
                            ArrayList<Node> inOrderTree2 = inOrder(current, newTree);

                            //reAssign pageRank
                            int newPR = 1;
                            for(int i = newTree.size() - 1; i>=0; i--){
                                newTree.get(i).setPageRank(newPR);
                                newPR++;
                            }

                            //for loop traverse the whole ArrayList, add String with Link, title and 4 factors of pageRank (30 results) after delete
                            int newRankDelete = 1;
                            for(int i = newTree.size() - 1; i >= 0; i--){
                                String tempLink = "[" + (newRankDelete) + "]" + "Title: " + inOrderTree2.get(i).getTitle() + "  || Link: " +
                                        inOrderTree2.get(i).getUrl() + "|| Index: " + nodeList.get(i).getPosition();
                                String tempScore = "PageRank: " + inOrderTree2.get(i).getPageRank() + " |TotalScore: " + inOrderTree2.get(i).getTotalScore() +
                                        " ||with Factor1: " + inOrderTree2.get(i).getScore1() + " |Factor2: " +
                                        inOrderTree2.get(i).getScore2() + " |Factor3: " + inOrderTree2.get(i).getScore3() + " |Factor4: " + inOrderTree2.get(i).getScore4();
                                deletedView.getItems().add(tempLink);
                                deletedView.getItems().add(tempScore);
                                newRankDelete++;
                            }

                            //Create the verticle box, horizontal box, and scene for display the 30 results listView after insert once the delete button clicked
                            VBox deleteNewVBox = new VBox(searchField, searchButton, displayTopUnique, insert, delete, deleteURl, deletePageRank, deleteSubmit);
                            HBox deleteNewHBox = new HBox(deleteNewVBox, deletedView);
                            Scene deleteNewScene = new Scene(deleteNewHBox, 1200, 600);
                            primaryStage.setScene(deleteNewScene);
                            primaryStage.show();
                        });

                        displayTopOneResult.setOnAction((ActionEvent) -> {
                            //for loop traverse the whole ArrayList, add String with Link, title and 4 factors of pageRank (30 results) of unique keyword
                            for(int i = 0; i < listOfTop.size(); i++){
                                String tempLink = "[" + (i+1) + "]" + "Company Name: " + listOfTop.get(i).getCompanyName() +  "|| Title: " + listOfTop.get(i).getTitle() + "  || Score: " +
                                        listOfTop.get(i).getTotalScore() + "||Original Index: " + listOfTop.get(i).getPosition();
                                topView.getItems().add(tempLink);
                            }

                            //Create the verticle box, horizontal box, and scene for display the 30 results listView
                            VBox OneVBox = new VBox(searchField, searchButton, displayTopUnique, pageRankSelected, pageRankSearch, insert, delete, displayTopOneResult);
                            HBox OneHBox = new HBox(OneVBox, topView);
                            Scene OneScene = new Scene(OneHBox, 1200, 600);
                            primaryStage.setScene(OneScene);
                            primaryStage.show();

                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                     /* When click on the display top unique Button,
                            the listView would change to display the top
                            unique keyword and count.
                         */
                    displayTopUnique.setOnAction((ActionEvent) ->{
                        int count = 0;

                        //for loop that add the unique result and the counts to the listView
                        for(int i = uniqueArrayList.size() - 1; i >= 0; i--){
                            String countUnique = "[" + (count) + "]" + "Title: " + uniqueArrayList.get(i).getKeyword() + "  || Count: " +
                                    uniqueArrayList.get(i).getCount();
                            uniqueView.getItems().add(countUnique);
                            count++;
                        }

                        //Create the verticle box, horizontal box, and scene for display the unique results listView once the search Button clicked
                        VBox vboxUnique = new VBox(searchField, searchButton, displayTopUnique);
                        HBox hboxUnique = new HBox(vboxUnique, uniqueView);
                        Scene sceneUnique = new Scene(hboxUnique );
                        primaryStage.setScene(sceneUnique);
                        primaryStage.show();
                    });
                }
        );

        //Create the verticle box and scene for display the main scene that contains search Button.
        VBox welcome = new VBox(searchField, searchButton);
        Scene scene = new Scene(welcome, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /* Inorder tree walk method */
    public ArrayList<Node> inOrder(Node newRoot, ArrayList<Node> newList){
        Node current = newRoot;
        while (current != null) {
            if (current.getLeft() == null) {
                newList.add(current);
                current = current.getRight();
            }
            else {
                /* Find the inorder predecessor of current */
                Node pre = current.getLeft();
                while (pre.getRight() != null && pre.getRight() != current)
                    pre = pre.getRight();

                /* Make current as right child of its inorder predecessor */
                if (pre.getRight() == null) {
                    pre.setRight(current);
                    current = current.getLeft();
                }
                else {
                    pre.setRight(null);
                    newList.add(current);
                    current = current.getRight();
                }
            }
        }
        return newList;
    }

    /* get host name and get company name method */
    public String getHostName(String url){
        if (url.startsWith("https://www"))
            return url.substring(url.indexOf(".") + 1, url.lastIndexOf("."));
        else if (url.startsWith("https://"))
            return url.substring(8, url.lastIndexOf("."));
        else if (url.startsWith("http://www"))
            return url.substring(url.indexOf(".") + 1, url.lastIndexOf("."));
        else if (url.startsWith("https://en"))
            return url.substring(url.indexOf(".") + 1, url.lastIndexOf("."));
        else if(url.startsWith("http://"))
            return url.substring(8, url.indexOf("."));
        else
            return url;
    }

    public static void main(String[] args) {

        //Launch the UI
        launch(args);
    }
}
