package com.soccernow.fx.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomeScreenController {

  @FXML
  private void handlePlayersButton(ActionEvent event) throws IOException {
    Parent playerListRoot = FXMLLoader.load(getClass().getResource("/fxml/PlayerList.fxml"));
    Scene playerListScene = new Scene(playerListRoot);

    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(playerListScene);
  }

  @FXML
  private void handleTeamsButton(ActionEvent event) {
    // TODO: Implement navigation
  }

  @FXML
  private void handleMatchesButton(ActionEvent event) {
    // TODO: Implement navigation
  }
}
