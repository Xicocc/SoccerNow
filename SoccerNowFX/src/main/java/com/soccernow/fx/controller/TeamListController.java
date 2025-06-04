package com.soccernow.fx.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soccernow.fx.dto.PlayerRegistrationDTO;
import com.soccernow.fx.dto.TeamRegistrationDTO;
import com.soccernow.fx.util.AppWindowManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class TeamListController {

  @FXML private TableView<TeamRegistrationDTO> tableTeams;
  @FXML private TableColumn<TeamRegistrationDTO, Number> colID;
  @FXML private TableColumn<TeamRegistrationDTO, String> colName;
  @FXML private TableColumn<TeamRegistrationDTO, Number> colWins;
  @FXML private TableColumn<TeamRegistrationDTO, Number> colNumPlayers;
  @FXML private TableColumn<TeamRegistrationDTO, String> colPlayers;

  @FXML private Button btnBack;
  @FXML private Button btnAddTeam;
  @FXML private Button btnAddPlayerToTeam;
  @FXML private Button btnEditTeam;
  @FXML private Button btnDeleteTeam;
  @FXML private Button btnRemovePlayerFromTeam;

  private final ObservableList<TeamRegistrationDTO> teamList = FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    colID.setCellValueFactory(new PropertyValueFactory<>("id"));
    colName.setCellValueFactory(new PropertyValueFactory<>("name"));
    colWins.setCellValueFactory(new PropertyValueFactory<>("wins"));
    colNumPlayers.setCellValueFactory(
        cellData -> {
          TeamRegistrationDTO team = cellData.getValue();
          int numPlayers = (team.getPlayers() != null) ? team.getPlayers().size() : 0;
          return new javafx.beans.property.SimpleIntegerProperty(numPlayers);
        });
    colPlayers.setCellValueFactory(
        cellData -> {
          TeamRegistrationDTO team = cellData.getValue();
          if (team.getPlayers() == null || team.getPlayers().isEmpty()) {
            return new javafx.beans.property.SimpleStringProperty("-");
          } else {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (var player : team.getPlayers()) {
              if (i > 0) sb.append("; ");
              sb.append(player.getId()).append(":").append(player.getName());
              i++;
              if (i % 3 == 0) sb.append("\n");
            }
            return new javafx.beans.property.SimpleStringProperty(sb.toString());
          }
        });

    // This is the key part:
    colPlayers.setCellFactory(
        tc ->
            new TableCell<TeamRegistrationDTO, String>() {
              private final Label label = new Label();

              {
                label.setWrapText(true);
                label.setAlignment(javafx.geometry.Pos.TOP_LEFT);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setAlignment(javafx.geometry.Pos.TOP_LEFT);
                // Bind the label width to the column width
                label.maxWidthProperty().bind(tc.widthProperty().subtract(12));
                label.prefWidthProperty().bind(tc.widthProperty().subtract(12));
              }

              @Override
              protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                  setGraphic(null);
                  setPrefHeight(Control.USE_COMPUTED_SIZE);
                } else {
                  label.setText(item);
                  setGraphic(label);
                  // Defer height adjustment until after layout pass
                  javafx.application.Platform.runLater(
                      () -> {
                        double needed = label.prefHeight(label.getWidth());
                        label.setMinHeight(needed);
                        label.setPrefHeight(needed);
                        setMinHeight(needed);
                        setPrefHeight(needed);
                      });
                }
              }
            });

    tableTeams.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    tableTeams.setItems(teamList);

    btnEditTeam
        .disableProperty()
        .bind(tableTeams.getSelectionModel().selectedItemProperty().isNull());
    btnDeleteTeam
        .disableProperty()
        .bind(tableTeams.getSelectionModel().selectedItemProperty().isNull());
    btnAddPlayerToTeam
        .disableProperty()
        .bind(tableTeams.getSelectionModel().selectedItemProperty().isNull());
    btnRemovePlayerFromTeam
        .disableProperty()
        .bind(tableTeams.getSelectionModel().selectedItemProperty().isNull());

    fetchTeamsFromBackend();

    Platform.runLater(
        () -> {
          btnBack.requestFocus();
        });
  }

  private void fetchTeamsFromBackend() {
    String endpoint = "http://localhost:8080/api/teams";
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
      Type listType = new TypeToken<List<TeamRegistrationDTO>>() {}.getType();
      List<TeamRegistrationDTO> teams = gson.fromJson(json, listType);

      teamList.clear();
      teamList.addAll(teams);
      javafx.application.Platform.runLater(() -> tableTeams.refresh());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void handleBack(ActionEvent event) {
    try {
      Stage stage = (Stage) btnBack.getScene().getWindow();
      AppWindowManager.persistSize(stage);

      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/HomeScreen.fxml"));
      Parent homeRoot = loader.load();
      Scene homeScene = new Scene(homeRoot);

      stage.setScene(homeScene);
      AppWindowManager.applySize(stage);
      stage.setTitle("Home Screen");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void handleAddTeam(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddTeamForm.fxml"));
      Parent root = loader.load();

      AddTeamFormController dialogController = loader.getController();
      dialogController.setTeamDataListener(
          (name) -> {
            addTeamToBackend(name);
          });

      Stage dialogStage = new Stage();
      dialogStage.setTitle("Add Team");
      dialogStage.setScene(new Scene(root));
      dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
      dialogStage.setResizable(false);
      dialogStage.showAndWait();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void addTeamToBackend(String name) {
    String endpoint = "http://localhost:8080/api/teams";
    try {
      URI uri = URI.create(endpoint);
      URL url = uri.toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setDoOutput(true);

      com.google.gson.Gson gson = new com.google.gson.Gson();
      com.soccernow.fx.dto.TeamRegistrationDTO team =
          new com.soccernow.fx.dto.TeamRegistrationDTO();
      team.setName(name);
      String jsonInputString = gson.toJson(team);

      try (OutputStream os = conn.getOutputStream()) {
        byte[] input = jsonInputString.getBytes("utf-8");
        os.write(input, 0, input.length);
      }

      int responseCode = conn.getResponseCode();
      conn.disconnect();

      if (responseCode == 201 || responseCode == 200) {
        fetchTeamsFromBackend();

        // Show success dialog on JavaFX thread
        Platform.runLater(
            () -> {
              Alert alert = new Alert(AlertType.INFORMATION);
              alert.setTitle("Team Added");
              alert.setHeaderText(null);
              alert.setContentText("Team added successfully!");
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
            alert.setTitle("Add Team Failed");
            alert.setHeaderText(null);
            alert.setContentText("Failed to add team: " + e.getMessage());
            alert.showAndWait();
          });
    }
  }

  @FXML
  private void handleAddPlayerToTeam() {
    TeamRegistrationDTO selectedTeam = tableTeams.getSelectionModel().getSelectedItem();
    if (selectedTeam == null) return;

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddPlayerToTeam.fxml"));
      Parent root = loader.load();
      AddPlayerToTeamController controller = loader.getController();

      // Fetch all players from backend
      List<PlayerRegistrationDTO> allPlayers = fetchAllPlayers();
      controller.setAvailablePlayers(allPlayers);
      controller.setTeamId(selectedTeam.getId());
      controller.setOnPlayersAdded(this::fetchTeamsFromBackend);

      Stage dialogStage = new Stage();
      dialogStage.setTitle("Add Player to Team");
      dialogStage.setScene(new Scene(root));
      dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
      dialogStage.setResizable(false);
      dialogStage.showAndWait();

      fetchTeamsFromBackend();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // Helper method inside TeamListController to fetch all players
  private List<PlayerRegistrationDTO> fetchAllPlayers() {
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
      return gson.fromJson(json, listType);
    } catch (Exception e) {
      e.printStackTrace();
      return java.util.Collections.emptyList();
    }
  }

  @FXML
  private void handleRemovePlayerFromTeam() {
    TeamRegistrationDTO selectedTeam = tableTeams.getSelectionModel().getSelectedItem();
    if (selectedTeam == null
        || selectedTeam.getPlayers() == null
        || selectedTeam.getPlayers().isEmpty()) {
      Alert alert =
          new Alert(Alert.AlertType.WARNING, "No players to remove in this team.", ButtonType.OK);
      alert.setTitle("No Players");
      alert.setHeaderText(null);
      alert.showAndWait();
      return;
    }
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RemovePlayerFromTeam.fxml"));
      Parent root = loader.load();
      RemovePlayerFromTeamController controller = loader.getController();

      // Only show players already on the team
      controller.setPlayersInTeam(selectedTeam.getPlayers());
      controller.setTeamId(selectedTeam.getId());
      controller.setOnPlayersRemoved(this::fetchTeamsFromBackend);

      Stage dialogStage = new Stage();
      dialogStage.setTitle("Remove Player from Team");
      dialogStage.setScene(new Scene(root));
      dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
      dialogStage.setResizable(false);
      dialogStage.showAndWait();

      fetchTeamsFromBackend();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void handleEditTeam(ActionEvent event) {
    TeamRegistrationDTO selected = tableTeams.getSelectionModel().getSelectedItem();
    if (selected == null) {
      javafx.scene.control.Alert alert =
          new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
      alert.setTitle("No Selection");
      alert.setHeaderText(null);
      alert.setContentText("Please select a team to edit.");
      alert.showAndWait();
      return;
    }

    try {
      javafx.fxml.FXMLLoader loader =
          new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/EditTeamStatsForm.fxml"));
      javafx.scene.Parent root = loader.load();

      com.soccernow.fx.controller.EditTeamStatsFormController dialogController =
          loader.getController();
      dialogController.setTeamInfo(
          selected.getId() != null ? selected.getId().longValue() : 0L,
          selected.getName(),
          selected.getWins() != null ? selected.getWins().longValue() : 0L);
      dialogController.setOnStatsUpdated(this::fetchTeamsFromBackend);

      javafx.stage.Stage dialogStage = new javafx.stage.Stage();
      dialogStage.setTitle("Edit Team Stats");
      dialogStage.setScene(new javafx.scene.Scene(root));
      dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
      dialogStage.setResizable(false);
      dialogStage.showAndWait();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void handleDeleteTeam() {
    TeamRegistrationDTO selected = tableTeams.getSelectionModel().getSelectedItem();
    if (selected == null) {
      javafx.scene.control.Alert alert =
          new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
      alert.setTitle("No Selection");
      alert.setHeaderText(null);
      alert.setContentText("Please select a team to delete.");
      alert.showAndWait();
      return;
    }
    javafx.scene.control.Alert confirmationAlert =
        new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
    confirmationAlert.setTitle("Confirm Delete");
    confirmationAlert.setHeaderText(null);
    confirmationAlert.setContentText(
        "Are you sure you want to delete team: "
            + selected.getName()
            + " (ID: "
            + selected.getId()
            + ")?");
    confirmationAlert
        .showAndWait()
        .ifPresent(
            response -> {
              if (response == ButtonType.OK) {
                int code = sendDeleteTeam(selected.getId(), selected.getName());
                if (code == 200 || code == 204) {
                  javafx.scene.control.Alert confirmAlert =
                      new javafx.scene.control.Alert(
                          javafx.scene.control.Alert.AlertType.INFORMATION);
                  confirmAlert.setTitle("Team Deleted");
                  confirmAlert.setHeaderText(null);
                  confirmAlert.setContentText("Team deleted successfully.");
                  confirmAlert.showAndWait();
                  fetchTeamsFromBackend();
                } else {
                  javafx.scene.control.Alert errorAlert =
                      new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                  errorAlert.setTitle("Error");
                  errorAlert.setHeaderText(null);
                  errorAlert.setContentText("Deletion failed! (Error code: " + code + ")");
                  errorAlert.showAndWait();
                }
              }
            });
  }

  private int sendDeleteTeam(Long teamId, String teamName) {
    try {
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create("http://localhost:8080/api/teams/" + teamId + "/" + teamName))
              .DELETE()
              .header("Accept", "*/*")
              .build();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      return response.statusCode();
    } catch (Exception e) {
      e.printStackTrace();
      return -1;
    }
  }
}
