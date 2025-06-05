package com.soccernow.fx.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.soccernow.fx.dto.ChampionshipRegistrationDTO;
import com.soccernow.fx.dto.GameRegistrationDTO;
import com.soccernow.fx.util.AppWindowManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class GameListController {
  @FXML private TableView<GameRegistrationDTO> tableGames;
  @FXML private TableColumn<GameRegistrationDTO, Number> colId;
  @FXML private TableColumn<GameRegistrationDTO, String> colChampionship;
  @FXML private TableColumn<GameRegistrationDTO, String> colTeamA;
  @FXML private TableColumn<GameRegistrationDTO, String> colTeamB;
  @FXML private TableColumn<GameRegistrationDTO, String> colDate;
  @FXML private TableColumn<GameRegistrationDTO, String> colStatus;
  @FXML private TableColumn<GameRegistrationDTO, String> colScore;
  @FXML private TableColumn<GameRegistrationDTO, String> colReferee;
  @FXML private TableColumn<GameRegistrationDTO, String> colLocation;

  @FXML private Button btnAddGame;
  @FXML private Button btnEditGame;
  @FXML private Button btnCancelGame;
  @FXML private Button btnDeleteGame;
  @FXML private Button btnBack;

  private final ObservableList<GameRegistrationDTO> gameList = FXCollections.observableArrayList();
  private final Map<Long, String> championshipNameMap = new HashMap<>();

  @FXML
  public void initialize() {
    fetchChampionships();
    fetchGamesFromBackend();

    btnCancelGame
        .disableProperty()
        .bind(tableGames.getSelectionModel().selectedItemProperty().isNull());

    colId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));

    colChampionship.setCellValueFactory(
        cellData -> {
          Long champId = cellData.getValue().getChampionshipId();
          String champStr = "-";
          if (champId != null && champId != 0 && championshipNameMap.containsKey(champId)) {
            champStr = championshipNameMap.get(champId) + " (ID: " + champId + ")";
          } else if (champId != null && champId != 0) {
            champStr = "(ID: " + champId + ")";
          }
          return new SimpleStringProperty(champStr);
        });

    colTeamA.setCellValueFactory(
        cellData -> {
          String name = cellData.getValue().getHomeTeamName();
          Long id = cellData.getValue().getHomeTeamId();
          return new SimpleStringProperty(
              (name != null && id != null) ? name + " (ID: " + id + ")" : "-");
        });

    colTeamB.setCellValueFactory(
        cellData -> {
          String name = cellData.getValue().getAwayTeamName();
          Long id = cellData.getValue().getAwayTeamId();
          return new SimpleStringProperty(
              (name != null && id != null) ? name + " (ID: " + id + ")" : "-");
        });

    colReferee.setCellValueFactory(
        cellData -> {
          String name = cellData.getValue().getRefereeName();
          Long id = cellData.getValue().getRefereeId();
          return new SimpleStringProperty(
              (name != null && id != null) ? name + " (ID: " + id + ")" : "-");
        });

    colScore.setCellValueFactory(
        cellData -> {
          Integer home = cellData.getValue().getHomeScore();
          Integer away = cellData.getValue().getAwayScore();
          if (home == null || away == null) return new SimpleStringProperty("-");
          return new SimpleStringProperty(home + " - " + away);
        });

    colDate.setCellValueFactory(
        cellData -> {
          String gameTimeStr = cellData.getValue().getGameTime();
          if (gameTimeStr == null || gameTimeStr.isEmpty()) {
            return new SimpleStringProperty("-");
          }
          try {
            java.time.LocalDateTime ldt = java.time.LocalDateTime.parse(gameTimeStr);
            String formatted =
                ldt.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            return new SimpleStringProperty(formatted);
          } catch (Exception e) {
            return new SimpleStringProperty(gameTimeStr);
          }
        });

    colStatus.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
    colLocation.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));

    setDashOnlyForRows(colChampionship);
    setDashOnlyForRows(colTeamA);
    setDashOnlyForRows(colTeamB);
    setDashOnlyForRows(colReferee);
    setDashOnlyForRows(colScore);
    setDashOnlyForRows(colDate);
    setDashOnlyForRows(colStatus);
    setDashOnlyForRows(colLocation);

    tableGames.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    tableGames.setItems(gameList);

    btnDeleteGame
        .disableProperty()
        .bind(tableGames.getSelectionModel().selectedItemProperty().isNull());
    btnEditGame
        .disableProperty()
        .bind(tableGames.getSelectionModel().selectedItemProperty().isNull());

    Platform.runLater(() -> btnBack.requestFocus());
  }

  private void setDashOnlyForRows(TableColumn<GameRegistrationDTO, String> col) {
    col.setCellFactory(
        tc ->
            new TableCell<>() {
              @Override
              protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : (item == null || item.isEmpty()) ? "-" : item);
              }
            });
  }

  private void fetchChampionships() {
    try {
      String endpoint = "http://localhost:8080/api/championships";
      URI uri = URI.create(endpoint);
      URL url = uri.toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Accept", "application/json");

      BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      StringBuilder jsonBuilder = new StringBuilder();
      String output;
      while ((output = br.readLine()) != null) {
        jsonBuilder.append(output);
      }
      conn.disconnect();

      Gson gson =
          new GsonBuilder()
              .registerTypeAdapter(
                  java.time.LocalDate.class,
                  (com.google.gson.JsonDeserializer<java.time.LocalDate>)
                      (json, type, context) -> java.time.LocalDate.parse(json.getAsString()))
              .registerTypeAdapter(
                  java.time.LocalDate.class,
                  (com.google.gson.JsonSerializer<java.time.LocalDate>)
                      (date, type, context) -> new com.google.gson.JsonPrimitive(date.toString()))
              .create();
      Type listType = new TypeToken<List<ChampionshipRegistrationDTO>>() {}.getType();
      List<ChampionshipRegistrationDTO> championships =
          gson.fromJson(jsonBuilder.toString(), listType);

      synchronized (championshipNameMap) {
        championshipNameMap.clear();
        for (ChampionshipRegistrationDTO c : championships) {
          championshipNameMap.put(c.getId(), c.getName());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void fetchGamesFromBackend() {
    try {
      String endpoint = "http://localhost:8080/api/games";
      URI uri = URI.create(endpoint);
      URL url = uri.toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Accept", "application/json");

      if (conn.getResponseCode() != 200) {
        throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
      }

      BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      StringBuilder jsonBuilder = new StringBuilder();
      String output;
      while ((output = br.readLine()) != null) {
        jsonBuilder.append(output);
      }
      conn.disconnect();

      Gson gson =
          new GsonBuilder()
              .registerTypeAdapter(
                  java.time.LocalDate.class,
                  (com.google.gson.JsonDeserializer<java.time.LocalDate>)
                      (json, type, context) -> java.time.LocalDate.parse(json.getAsString()))
              .registerTypeAdapter(
                  java.time.LocalDate.class,
                  (com.google.gson.JsonSerializer<java.time.LocalDate>)
                      (date, type, context) -> new com.google.gson.JsonPrimitive(date.toString()))
              .create();
      Type listType = new TypeToken<List<GameRegistrationDTO>>() {}.getType();
      List<GameRegistrationDTO> games = gson.fromJson(jsonBuilder.toString(), listType);

      Platform.runLater(
          () -> {
            gameList.clear();
            gameList.addAll(games);
          });
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
  private void handleAddGame(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddGameForm.fxml"));
      Parent root = loader.load();

      Stage dialogStage = new Stage();
      dialogStage.setTitle("Add Game");
      dialogStage.setScene(new Scene(root));
      dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
      dialogStage.setResizable(false);
      dialogStage.showAndWait();

      // Refresh data after dialog closes
      fetchChampionships();
      fetchGamesFromBackend();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void handleCancelGame() {
    GameRegistrationDTO selected = tableGames.getSelectionModel().getSelectedItem();
    if (selected == null) {
      Alert alert = new Alert(AlertType.WARNING);
      alert.setTitle("No Selection");
      alert.setHeaderText(null);
      alert.setContentText("Please select a game to cancel.");
      alert.showAndWait();
      return;
    }
    Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
    confirmationAlert.setTitle("Confirm Cancel");
    confirmationAlert.setHeaderText(null);
    confirmationAlert.setContentText(
        "Are you sure you want to cancel game: "
            + selected.getHomeTeamName()
            + " vs "
            + selected.getAwayTeamName()
            + " (ID: "
            + selected.getId()
            + ")?");
    confirmationAlert
        .showAndWait()
        .ifPresent(
            response -> {
              if (response == ButtonType.OK) {
                int code = sendCancelGame(selected.getId());
                if (code == 200 || code == 204) {
                  Alert confirmAlert = new Alert(AlertType.INFORMATION);
                  confirmAlert.setTitle("Game Canceled");
                  confirmAlert.setHeaderText(null);
                  confirmAlert.setContentText("Game canceled successfully.");
                  confirmAlert.showAndWait();
                  fetchGamesFromBackend();
                } else {
                  Alert errorAlert = new Alert(AlertType.ERROR);
                  errorAlert.setTitle("Error");
                  errorAlert.setHeaderText(null);
                  errorAlert.setContentText("Cancel failed! (Error code: " + code + ")");
                  errorAlert.showAndWait();
                }
              }
            });
  }

  private int sendCancelGame(Long gameId) {
    try {
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create("http://localhost:8080/api/games/" + gameId + "/cancel"))
              .method("PATCH", HttpRequest.BodyPublishers.noBody())
              .header("Accept", "*/*")
              .build();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      return response.statusCode();
    } catch (Exception e) {
      e.printStackTrace();
      return -1;
    }
  }

  @FXML
  private void handleEditGame(ActionEvent event) {
    GameRegistrationDTO selected = tableGames.getSelectionModel().getSelectedItem();
    if (selected == null) {
      Alert alert = new Alert(AlertType.WARNING);
      alert.setTitle("No Selection");
      alert.setHeaderText(null);
      alert.setContentText("Please select a game to edit.");
      alert.showAndWait();
      return;
    }

    try {
      FXMLLoader loader =
          new FXMLLoader(getClass().getResource("/fxml/EditGameStatsForm.fxml")); // UPDATED!
      Parent root = loader.load();

      EditGameStatsFormController dialogController = loader.getController();
      dialogController.setGameInfo(selected);
      dialogController.setOnGameUpdated(this::fetchGamesFromBackend);

      Stage dialogStage = new Stage();
      dialogStage.setTitle("Edit Game Score");
      dialogStage.setScene(new Scene(root));
      dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
      dialogStage.setResizable(false);
      dialogStage.showAndWait();

      fetchGamesFromBackend();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void handleDeleteGame() {
    GameRegistrationDTO selected = tableGames.getSelectionModel().getSelectedItem();
    if (selected == null) {
      Alert alert = new Alert(AlertType.WARNING);
      alert.setTitle("No Selection");
      alert.setHeaderText(null);
      alert.setContentText("Please select a game to delete.");
      alert.showAndWait();
      return;
    }
    Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
    confirmationAlert.setTitle("Confirm Delete");
    confirmationAlert.setHeaderText(null);
    confirmationAlert.setContentText(
        "Are you sure you want to delete game: "
            + selected.getHomeTeamName()
            + " vs "
            + selected.getAwayTeamName()
            + " (ID: "
            + selected.getId()
            + ")?");
    confirmationAlert
        .showAndWait()
        .ifPresent(
            response -> {
              if (response == ButtonType.OK) {
                int code = sendDeleteGame(selected.getId());
                if (code == 200 || code == 204) {
                  Alert confirmAlert = new Alert(AlertType.INFORMATION);
                  confirmAlert.setTitle("Game Deleted");
                  confirmAlert.setHeaderText(null);
                  confirmAlert.setContentText("Game deleted successfully.");
                  confirmAlert.showAndWait();
                  fetchGamesFromBackend();
                } else {
                  Alert errorAlert = new Alert(AlertType.ERROR);
                  errorAlert.setTitle("Error");
                  errorAlert.setHeaderText(null);
                  errorAlert.setContentText("Deletion failed! (Error code: " + code + ")");
                  errorAlert.showAndWait();
                }
              }
            });
  }

  private int sendDeleteGame(Long gameId) {
    try {
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create("http://localhost:8080/api/games/" + gameId))
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
