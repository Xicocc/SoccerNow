package com.soccernow.fx.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.soccernow.fx.dto.ChampionshipRegistrationDTO;
import com.soccernow.fx.util.AppWindowManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.format.DateTimeFormatter;
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

public class ChampionshipListController {

  @FXML private TableView<ChampionshipRegistrationDTO> tableChampionships;
  @FXML private TableColumn<ChampionshipRegistrationDTO, Long> colID;
  @FXML private TableColumn<ChampionshipRegistrationDTO, String> colName;
  @FXML private TableColumn<ChampionshipRegistrationDTO, String> colDescription;
  @FXML private TableColumn<ChampionshipRegistrationDTO, String> colStatus;
  @FXML private TableColumn<ChampionshipRegistrationDTO, String> colStartDate;
  @FXML private TableColumn<ChampionshipRegistrationDTO, String> colEndDate;
  @FXML private TableColumn<ChampionshipRegistrationDTO, String> colLocation;

  @FXML private Button btnAddChampionship;
  @FXML private Button btnEditChampionship;
  @FXML private Button btnDeleteChampionship;
  @FXML private Button btnBack;

  private final ObservableList<ChampionshipRegistrationDTO> championshipList =
      FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    colID.setCellValueFactory(new PropertyValueFactory<>("id"));
    colName.setCellValueFactory(new PropertyValueFactory<>("name"));
    colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

    colDescription.setCellValueFactory(
        cellData -> {
          String value = cellData.getValue().getDescription();
          return new javafx.beans.property.SimpleStringProperty(
              (value == null || value.trim().isEmpty()) ? "-" : value);
        });

    colLocation.setCellValueFactory(
        cellData -> {
          String value = cellData.getValue().getLocation();
          return new javafx.beans.property.SimpleStringProperty(
              (value == null || value.trim().isEmpty()) ? "-" : value);
        });

    // Format LocalDate to String for display
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    colStartDate.setCellValueFactory(
        cellData -> {
          var date = cellData.getValue().getStartDate();
          return new javafx.beans.property.SimpleStringProperty(
              date != null ? date.format(dateFormatter) : "-");
        });
    colEndDate.setCellValueFactory(
        cellData -> {
          var date = cellData.getValue().getEndDate();
          return new javafx.beans.property.SimpleStringProperty(
              date != null ? date.format(dateFormatter) : "-");
        });

    tableChampionships.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    tableChampionships.setItems(championshipList);

    fetchChampionshipsFromBackend();

    btnDeleteChampionship
        .disableProperty()
        .bind(tableChampionships.getSelectionModel().selectedItemProperty().isNull());
    btnEditChampionship
        .disableProperty()
        .bind(tableChampionships.getSelectionModel().selectedItemProperty().isNull());

