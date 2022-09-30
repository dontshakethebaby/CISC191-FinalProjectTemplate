package edu.sdccd.cisc191.template;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import java.io.*;
import static java.lang.System.out;

public class Server extends Application {

    /* Referenced https://www.youtube.com/watch?v=SB9AnciLmsw&ab_channel=JavaCodeJunkie
    for JavaFX basics to do a single stage with multiple scenes
     */
    private Stage stage;
    private Scene initialScene; // First scene
    private Scene guessScene; // Second scene
    private Scene resultsScene; // Third scene
    private Scene computerWordsScene; // Third and a half scene (optional scene between 3rd and 4th)
    private Scene summaryScene; // Fourth and final scene
    private String currentMainWord;
    private int mainIndex;
    private String guess1;
    private String guess2;
    private String guess3;
    int level = 20; // difficulty level set by length of common words list
    private String[] currentCommonWords = new String[level];

    public Server() {
    }

    // Sets the stage with initial scene
    @Override
    public void start(Stage primaryStage) throws IOException, InterruptedException {

        stage = primaryStage;
        stage.setTitle("Mindmeld Single Player");

        initialScene = createInitialScene();
        guessScene = createGuessingScene();
        resultsScene = createResultsScene();
        computerWordsScene = createComputerWordsScene();
        summaryScene = createSummaryScene();

        stage.setScene(initialScene);

        stage.show();
    }

    // Initial scene explains how the game works
    private Scene createInitialScene() {

        Button beginButton = new Button("Begin Game");
        beginButton.setOnAction(e -> stage.setScene(guessScene)); // moves to next scene

        Label title = new Label("Mindmeld vs Computer");
        title.setFont(new Font("", 16));

        String instructions = "This is a word game, you will be given a main word, and then you will guess three " +
                "words that are associated with that word. Your goal is to match as many words as possible from a "+
                "computer generated list of words associated with that main word.\n " ;
        Label instr = new Label(instructions);
        instr.setWrapText(true);

        Label rulesTitle = new Label("Rules");
        rulesTitle.setFont(new Font("", 16));
        String gameRules = "1. Compound words are okay, ex: \"ice cream\", but must be no longer than two words.\n"+
                "2. Your guess cannot be a substring of the main word, ex: \"dino\" for \"DINOSAUR\".\n"+
                "3. Your guess cannot contain the entire main word, ex: \"strawberry\" is not \n"+
                "allowed for \"BERRY\", but \"cherry\" is okay.";
        Label rules = new Label(gameRules);
        rules.setWrapText(true);

        BorderPane border = new BorderPane();
        VBox vbox = new VBox(title,instr,rulesTitle,rules);
        vbox.setPadding(new Insets(20,20,20,20));
        HBox hbox = new HBox(beginButton);
        hbox.setPadding(new Insets(20, 20, 20, 20));
        hbox.setAlignment(Pos.BASELINE_RIGHT);
        border.setCenter(vbox);
        border.setBottom(hbox);

        initialScene = new Scene(border, 500, 400);

        return initialScene;
    }

    /* Guessing scene is where most of the action happens, including picking and saving the main word for
    the game, saving user guesses to file, and generating and saving the common words list.
    */
    private Scene createGuessingScene() throws FileNotFoundException {

        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> {
            stage.setScene(resultsScene);
        }); // moves to next scene

        // These three "Save" buttons and text fields write the textfield data to a file
        TextField guessField1 = new TextField();
        TextField guessField2 = new TextField();
        TextField guessField3 = new TextField();

        // https://stackoverflow.com/questions/1053467/how-do-i-save-a-string-to-a-text-file-using-java
        PrintStream fileStream = new PrintStream("userguesses.txt");
        System.setOut(fileStream);

        Button guess1Button = new Button("Save");
        guess1Button.setOnAction(e -> {
            out.println(guessField1.getText());
        });
        HBox guess1HBox = new HBox(15,guessField1, guess1Button);
        guess1HBox.setAlignment(Pos.CENTER);

        Button guess2Button = new Button("Save");
        guess2Button.setOnAction(e -> {
            out.println(guessField2.getText());
        });
        HBox guess2HBox = new HBox(15,guessField2, guess2Button);
        guess2HBox.setAlignment(Pos.CENTER);

        Button guess3Button = new Button("Save");
        guess3Button.setOnAction(e -> {
            out.println(guessField3.getText());
        });
        HBox guess3HBox = new HBox(15,guessField3, guess3Button);
        guess3HBox.setAlignment(Pos.CENTER);

        // Saves main word, index of main word, and common words list for the game
        String mainWord = DataManager.pickMain();
        String[] array = mainWord.split("_");
        currentMainWord = array[0];
        mainIndex = Integer.parseInt(array[1]);
        currentCommonWords = DataManager.getCommonWords(mainIndex,level);

