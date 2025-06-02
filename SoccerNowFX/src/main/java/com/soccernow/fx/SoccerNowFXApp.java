package com.soccernow.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SoccerNowFXApp extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginScreen.fxml"));
    Scene scene = new Scene(root);
    primaryStage.setTitle("SoccerNow");
    primaryStage.setScene(scene);

    // Set minimum size for window
    primaryStage.setMinWidth(600);
    primaryStage.setMinHeight(400);

    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