    Platform.runLater(() -> btnBack.requestFocus());
  }

  private void fetchChampionshipsFromBackend() {
    String endpoint = "http://localhost:8080/api/championships";
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
      Gson gson =
          new GsonBuilder()
              .registerTypeAdapter(
                  java.time.LocalDate.class,
                  (com.google.gson.JsonDeserializer<java.time.LocalDate>)
                      (jsonElement, type, context) ->
                          java.time.LocalDate.parse(jsonElement.getAsString()))
              .registerTypeAdapter(
                  java.time.LocalDate.class,
                  (com.google.gson.JsonSerializer<java.time.LocalDate>)
                      (date, type, context) -> new com.google.gson.JsonPrimitive(date.toString()))
              .create();
      Type listType = new TypeToken<List<ChampionshipRegistrationDTO>>() {}.getType();
      List<ChampionshipRegistrationDTO> championships = gson.fromJson(json, listType);

      championshipList.clear();
      championshipList.addAll(championships);

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
  private void handleAddChampionship(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddChampionshipForm.fxml"));
      Parent root = loader.load();

      AddChampionshipFormController dialogController = loader.getController();
      dialogController.setChampionshipDataListener(
          (name, description, startDate, endDate, location) -> {
            addChampionshipToBackend(name, description, startDate, endDate, location);
          });

      Stage dialogStage = new Stage();
      dialogStage.setTitle("Add Championship");
      dialogStage.setScene(new Scene(root));
      dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
      dialogStage.setResizable(false);
      dialogStage.showAndWait();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void addChampionshipToBackend(
      String name,
      String description,
      java.time.LocalDate startDate,
      java.time.LocalDate endDate,
      String location) {
    String endpoint = "http://localhost:8080/api/championships";
    try {
      URI uri = URI.create(endpoint);
      URL url = uri.toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setDoOutput(true);

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
      ChampionshipRegistrationDTO dto = new ChampionshipRegistrationDTO();
      dto.setName(name);
      dto.setDescription(description);
      dto.setStartDate(startDate);
      dto.setEndDate(endDate);
      dto.setLocation(location);

      String jsonInputString = gson.toJson(dto);

      try (OutputStream os = conn.getOutputStream()) {
        byte[] input = jsonInputString.getBytes("utf-8");
        os.write(input, 0, input.length);
      }

      int responseCode = conn.getResponseCode();
      conn.disconnect();

      if (responseCode == 201 || responseCode == 200) {
        fetchChampionshipsFromBackend();
        Platform.runLater(
            () -> {
              Alert alert = new Alert(AlertType.INFORMATION);
              alert.setTitle("Championship Added");
              alert.setHeaderText(null);
              alert.setContentText("Championship added successfully!");
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
            alert.setTitle("Add Championship Failed");
            alert.setHeaderText(null);
            alert.setContentText("Failed to add championship: " + e.getMessage());
            alert.showAndWait();
          });
    }
  }

  @FXML
  private void handleEditChampionship(ActionEvent event) {
    ChampionshipRegistrationDTO selected = tableChampionships.getSelectionModel().getSelectedItem();
    if (selected == null) {
      Alert alert = new Alert(AlertType.WARNING);
      alert.setTitle("No Selection");
      alert.setHeaderText(null);
      alert.setContentText("Please select a championship to edit.");
      alert.showAndWait();
      return;
    }

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditChampionshipForm.fxml"));
      Parent root = loader.load();

      EditChampionshipFormController dialogController = loader.getController();
      dialogController.setChampionshipInfo(selected);
      dialogController.setOnChampionshipUpdated(this::fetchChampionshipsFromBackend);

      Stage dialogStage = new Stage();
      dialogStage.setTitle("Edit Championship");
      dialogStage.setScene(new Scene(root));
      dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
      dialogStage.setResizable(false);
      dialogStage.showAndWait();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void handleDeleteChampionship() {
    ChampionshipRegistrationDTO selected = tableChampionships.getSelectionModel().getSelectedItem();
    if (selected == null) {
      Alert alert = new Alert(AlertType.WARNING);
      alert.setTitle("No Selection");
      alert.setHeaderText(null);
      alert.setContentText("Please select a championship to delete.");
      alert.showAndWait();
      return;
    }
    // Optional: confirmation dialog
    Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
    confirmationAlert.setTitle("Confirm Delete");
    confirmationAlert.setHeaderText(null);
    confirmationAlert.setContentText(
        "Are you sure you want to delete championship: " + selected.getName() + "?");
    confirmationAlert
        .showAndWait()
        .ifPresent(
            response -> {
              if (response == ButtonType.OK) {
                int code = sendDeleteChampionship(selected.getId());
                if (code == 200 || code == 204) {
                  Alert confirmAlert = new Alert(AlertType.INFORMATION);
                  confirmAlert.setTitle("Championship Deleted");
                  confirmAlert.setHeaderText(null);
                  confirmAlert.setContentText("Championship deleted successfully.");
                  confirmAlert.showAndWait();
                  fetchChampionshipsFromBackend();
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

  private int sendDeleteChampionship(Long championshipId) {
    try {
      var client = java.net.http.HttpClient.newHttpClient();
      var request =
          java.net.http.HttpRequest.newBuilder()
              .uri(URI.create("http://localhost:8080/api/championships/" + championshipId))
              .DELETE()
              .header("Accept", "*/*")
              .build();
      var response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
      return response.statusCode();
    } catch (Exception e) {
      e.printStackTrace();
      return -1;
    }
  }
}
