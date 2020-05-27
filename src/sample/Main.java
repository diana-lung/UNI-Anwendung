package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import view.UI;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        UI ui=new UI();

        primaryStage.setTitle("Student Application");
        GridPane mainGridPane1=ui.createGridPane();
        ui.addMenuUIControls1(mainGridPane1);
        primaryStage.setX(100);
        primaryStage.setY(100);


        // Create a scene with registration form grid pane as the root node
        Scene scene = new Scene(mainGridPane1, 550, 500);
        // Set the scene in primary stage
        primaryStage.setScene(scene);


        Stage secondStage= new Stage();
        secondStage.setTitle("Teacher Application");
        GridPane mainGridPane=ui.createGridPane();
        ui.addMenuUIControls2(mainGridPane);
        secondStage.setX(700);
        secondStage.setY(100);

        // Create a scene with registration form grid pane as the root node
        Scene scene2 = new Scene(mainGridPane, 550, 500);
        // Set the scene in primary stage
        secondStage.setScene(scene2);

        primaryStage.show();
        secondStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

