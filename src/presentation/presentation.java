package presentation;

import fileReading.Loader;
import graph.Graph;
import graph.Path;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Presentation extends Application {
    //graph.Graph graph = new graph.Graph();
    ProgressBar actorsPB, actressesPB;
    Label actorsLabel, actressesLabel;
    VBox root = new VBox();
    VBox loadingBox = new VBox();
    VBox baconSearchBox = new VBox();
    Graph graph;
    Loader loader;
    TextField sourceTextField, targetTextField;
    Button searchButton;
    Label result;

    public void updateProgressBar(int loaded, int total, String name) {
        double progress = (double) loaded/total;
        if(name.equals("actors")){
            if(progress-actorsProgress > 0.001){
                actorsProgress = progress;
                //Platform.runLater(this::updateActorsProgress);
                actorsPB.setProgress(actorsProgress);
            }
        } else{
            if(progress-actressesProgress > 0.001){
                actressesProgress = progress;
                //Platform.runLater(this::updateActressesProgress);
                actressesPB.setProgress(actressesProgress);
            }
        }
    }
    private void updateActorsProgress(){
        actorsPB.setProgress(actorsProgress);
    }
    private void updateActressesProgress(){
        actressesPB.setProgress(actressesProgress);
    }

    private double actorsProgress = 0.0;
    private double actressesProgress = 0.0;
    public void updateProgressLabel(String thread, boolean isDone) {
        if(thread.equals("actors")){
            if(isDone) {
                Platform.runLater(this::setActorsLoadToDone);
            } else {
                Platform.runLater(this::setActorsLoadToLoading);
            }
        } else {
            if(isDone){
                Platform.runLater(this::setActressesLoadToDone);
            } else {
                Platform.runLater(this::setActressesLoadToLoading);
            }
        }
    }

    private void setActorsLoadToDone(){
        actorsLabel.setText("Loading actors completed");
        root.getChildren().remove(loadingBox);
        root.getChildren().add(baconSearchBox);
        graph = new Graph(loader.getPersons(), this);
        Thread t = new Thread(graph, "graph");
        t.start();
    }

    private void setActressesLoadToDone(){
        actressesLabel.setText("Loading actresses completed");
    }

    private void setActorsLoadToLoading(){
        actorsLabel.setText("Loading actors...");
    }

    private void setActressesLoadToLoading() {
        actressesLabel.setText("Loading actresses...");
    }

    @Override
    public void start(Stage stage) {
        root.setStyle("-fx-background-color: #2a2e2b");

        Label title = new Label("Bacon Number Generator" );
        title.setFont(new Font(26));
        title.setTextFill(Color.GHOSTWHITE);

        actorsPB = new ProgressBar(0);
        actorsLabel = new Label();
        actorsLabel.setTextFill(Color.GHOSTWHITE);
        actressesPB = new ProgressBar(0);
        actressesLabel = new Label();
        actressesLabel.setTextFill(Color.GHOSTWHITE);
        actorsPB.setPrefWidth(250);
        actorsPB.setPrefHeight(15);
        actressesPB.setPrefWidth(250);
        actressesPB.setPrefHeight(15);
        actorsLabel.setPrefWidth(250);
        actressesLabel.setPrefWidth(250);


        loadingBox.setAlignment(Pos.CENTER);
        Region space = new Region();
        space.setPrefHeight(20);
        loadingBox.getChildren().addAll(actorsLabel,actorsPB,space,actressesLabel,actressesPB);
//    private static final int actorsSize = 2672685;
//    private static final int actressesSize = 1495997;
        loader = new Loader("actors.list", "actresses.list", 2672685,1495997 );
        loader.setPresentation(this);
        Thread t = new Thread(loader);
        t.start();

        Region space2 = new Region();
        Region space3 = new Region();
        Region space4 = new Region();
        space2.setPrefHeight(20);
        space3.setPrefHeight(20);
        space4.setPrefHeight(50);
        Label sourceLabel = new Label("Source:");
        sourceLabel.setTextFill(Color.GHOSTWHITE);
        sourceTextField = new TextField();
        sourceTextField.setMaxWidth(250);
        Label targetLabel = new Label("Target:");
        targetLabel.setTextFill(Color.GHOSTWHITE);
        targetTextField = new TextField();
        targetTextField.setMaxWidth(250);
        baconSearchBox.setPrefWidth(250);
        sourceLabel.setPrefWidth(250);
        targetLabel.setPrefWidth(250);
        baconSearchBox.setAlignment(Pos.CENTER);
        searchButton = new Button("Find bacon number");
        searchButton.setOnAction(new findBaconNumberHandler());
        searchButton.setDisable(true);
        result = new Label();
        result.setTextFill(Color.GHOSTWHITE);
        result.setPrefWidth(250);

        baconSearchBox.getChildren().addAll(sourceLabel, sourceTextField, space2, targetLabel, targetTextField, space3, searchButton, space4, result);

//        root.getChildren().addAll(title,grid, searchButton);
        root.getChildren().addAll(title,loadingBox);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(100,0,0,0));
        root.setSpacing(30);

        Scene scene = new Scene(root, 500,600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private class findBaconNumberHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            graph.setSource(sourceTextField.getText());
            graph.setTarget(targetTextField.getText());
            Path p = graph.findShortestPath();
            p.printPath();
            result.setText(p.toString());
            result.autosize();
        }
    }

    public void enableSearch(){
        searchButton.setDisable(false);
    }

}
