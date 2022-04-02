package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File mainDirectory = directoryChooser.showDialog(primaryStage);

        SpamDetector spamDetector = new SpamDetector(mainDirectory);

        TableColumn<TestFile,String> fileNameCol = new TableColumn<>("File");
        fileNameCol.setMinWidth(295);
        fileNameCol.setCellValueFactory(new PropertyValueFactory<>("filename"));
        TableColumn<TestFile,String> actualClassCol = new TableColumn<>("Actual Class");
        actualClassCol.setMinWidth(100);
        actualClassCol.setCellValueFactory(new PropertyValueFactory<>("actualClass"));
        TableColumn<TestFile,String> detectedClassCol = new TableColumn<>("Detected Class");
        detectedClassCol.setMinWidth(100);
        detectedClassCol.setCellValueFactory(new PropertyValueFactory<>("detectedClass"));
        TableColumn<TestFile,Double> spamProbCol = new TableColumn<>("Spam Probability");
        spamProbCol.setMinWidth(120);
        spamProbCol.setCellValueFactory(new PropertyValueFactory<>("spamProbabilityRounded"));
        TableColumn<TestFile,Double> wordCountCol = new TableColumn<>("Word Count");
        wordCountCol.setMinWidth(120);
        wordCountCol.setCellValueFactory(new PropertyValueFactory<>("wordCount"));

        TableView<TestFile> table;
        table = new TableView<>();
        table.setItems(spamDetector.getTestedFiles());
        table.getColumns().addAll(fileNameCol, actualClassCol, detectedClassCol, spamProbCol, wordCountCol);

        Canvas canvas = new Canvas(750, 50);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 40));
        gc.strokeText("SPAM DETECTOR", Math.round(canvas.getWidth()  / 2), Math.round(canvas.getHeight() / 2));
        gc.fillText("SPAM DETECTOR", Math.round(canvas.getWidth()  / 2), Math.round(canvas.getHeight() / 2));

        Label summaryLb = new Label("--------------------------------------------------Summary--------------------------------------------------");
        summaryLb.setFont(Font.font("Arial", 20));
        Label accuracyLb = new Label("Accuracy:");
        Label precisionLb = new Label("Precision:");
        Label numTruePosLb = new Label("True Positive Files:");
        Label numFalsePosLb = new Label("False Positive Files:");
        Label numTrueNegLb = new Label("True Negative Files:");
        Label numFalseNegLb = new Label("False Negative Files:");
        Label numFilesLb = new Label("Number of Files:");

        TextField accuracyTx = new TextField(spamDetector.getAccuracyRounded());
        accuracyTx.setEditable(false);
        accuracyTx.setMaxWidth(150);
        TextField precisionTx = new TextField(spamDetector.getPrecisionRounded());
        accuracyTx.setEditable(false);
        precisionTx.setMaxWidth(150);
        TextField numTruePosTx = new TextField(spamDetector.getNumTruePosString());
        numTruePosTx.setEditable(false);
        numTruePosTx.setMaxWidth(150);
        TextField numFalsePosTx = new TextField(spamDetector.getNumFalsePosString());
        numFalsePosTx.setEditable(false);
        numFalsePosTx.setMaxWidth(150);
        TextField numTrueNegTx = new TextField(spamDetector.getNumTrueNegString());
        numTrueNegTx.setEditable(false);
        numTrueNegTx.setMaxWidth(150);
        TextField numFalseNegTx = new TextField(spamDetector.getNumFalseNegString());
        numFalseNegTx.setEditable(false);
        numFalseNegTx.setMaxWidth(150);
        TextField numFilesTx = new TextField(spamDetector.getNumFilesString());
        numFilesTx.setEditable(false);
        numFilesTx.setMaxWidth(150);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(canvas,0,0,4,1);
        grid.add(table, 0,1,4,1);
        grid.add(summaryLb,0,2,4,1);
        grid.add(numTruePosLb,0,3);
        grid.add(numTruePosTx,1,3);
        grid.add(numFalsePosLb,2,3);
        grid.add(numFalsePosTx,3,3);
        grid.add(numTrueNegLb,0,4);
        grid.add(numTrueNegTx,1,4);
        grid.add(numFalseNegLb,2,4);
        grid.add(numFalseNegTx,3,4);
        grid.add(accuracyLb,0,5);
        grid.add(accuracyTx,1,5);
        grid.add(precisionLb,2,5);
        grid.add(precisionTx,3,5);
        grid.add(numFilesLb,0,6);
        grid.add(numFilesTx,1,6);

        Scene scene = new Scene(grid,800,650);
        primaryStage.setTitle("Spam Detector");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }
}
