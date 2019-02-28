/*
Author:       Yang Li
Project Name: SERP Engine Simulator 3.0
Package:      Google Search 3.0
Class:        Main.java
 */

package GoogleSearch3;

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
    Button sortedButton;
    Button insert;
    Button delete;

    TextField searchField; //The textField that let user enter their searches
    TextField pageRankSelected; //The textField that let user enter the targeted pagerank

    //insert and delete textField and button
    TextField insertURl;
    TextField insertScore1;
    TextField insertScore2;
    TextField insertScore3;
    TextField insertScore4;
    Button insertSubmit;

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

        sortedButton = new Button("Sort based on TotalScore");
        sortedButton.setPrefSize(300, 30);

        insert = new Button("Insert");
        insert.setPrefSize(300, 30);

        delete = new Button("Delete");
        delete.setPrefSize(300, 30);

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
        deletePageRank = new TextField("PageRank: ");
        deletePageRank.setPrefSize(300, 30);

        deleteSubmit = new Button("Submit");
        deleteSubmit.setPrefSize(300, 30);

        /*This main button "Search" fetch the data from Internet that return the 30 results,
        all the magic happen when this button clicked, main method of this class
         */
        searchButton.setOnAction((ActionEvent action) ->
                {
                    //store the value into the "Search" from textField.getText function
                    search = searchField.getText();

                    //The full url that return the result of user input with 30 links
                    String searchURL = GOOGLE_SEARCH_URL + "?q=" + search + "&num=50";

                    try {

                        RBTree rbTree = new RBTree();

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
                            Node link = new Node(0, 0, 0, 0, 0, 0, "", "", 0, "");
                            link.setScore1(pScore1);
                            link.setScore2(pScore2);
                            link.setScore3(pScore3);
                            link.setScore4(pScore4);
                            link.setTotalScore(pTotalScore);
                            link.setUrl(url);
                            link.setTitle(title);
                            position++;
                            link.setPosition(position);

                            if(position > 30) break;
                            rbTree.RBTree_insert(link);
                        }

                        // Setting the three listViewL: all results, insert, delete, pageRank check
                        ListView<String> allView = new ListView<>();
                        ListView<String> sortedView = new ListView<>();
                        ListView<String> insertedView = new ListView<>();
                        ListView<String> deletedView = new ListView<>();
                        ListView<String> pageRankView = new ListView<>();

                        // Setting the size for the three ListView
                        allView.setPrefSize(900, 600);
                        sortedView.setPrefSize(900, 600);
                        insertedView.setPrefSize(900, 600);
                        deletedView.setPrefSize(900, 600);
                        pageRankView.setPrefSize(900, 600);


                        //Create the ArrayList to inorder walk the tree
                        ArrayList<Node> unsorted = new ArrayList<>();
                        Node mainRoot = rbTree.getRoot();
                        Node mainNil = rbTree.getNil();
                        ArrayList<Node> inOrderTree = inOrder(mainRoot, mainNil, unsorted);

                        //reAssign pageRank
                        int newPR = 1;
                        for(int i = inOrderTree.size() - 1; i>=0; i--){
                            inOrderTree.get(i).setPageRank(newPR);
                            newPR++;
                        }

                        //for loop traverse the whole ArrayList, add String with Link, title and 4 factors of pageRank (30 results)
                        int rank = 1;
                        for(int i = inOrderTree.size() - 1; i >= 0; i--){
                            String tempLink = "[" + (rank) + "]" + "COLOR: " + inOrderTree.get(i).getColor() +
                                    "         || Index: " + inOrderTree.get(i).getPosition() + "      || Link: " + inOrderTree.get(i).getUrl();
                            String tempScore = "PageRank: " + inOrderTree.get(i).getPageRank() + " |TotalScore: " + inOrderTree.get(i).getTotalScore() + " ||Factor1: " + inOrderTree.get(i).getScore1() + " |Factor2: " +
                                    inOrderTree.get(i).getScore2() + " |Factor3: " + inOrderTree.get(i).getScore3() + " |Factor4: " + inOrderTree.get(i).getScore4();
                            allView.getItems().add(tempLink);
                            allView.getItems().add(tempScore);
                            rank++;
                        }

                        //Create the verticle box, horizontal box, and scene for display the 30 results listView once the search Button clicked
                        VBox vboxTop = new VBox(searchField, searchButton, pageRankSelected, pageRankSearch, insert, delete, sortedButton);
                        HBox hboxTop = new HBox(vboxTop, allView);
                        Scene sceneDisplayTop = new Scene(hboxTop, 1200, 600);
                        primaryStage.setScene(sceneDisplayTop);
                        primaryStage.show();

                        pageRankSearch.setOnAction((ActionEvent) -> {
                            //get the pageRank number by the textField
                            int prField = Integer.parseInt(pageRankSelected.getText());

                            int tScore = 0; //create a score that about to search
                            for(int i = 0; i < inOrderTree.size(); i++) {
                                if(inOrderTree.get(i).getPageRank() == prField){
                                    //get the totalScore by the pageRank
                                    tScore = inOrderTree.get(i).getTotalScore();
                                }
                            }

                            //Create the node and using the tree search to get the right Node
                            Node temp;
                            temp = rbTree.RBTree_search(rbTree.getRoot(), tScore);
                            String tempLink ="Title: " + temp.getTitle() + "  || Link: " +
                                    temp.getUrl();
                            String tempScore = "PageRank: " + temp.getPageRank() + " |TotalScore: " + temp.getTotalScore() + " ||Factor1: " + temp.getScore1() + " |Factor2: " +
                                    temp.getScore2() + " |Factor3: " + temp.getScore3() + " |Factor4: " + temp.getScore4() + " ||original index: " + temp.getPosition();

                            pageRankView.getItems().add(tempLink);
                            pageRankView.getItems().add(tempScore);

                            //Create the verticle box, horizontal box, and scene for display the result by the selected pageRank
                            VBox pageRankResultVBox = new VBox(searchField, searchButton, insert, delete);
                            HBox pageRankResultHBox = new HBox(pageRankResultVBox, pageRankView);
                            Scene pageRankResultScene = new Scene(pageRankResultHBox, 1200, 600);
                            primaryStage.setScene(pageRankResultScene);
                            primaryStage.show();
                        });

                        sortedButton.setOnAction((ActionEvent) -> {
                            //for loop traverse the whole ArrayList, add String with Link, title and 4 factors of pageRank (30 results)
                            int scoreRank = 1;
                            for(int i = 0; i < inOrderTree.size(); i++){
                                String tempLink = "[" + (scoreRank) + "]" + " COLOR: " + inOrderTree.get(i).getColor() + " |TotalScore: " + inOrderTree.get(i).getTotalScore() +
                                "|| Index: " + inOrderTree.get(i).getPosition() + "  || Link: " + inOrderTree.get(i).getUrl();
                                String tempScore = "PageRank: " + inOrderTree.get(i).getPageRank() + " ||Factor1: " + inOrderTree.get(i).getScore1() + " |Factor2: " +
                                        inOrderTree.get(i).getScore2() + " |Factor3: " + inOrderTree.get(i).getScore3() + " |Factor4: " + inOrderTree.get(i).getScore4();
                                sortedView.getItems().add(tempLink);
                                sortedView.getItems().add(tempScore);
                                scoreRank++;
                            }

                            //Create the verticle box, horizontal box, and scene for display the 30 results listView once the search Button clicked
                            VBox vboxSorted = new VBox(searchField, searchButton, pageRankSelected, pageRankSearch, insert, delete, sortedButton);
                            HBox hboxSorted = new HBox(vboxSorted, sortedView);
                            Scene sceneDisplaySorted = new Scene(hboxSorted, 1200, 600);
                            primaryStage.setScene(sceneDisplaySorted);
                            primaryStage.show();
                        });

                        insert.setOnAction((ActionEvent) -> {
                            //Create the verticle box, horizontal box, and scene that allow users to enter the url and 4 scores
                            VBox insertVBox = new VBox(searchField, searchButton, insert, delete, insertURl, insertScore1, insertScore2, insertScore3, insertScore4, insertSubmit);
                            HBox insertHBox = new HBox(insertVBox, insertedView);
                            Scene insertScene = new Scene(insertHBox, 1200, 600);
                            primaryStage.setScene(insertScene);
                            primaryStage.show();
                            insertedView.getItems().clear();
                        });

                        delete.setOnAction((ActionEvent) -> {
                            //Create the verticle box, horizontal box, and scene that allow users to delete the URL by pageRank
                            VBox deleteVBox = new VBox(searchField, searchButton, insert, delete, deletePageRank, deleteSubmit);
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
                            int insertPosition = 30;
                            Node insertNode = new Node(score1Inserted, score2Inserted, score3Inserted, score4Inserted, totalInsert, 0, urlInserted, "", ++insertPosition, "");

                            //Insert the Node using RBT
                            rbTree.RBTree_insert(insertNode);

                            //Create the ArrayList to inorder walk the tree
                            ArrayList<Node> newTree = new ArrayList<>();
                            Node currentRoot = rbTree.getRoot();
                            Node currentNil = rbTree.getNil();
                            ArrayList<Node> insertedTree = inOrder(currentRoot, currentNil, newTree);

                            //reAssign pageRank
                            int PR = 1;
                            for(int i = newTree.size() - 1; i>=0; i--){
                                newTree.get(i).setPageRank(PR);
                                PR++;
                            }

                            //for loop traverse the whole ArrayList, add String with Link, title and 4 factors of pageRank (30 results) after insert
                            int newRank = 1;
                            for(int i = newTree.size() - 1; i >= 0; i--){
                                String tempLink = "[" + (newRank) + "]" + " COLOR: " + insertedTree.get(i).getColor() + "  || Link: " +
                                        insertedTree.get(i).getUrl() + "|| Index: " + insertedTree.get(i).getPosition();
                                String tempScore = "PageRank: " + insertedTree.get(i).getPageRank() + " |TotalScore: " + insertedTree.get(i).getTotalScore() +
                                        " ||Factor1: " + insertedTree.get(i).getScore1() + " |Factor2: " +
                                        insertedTree.get(i).getScore2() + " |Factor3: " + insertedTree.get(i).getScore3() + " |Factor4: " + insertedTree.get(i).getScore4();
                                insertedView.getItems().add(tempLink);
                                insertedView.getItems().add(tempScore);
                                newRank++;
                            }

                            //Create the verticle box, horizontal box, and scene for display the 30 results listView after insert once the insert button clicked
                            VBox insertNewVBox = new VBox(searchField, searchButton, insert, insertSubmit, delete);
                            HBox insertNewHBox = new HBox(insertNewVBox, insertedView);
                            Scene insertNewScene = new Scene(insertNewHBox, 1200, 600);
                            primaryStage.setScene(insertNewScene);
                            primaryStage.show();
                        });

                        deleteSubmit.setOnAction((ActionEvent) -> {
                            //getting pagerank by the textFields
                            int pageRankDeleted = Integer.parseInt(deletePageRank.getText());

                            /* Create the Node the store the delete variables
                                user can delete Node by the pageRank
                             */
                            Node deleteNode;
                            for(int i = 0; i < inOrderTree.size(); i++){
                                if(inOrderTree.get(i).getPageRank() == pageRankDeleted){
                                    deleteNode = inOrderTree.get(i);
                                    rbTree.RBTree_delete(deleteNode);
                                }
                            }

                            //Create the ArrayList to inorder walk the tree
                            ArrayList<Node> newTree = new ArrayList<>();
                            Node deleteRoot = rbTree.getRoot();
                            Node deleteNil = rbTree.getNil();
                            ArrayList<Node> inOrderTree2 = inOrder(deleteRoot, deleteNil, newTree);

                            //reAssign pageRank
                            int PR = 1;
                            for(int i = newTree.size() - 1; i>=0; i--){
                                newTree.get(i).setPageRank(PR);
                                PR++;
                            }

                            //for loop traverse the whole ArrayList, add String with Link, title and 4 factors of pageRank (30 results) after delete
                            int newRankDelete = 1;
                            for(int i = inOrderTree2.size() - 1; i >= 0; i--){
                                String tempLink = "[" + (newRankDelete) + "]" + "COLOR: " + inOrderTree2.get(i).getColor() +
                                        "         || Index: " + inOrderTree2.get(i).getPosition() + "      || Link: " + inOrderTree2.get(i).getUrl();
                                String tempScore = "PageRank: " + inOrderTree2.get(i).getPageRank() + " |TotalScore: " + inOrderTree2.get(i).getTotalScore() + " ||Factor1: " + inOrderTree2.get(i).getScore1() + " |Factor2: " +
                                        inOrderTree2.get(i).getScore2() + " |Factor3: " + inOrderTree2.get(i).getScore3() + " |Factor4: " + inOrderTree2.get(i).getScore4();
                                deletedView.getItems().add(tempLink);
                                deletedView.getItems().add(tempScore);
                                newRankDelete++;
                            }

                            //Create the verticle box, horizontal box, and scene for display the 30 results listView after insert once the delete button clicked
                            VBox deleteNewVBox = new VBox(searchField, searchButton, insert, delete, deletePageRank, deleteSubmit);
                            HBox deleteNewHBox = new HBox(deleteNewVBox, deletedView);
                            Scene deleteNewScene = new Scene(deleteNewHBox, 1200, 600);
                            primaryStage.setScene(deleteNewScene);
                            primaryStage.show();
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        //Create the verticle box and scene for display the main scene that contains search Button.
        VBox welcome = new VBox(searchField, searchButton);
        Scene scene = new Scene(welcome, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /* Inorder tree walk method */
    public ArrayList<Node> inOrder(Node mainNull, Node mainNil, ArrayList<Node> newList){
        Node x = mainNull;
        Node y = mainNil;
        while (x != y) {
            if (x.getLeft() == y) {
                newList.add(x);
                x = x.getRight();
            }
            else {
                /* Find the inorder predecessor of current */
                Node pre = x.getLeft();
                while (pre.getRight() != y && pre.getRight() != x)
                    pre = pre.getRight();

                /* Make current as right child of its inorder predecessor */
                if (pre.getRight() == y) {
                    pre.setRight(x);
                    x = x.getLeft();
                }
                else {
                    pre.setRight(y);
                    newList.add(x);
                    x = x.getRight();
                }
            }
        }
        return newList;
    }

    public static void main(String[] args) {

        //Launch the UI
        launch(args);
    }
}