        // Labels, texts
        Label title = new Label("Guess Words \n");
        title.setFont(new Font("", 16));

        TextFlow flow = new TextFlow();
        Text text1 = new Text("Your main word is ");
        text1.setFont(Font.font("", 14));
        Text text2 = new Text(currentMainWord);
        text2.setFont(Font.font("", FontWeight.BOLD, 14));
        Text text3 = new Text(", give three words associated with ");
        text3.setFont(Font.font("", 14));
        Text text4 = new Text(currentMainWord +  "\n \n");
        text4.setFont(Font.font("", FontWeight.BOLD, 14));
        flow.getChildren().addAll(text1, text2, text3, text4);

        // Organizes visual elements within a border pane layout
        BorderPane border = new BorderPane();
        VBox vbox = new VBox(title,flow,guess1HBox, guess2HBox, guess3HBox);
        vbox.setPadding(new Insets(20, 20, 20, 20));
        HBox hbox = new HBox(nextButton);
        hbox.setPadding(new Insets(20, 20, 20, 20));
        hbox.setAlignment(Pos.BASELINE_RIGHT);
        border.setCenter(vbox);
        border.setBottom(hbox);

        guessScene = new Scene(border, 500, 400);

        return guessScene;

    }

    // Displays results to user
    private Scene createResultsScene() throws IOException, InterruptedException {

        guess1 = DataManager.getGuess(0);
        guess2 = DataManager.getGuess(1);
        guess3 = DataManager.getGuess(2);

        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> stage.setScene(summaryScene)); // moves to next round

        Button computerWordsButton = new Button("See Associated Words List");
        computerWordsButton.setOnAction(e -> stage.setScene(computerWordsScene));

        TextFlow flow = new TextFlow();
        Text text1 = new Text("\n" + guess1 + " earned you " + "points");
        text1.setFont(Font.font("", 14));
        Text text2 = new Text("\n" + guess2 + " earned you " + "points");
        text2.setFont(Font.font("", 14));
        Text text3 = new Text("\n" + guess3 + " earned you " + "points");
        text3.setFont(Font.font("", 14));
        Text text4 = new Text("\n \n For a total of " + "points");
        text4.setFont(Font.font("",FontWeight.BOLD,14));
        flow.getChildren().addAll(text1,text2,text3,text4);

        Label title = new Label("Results");
        title.setFont(new Font("", 16));
        VBox vbox = new VBox(title,flow);
        vbox.setPadding(new Insets(20, 20, 20, 20));

        BorderPane border = new BorderPane();
        VBox vboxRight = new VBox(nextButton);
        vboxRight.setAlignment(Pos.BOTTOM_RIGHT);
        VBox vboxLeft = new VBox(computerWordsButton);
        vboxRight.setAlignment(Pos.BOTTOM_LEFT);
        HBox hbox = new HBox(265,vboxLeft,vboxRight);
        hbox.setPadding(new Insets(20, 20, 20, 20));
        border.setCenter(vbox);
        border.setBottom(hbox);


        resultsScene = new Scene(border, 500, 400);

        return resultsScene;
    }

    private Scene createComputerWordsScene() {
        Button backButton = new Button("Back to Results");
        backButton.setOnAction(e -> stage.setScene(resultsScene)); // moves back to the previous scene (results)

        Label title = new Label("List of Associated Words");
        title.setFont(new Font("",16));

        VBox leftBox = new VBox();
        VBox rightBox = new VBox();
        for (int i = 0; i < 15; i++) {
            Label label = new Label(currentCommonWords[i]);
            leftBox.getChildren().add(label);
        }
        for (int i = 15; i < level; i++) {
            Label label = new Label(currentCommonWords[i]);
            rightBox.getChildren().add(label);
        }
        HBox listBox = new HBox(leftBox,rightBox);

        BorderPane border = new BorderPane();
        VBox vbox = new VBox(title,listBox);
        vbox.setPadding(new Insets(20,20,20,20));
        HBox hbox = new HBox(backButton);
        hbox.setPadding(new Insets(20, 20, 20, 20));
        hbox.setAlignment(Pos.BASELINE_LEFT);
        border.setCenter(vbox);
        border.setBottom(hbox);

        computerWordsScene = new Scene(border, 500, 400);

        return computerWordsScene;
    }

    private Scene createSummaryScene() {
        Button finishButton = new Button("Finish");
        finishButton.setOnAction(e -> Platform.exit()); // exits the stage

        HBox hbox = new HBox(finishButton);
        hbox.setPadding(new Insets(20,20,20,20));
        hbox.setAlignment(Pos.BOTTOM_CENTER);

        summaryScene = new Scene(hbox, 500, 400);

        return summaryScene;
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}