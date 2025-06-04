package com.soccernow.fx;

import com.soccernow.fx.util.AppWindowManager;
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
    primaryStage.setMinWidth(602);
    primaryStage.setMinHeight(430);

    primaryStage.setMaximized(true);
    AppWindowManager.persistSize(primaryStage);

    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
