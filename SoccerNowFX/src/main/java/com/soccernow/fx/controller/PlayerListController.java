package com.soccernow.fx.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soccernow.fx.dto.PlayerRegistrationDTO;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class PlayerListController {
  @FXML private TableView<PlayerRegistrationDTO> tablePlayers;
  @FXML private TableColumn<PlayerRegistrationDTO, Number> colID;
  @FXML private TableColumn<PlayerRegistrationDTO, String> colName;
  @FXML private TableColumn<PlayerRegistrationDTO, Number> colAge;
  @FXML private TableColumn<PlayerRegistrationDTO, String> colPosition;
  @FXML private TableColumn<PlayerRegistrationDTO, Number> colGamesPlayed;
  @FXML private TableColumn<PlayerRegistrationDTO, Number> colGoalsScored;
  @FXML private TableColumn<PlayerRegistrationDTO, Number> colYellowCards;
  @FXML private TableColumn<PlayerRegistrationDTO, Number> colRedCards;

  @FXML private Button btnAddPlayer;
  @FXML private Button btnEditPlayer;
  @FXML private Button btnDeletePlayer;

  private final ObservableList<PlayerRegistrationDTO> playerList =
      FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    colID.setCellValueFactory(new PropertyValueFactory<>("id"));
    colName.setCellValueFactory(new PropertyValueFactory<>("name"));
    colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
    colPosition.setCellValueFactory(new PropertyValueFactory<>("preferredPosition"));
    colGamesPlayed.setCellValueFactory(new PropertyValueFactory<>("gamesPlayed"));
    colGoalsScored.setCellValueFactory(new PropertyValueFactory<>("goalsScored"));
    colYellowCards.setCellValueFactory(new PropertyValueFactory<>("yellowCards"));
    colRedCards.setCellValueFactory(new PropertyValueFactory<>("redCards"));

    tablePlayers.setItems(playerList);

    fetchPlayersFromBackend();
  }

  private void fetchPlayersFromBackend() {
    String endpoint = "http://localhost:8080/api/players";
    try {
      URI uri = URI.create(endpoint);
      URL url = uri.toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Accept", "application/json");

      if (conn.getResponseCode() != 200) {
        throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
      }

      BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
      StringBuilder jsonBuilder = new StringBuilder();
      String output;
      while ((output = br.readLine()) != null) {
        jsonBuilder.append(output);
      }
      conn.disconnect();

      String json = jsonBuilder.toString();
      Gson gson = new Gson();
      Type listType = new TypeToken<List<PlayerRegistrationDTO>>() {}.getType();
      List<PlayerRegistrationDTO> players = gson.fromJson(json, listType);

      playerList.clear();
      playerList.addAll(players);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void handleAddPlayer(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddPlayerForm.fxml"));
      Parent root = loader.load();

      AddPlayerFormController dialogController = loader.getController();
      dialogController.setPlayerDataListener(
          (name, age, position) -> {
            addPlayerToBackend(name, age, position);
          });

      Stage dialogStage = new Stage();
      dialogStage.setTitle("Add Player");
      dialogStage.setScene(new Scene(root));
      dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
      dialogStage.setResizable(false);
      dialogStage.showAndWait();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void addPlayerToBackend(String name, int age, String position) {
    String endpoint = "http://localhost:8080/api/auth/register/player";
    try {
      URI uri = URI.create(endpoint);
      URL url = uri.toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setDoOutput(true);

      com.google.gson.Gson gson = new com.google.gson.Gson();
      com.soccernow.fx.dto.PlayerRegistrationDTO player =
          new com.soccernow.fx.dto.PlayerRegistrationDTO();
      player.setName(name);
      player.setAge(age);
      player.setPreferredPosition(position);
      String jsonInputString = gson.toJson(player);

      try (OutputStream os = conn.getOutputStream()) {
        byte[] input = jsonInputString.getBytes("utf-8");
        os.write(input, 0, input.length);
      }

      int responseCode = conn.getResponseCode();
      conn.disconnect();

      if (responseCode == 201 || responseCode == 200) {
        fetchPlayersFromBackend();

        // Show success dialog on JavaFX thread
        Platform.runLater(
            () -> {
              Alert alert = new Alert(AlertType.INFORMATION);
              alert.setTitle("Player Added");
              alert.setHeaderText(null);
              alert.setContentText("Player added successfully!");
              alert.showAndWait();
            });
      } else {
        throw new RuntimeException("Failed : HTTP error code : " + responseCode);
      }
    } catch (Exception e) {
      e.printStackTrace();
      Platform.runLater(
          () -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Add Player Failed");
            alert.setHeaderText(null);
            alert.setContentText("Failed to add player: " + e.getMessage());
            alert.showAndWait();
          });
    }
  }

  @FXML
  private void handleEditPlayer(ActionEvent event) {
    PlayerRegistrationDTO selected = tablePlayers.getSelectionModel().getSelectedItem();
    if (selected == null) {
      // Optionally, alert user to select a player first
      javafx.scene.control.Alert alert =
          new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
      alert.setTitle("No Selection");
      alert.setHeaderText(null);
      alert.setContentText("Please select a player to edit.");
      alert.showAndWait();
      return;
    }

    try {
      javafx.fxml.FXMLLoader loader =
          new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/EditPlayerStatsForm.fxml"));
      javafx.scene.Parent root = loader.load();

      com.soccernow.fx.controller.EditPlayerStatsFormController dialogController =
          loader.getController();
      dialogController.setPlayerId(selected.getId());
      dialogController.setOnStatsUpdated(this::fetchPlayersFromBackend);

      javafx.stage.Stage dialogStage = new javafx.stage.Stage();
      dialogStage.setTitle("Edit Player Stats");
      dialogStage.setScene(new javafx.scene.Scene(root));
      dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
      dialogStage.setResizable(false);
      dialogStage.showAndWait();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void handleEditPlayer() {
    PlayerRegistrationDTO selected = tablePlayers.getSelectionModel().getSelectedItem();
    if (selected == null) return;

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditPlayerStatsForm.fxml"));
      Parent root = loader.load();

      EditPlayerStatsFormController dialogController = loader.getController();
      dialogController.setPlayerId(selected.getId());
      dialogController.setOnStatsUpdated(this::fetchPlayersFromBackend);

      Stage dialogStage = new Stage();
      dialogStage.setTitle("Edit Player Stats");
      dialogStage.setScene(new Scene(root));
      dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
      dialogStage.setResizable(false);
      dialogStage.showAndWait();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void handleDeletePlayer(ActionEvent event) {
    // Here you can open a dialog to input player details, then send to backend.
    // For now, just print to console.
    System.out.println("Delete Player clicked! Implement dialog/form here.");
    // After deleting, call fetchPlayersFromBackend() to refresh table.
  }
}
