package presentation;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class presentation extends Application {
    //graph.Graph graph = new graph.Graph();
    @Override
    public void start(Stage stage) {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #2a2e2b");

        Label title = getNewTitleLabel("Bacon Number Generator" , 26);
        GridPane grid = getSearchBox();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        Button searchButton = new Button("Find bacon number");
        searchButton.setOnAction(new findBaconNumberHandler());

        root.getChildren().addAll(title,grid, searchButton);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(100,0,0,0));
        root.setSpacing(30);

        Scene scene = new Scene(root, 500,400);
        stage.setScene(scene);
        stage.show();
    }

    private ComboBox<?> source;
    private ComboBox<?> target;
    private GridPane getSearchBox(){
        //ObservableList<?> oList = FXCollections.observableArrayList(graph.getPersons());
        //source = getNewComboBox(oList);
        //target = getNewComboBox(oList);
        final GridPane grid = new GridPane();
        grid.add(getNewTitleLabel("Source", 20), 0,0);
        grid.add(source,1,0);
        grid.add(getNewTitleLabel("Target", 20), 0,1);
        grid.add(target,1,1);

        return grid;
    }

    private ComboBox<?> getNewComboBox(ObservableList<?> o) {
        ComboBox<?> cb = new ComboBox<>(o);
        cb.setPrefWidth(200);
        return cb;
    }

    private Label getNewTitleLabel(String text, int size) {
        Label l = new Label(text);
        l.setTextFill(Color.WHITE);
        l.setFont(new Font( size));
        l.setAlignment(Pos.CENTER);
        return l;
    }

    public static void main(String[] args) {
        launch(args);
    }

    private class findBaconNumberHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            if(source.getSelectionModel().isEmpty() || target.getSelectionModel().isEmpty()){
                return;
            }
            System.out.println("make search");
        }
    }
}
