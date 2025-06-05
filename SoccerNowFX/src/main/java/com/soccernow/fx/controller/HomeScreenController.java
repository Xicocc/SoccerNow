package com.soccernow.fx.controller;

import com.soccernow.fx.util.AppWindowManager;
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
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

    AppWindowManager.persistSize(stage);

    Parent playerListRoot = FXMLLoader.load(getClass().getResource("/fxml/PlayerList.fxml"));
    Scene playerListScene = new Scene(playerListRoot);

    stage.setScene(playerListScene);
    AppWindowManager.applySize(stage);
    stage.setTitle("Player List");
  }

  @FXML
  private void handleRefereesButton(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

    AppWindowManager.persistSize(stage);

    Parent refereeListRoot = FXMLLoader.load(getClass().getResource("/fxml/RefereeList.fxml"));
    Scene refereeListScene = new Scene(refereeListRoot);

    stage.setScene(refereeListScene);
    AppWindowManager.applySize(stage);
    stage.setTitle("Referee List");
  }

  @FXML
  private void handleTeamsButton(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

    AppWindowManager.persistSize(stage);

    Parent teamListRoot = FXMLLoader.load(getClass().getResource("/fxml/TeamList.fxml"));
    Scene teamListScene = new Scene(teamListRoot);

    stage.setScene(teamListScene);
    AppWindowManager.applySize(stage);
    stage.setTitle("Team List");
  }

  @FXML
  private void handleGamesButton(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

    AppWindowManager.persistSize(stage);

    Parent gamesListRoot = FXMLLoader.load(getClass().getResource("/fxml/GameList.fxml"));
    Scene gamesListScene = new Scene(gamesListRoot);

    stage.setScene(gamesListScene);
    AppWindowManager.applySize(stage);
    stage.setTitle("Games List");
  }

  @FXML
  private void handleChampionshipsButton(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

    AppWindowManager.persistSize(stage);

    Parent championshipListRoot =
        FXMLLoader.load(getClass().getResource("/fxml/ChampionshipList.fxml"));
    Scene championshipListScene = new Scene(championshipListRoot);

    stage.setScene(championshipListScene);
    AppWindowManager.applySize(stage);
    stage.setTitle("Championship List");
  }
}
