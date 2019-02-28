/*
Author:       Yang Li
Project Name: SERP Engine Simulator
Package:      Google Search
Class:        Main.java
 */

package GoogleSearch;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
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

    // Create the final static String that store the Google Search Url
    public static final String GOOGLE_SEARCH_URL = "https://www.google.com/search";

    // Store the user search into private static String
    private static String search;

    // Create the display stage as window;
    Stage window;

    // Create five button
    Button searchButton; // the search button
    Button displayTenButton; // display the ten highest pageRank button
    Button pageRankSet; // direct to the setting pageRank scene button
    Button displayTopUnique; // display the ten unique keyword button
    Button change; // confirm the change of the 4 factors of pageRank button

    Label label; // display the label that successfully change of the pageRank

    TextField searchField; // The textField that let user enter their searches

    @Override
    public void start(Stage primaryStage) {

        // Setting the UI property of the variables above
        window = primaryStage;
        window.setTitle("Google Search Engine");

        searchButton = new Button("Search");
        searchButton.setPrefSize(300, 30);

        displayTenButton = new Button("Display 10 results with the highest pageRank");
        displayTenButton.setPrefSize(300, 30);

        pageRankSet = new Button("Setting the pageRank");
        pageRankSet.setPrefSize(300, 30);

        change = new Button("Change");
        change.setPrefSize(300, 30);

        displayTopUnique = new Button("Display the top 10 Unique Search ");
        displayTopUnique.setPrefSize(300, 30);

        searchField = new TextField("Enter the search");
        searchField.setPrefSize(300, 30);

        // Create an ArrayList of object "Unique" as uniqueArrayList
        ArrayList<Unique> uniqueArrayList = new ArrayList<>();

        // Create a uniqueSort instance of the Class uniqueSort.java, put the
        // uniqueArrayList into it and sort
        UniqueSort uniqueSort = new UniqueSort(uniqueArrayList);

        /*
         * This main button "Search" fetch the data from Internet that return the 30
         * results, all the magic happen when this button clicked, main method of this
         * class
         */
        searchButton.setOnAction((ActionEvent action) -> {
            // store the value into the "Search" from textField.getText function
            search = searchField.getText();

            // The full url that return the result of user input with 30 links
            String searchURL = GOOGLE_SEARCH_URL + "?q=" + search + "&num=30";

            // Create a View that display the top 10 unique result and set the width and
            // height
            ListView<String> uniqueView = new ListView<>();
            uniqueView.setPrefSize(900, 600);

            // Create an instance object of Unique.java and store the "search" into the
            // object
            Unique unique = new Unique(search);

            /*
             * Algorithm that increase the count if the keyword already existed, if the
             * keyword have't created, create a new one with count as one.
             */
            if (uniqueArrayList.size() == 0)
                uniqueArrayList.add(unique); // if empty, add one first
            else {
                boolean isUniq = true; // flag that checking the keyword is existed or not

                // Travel the ArrayList that find if the keyword existed or not, if yes, count
                // one more time
                for (int i = 0; i < uniqueArrayList.size(); i++) {
                    if (uniqueArrayList.get(i).getKeyword().equals(unique.getKeyword())) {
                        uniqueArrayList.get(i).countMore();
                        isUniq = false;
                    }
                }
                if (isUniq)
                    uniqueArrayList.add(unique); // if the keyword is unique, then create one
            }

            uniqueSort.sortUnique(); // Sort the count from highest to lowest

            try {
                // create a arraylist of object(Node.java) to store all the results
                ArrayList<Node> nodeList = new ArrayList<>();

                // create a another arraylist of objects(Node.java) to store the top 10 results
                ArrayList<Node> displayList = new ArrayList<>();

                // Create a HeapTree(instance of Heap.java) for store the 30+ results
                Heap heap = new Heap(nodeList);

                // Create a HeapTree(instance of Heap.java) for store the 10 top results
                Heap topHeap = new Heap(displayList);

                // Fetch the page
                Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();

                // Select the div, traverse the results and getting each result
                Elements results = doc.select("h3.r > a");
                int position = 0;
                for (Element result : results) {

                    // getting the link, title from the div h3.
                    String linkHref = result.attr("href");
                    String url = linkHref.substring(7, linkHref.indexOf("&"));
                    String title = result.text();

                    // Random 4 numbers for 4 factors between 0 - 30 and sum up as pageRank
                    int pScore1 = (int) (Math.random() * 31);
                    int pScore2 = (int) (Math.random() * 31);
                    int pScore3 = (int) (Math.random() * 31);
                    int pScore4 = (int) (Math.random() * 31);
                    int pageRank = pScore1 + pScore2 + pScore3 + pScore4;

                    // for each result, create a node that store pageRank, url and title, position
                    Node link = new Node(0, 0, 0, 0, 0, "", "", 0);
                    link.setScore1(pScore1);
                    link.setScore2(pScore2);
                    link.setScore3(pScore3);
                    link.setScore4(pScore4);
                    link.setPageRank(pageRank);
                    link.setUrl(url);
                    link.setTitle(title);
                    position++;
                    link.setPosition(position);

                    // store all of the node into the ArrayList of all results
                    nodeList.add(link);
                }

                // Calling the build-max-method from the Heap.java to set the heap-max tree
                heap.build_max_heap();

                // Setting the three listViewL: all results, top 10, pageRank changed
                ListView<String> allView = new ListView<>();
                ListView<String> topView = new ListView<>();
                ListView<String> pageRankView = new ListView<>();

                // Setting the size for the three ListView
                allView.setPrefSize(900, 600);
                topView.setPrefSize(900, 600);
                pageRankView.setPrefSize(900, 600);

                // for loop traverse the whole ArrayList, add String with Link, title and 4
                // factors of pageRank (30 results)
                for (int i = 0; i < 30; i++) {
                    String tempLink = "[" + (i + 1) + "]" + "Title: " + nodeList.get(i).getTitle() + "  || Link: "
                            + nodeList.get(i).getUrl();
                    String tempScore = "PageRank: " + nodeList.get(i).getPageRank() + " ||with Factor1: "
                            + nodeList.get(i).getScore1() + " |Factor2: " + nodeList.get(i).getScore2() + " |Factor3: "
                            + nodeList.get(i).getScore3() + " |Factor4: " + nodeList.get(i).getScore4();
                    allView.getItems().add(tempLink);
                    allView.getItems().add(tempScore);
                }

                // Button setOnAction function
                displayTenButton.setOnAction((ActionEvent) -> {
                    // for loop using extract-max method to get the highest node to display results
                    // Arraylist
                    for (int i = 0; i < 10; i++) {
                        // get the Result with the highest pageRank
                        Node temp = heap.heap_extract_max();

                        // add each node to top 10 results ArrayList, and set their position form 1 - 10
                        displayList.add(temp);
                        displayList.get(i).setPosition(i + 1);
                    }

                    // for loop for add String with Link, title and 4 factors of pageRank to the
                    // display View
                    for (int i = 0; i < displayList.size(); i++) {
                        String tempLink = "[" + (i + 1) + "]" + "Title: " + displayList.get(i).getTitle()
                                + "  || Link: " + displayList.get(i).getUrl();
                        String tempScore = "PageRank: " + displayList.get(i).getPageRank() + " ||with Factor1: "
                                + displayList.get(i).getScore1() + " |Factor2: " + displayList.get(i).getScore2()
                                + " |Factor3: " + displayList.get(i).getScore3() + " |Factor4: "
                                + displayList.get(i).getScore4();
                        topView.getItems().add(tempLink);
                        topView.getItems().add(tempScore);
                    }

                    // Create the verticle box, horizontal box, and scene for display the top ten
                    // listView once the displayTenButton clicked
                    VBox vboxTen = new VBox(searchField, searchButton, pageRankSet, displayTenButton, displayTopUnique);
                    HBox hboxTen = new HBox(vboxTen, topView);
                    Scene sceneDisplayTen = new Scene(hboxTen, 1200, 600);
                    primaryStage.setScene(sceneDisplayTen);
                    primaryStage.show();
                });

                // Create the verticle box, horizontal box, and scene for display the 30 results
                // listView once the search Button clicked
                VBox vboxTop = new VBox(searchField, searchButton, displayTenButton, displayTopUnique);
                HBox hboxTop = new HBox(vboxTop, allView);
                Scene sceneDisplayTop = new Scene(hboxTop, 1200, 600);
                primaryStage.setScene(sceneDisplayTop);
                primaryStage.show();

                // Setting up the button and textField that let user input their data.
                TextField positionSelected = new TextField("Enter a position");
                TextField score1Selected = new TextField("Factor1");
                score1Selected.setPrefSize(300, 30);

                TextField score2Selected = new TextField("Factor2");
                score2Selected.setPrefSize(300, 30);

                TextField score3Selected = new TextField("Factor3");
                score3Selected.setPrefSize(300, 30);

                TextField score4Selected = new TextField("Factor4");
                score4Selected.setPrefSize(300, 30);

                /*
                 * button click function: After click the page Rank set button, the user are
                 * able to enter their values follow by the position and the 4 factors.
                 */
                pageRankSet.setOnAction((ActionEvent) -> {
                    /*
                     * Create the verticle box, horizontal box, and scene for display the UI let
                     * user input their custom 4 factors once the search Button clicked
                     */
                    VBox vboxSet = new VBox(searchField, searchButton, positionSelected, score1Selected, score2Selected,
                            score3Selected, score4Selected, change, displayTopUnique);
                    HBox hboxSet = new HBox(vboxSet, topView);
                    Scene sceneSet = new Scene(hboxSet);
                    primaryStage.setScene(sceneSet);
                    primaryStage.show();
                });

                /*
                 * button click function: After choosing the result in the ListView and change
                 * the four factors of the pageRank, the ListView would display the results
                 * after the page rank changed.
                 */
                change.setOnAction((ActionEvent actionEvent) -> {
                    // get the position from the position testField.
                    String getPosition = positionSelected.getText();

                    // Travel the whole top ten ArrayList
                    for (int i = 0; i < displayList.size(); i++) {
                        // if the position matched, reset the 4 factors and sum up as new pageRank
                        if (Integer.parseInt(getPosition) == displayList.get(i).getPosition()) {
                            displayList.get(i).setScore1(Integer.parseInt(score1Selected.getText()));
                            displayList.get(i).setScore2(Integer.parseInt(score2Selected.getText()));
                            displayList.get(i).setScore3(Integer.parseInt(score3Selected.getText()));
                            displayList.get(i).setScore4(Integer.parseInt(score4Selected.getText()));
                            int newPageRank = displayList.get(i).getScore1() + displayList.get(i).getScore2()
                                    + displayList.get(i).getScore3() + displayList.get(i).getScore4();
                            displayList.get(i).setPageRank(newPageRank);

                            // Increase the Node as the pageRank increase
                            heap.heap_increase_key(i, displayList.get(i));

                            // display the pageRank successfully increase
                            label = new Label("DONE");
                        }
                    }

                    // resort the top 10 ArrayList
                    topHeap.heap_sort();

                    int count = 1;

                    // for loop to display the new top 10 results
                    for (int i = 9; i >= 0; i--) {
                        String tempLink = "[" + count + "]" + "Title: " + displayList.get(i).getTitle() + "  || Link: "
                                + displayList.get(i).getUrl();
                        String tempScore = "PageRank: " + displayList.get(i).getPageRank() + " ||with Factor1: "
                                + displayList.get(i).getScore1() + " |Factor2: " + displayList.get(i).getScore2()
                                + " |Factor3: " + displayList.get(i).getScore3() + " |Factor4: "
                                + displayList.get(i).getScore4();
                        pageRankView.getItems().add(tempLink);
                        pageRankView.getItems().add(tempScore);
                        count++;
                    }

                    // Create the verticle box, horizontal box, and scene for display the new top
                    // results listView once the search Button clicked
                    VBox vboxChange = new VBox(searchField, searchButton, label, displayTopUnique);
                    HBox hboxChange = new HBox(vboxChange, pageRankView);
                    Scene sceneChange = new Scene(hboxChange);
                    primaryStage.setScene(sceneChange);
                    primaryStage.show();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*
             * When click on the display top unique Button, the listView would change to
             * display the top unique keyword and count.
             */
            displayTopUnique.setOnAction((ActionEvent) -> {
                int count = 0;

                // for loop that add the unique result and the counts to the listView
                for (int i = uniqueArrayList.size() - 1; i >= 0; i--) {
                    String countUnique = "[" + (count) + "]" + "Title: " + uniqueArrayList.get(i).getKeyword()
                            + "  || Count: " + uniqueArrayList.get(i).getCount();
                    uniqueView.getItems().add(countUnique);
                    count++;
                }

                // Create the verticle box, horizontal box, and scene for display the unique
                // results listView once the search Button clicked
                VBox vboxUnique = new VBox(searchField, searchButton, displayTopUnique);
                HBox hboxUnique = new HBox(vboxUnique, uniqueView);
                Scene sceneUnique = new Scene(hboxUnique);
                primaryStage.setScene(sceneUnique);
                primaryStage.show();
            });
        });

        // Create the verticle box and scene for display the main scene that contains
        // search Button.
        VBox welcome = new VBox(searchField, searchButton);
        Scene scene = new Scene(welcome, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {

        // Launch the UI
        launch(args);
    }
}
