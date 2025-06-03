package com.soccernow.fx.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soccernow.fx.dto.RefereeRegistrationDTO;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class RefereeListController {
  @FXML private TableView<RefereeRegistrationDTO> tableReferees;
  @FXML private TableColumn<RefereeRegistrationDTO, Number> colID;
  @FXML private TableColumn<RefereeRegistrationDTO, String> colName;
  @FXML private TableColumn<RefereeRegistrationDTO, Number> colAge;
  @FXML private TableColumn<RefereeRegistrationDTO, Number> colGamesPart;

  @FXML private Button btnAddReferee;
  @FXML private Button btnEditReferee;
  @FXML private Button btnDeleteReferee;
  @FXML private Button btnBack;

  private final ObservableList<RefereeRegistrationDTO> RefereeList =
      FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    colID.setCellValueFactory(new PropertyValueFactory<>("id"));
    colName.setCellValueFactory(new PropertyValueFactory<>("name"));
    colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
    colGamesPart.setCellValueFactory(new PropertyValueFactory<>("gamesParticipated"));

    tableReferees.setItems(RefereeList);

    fetchRefereesFromBackend();

    btnDeleteReferee
        .disableProperty()
        .bind(tableReferees.getSelectionModel().selectedItemProperty().isNull());
    btnEditReferee
        .disableProperty()
        .bind(tableReferees.getSelectionModel().selectedItemProperty().isNull());
  }

  private void fetchRefereesFromBackend() {
    String endpoint = "http://localhost:8080/api/referees";
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
      Type listType = new TypeToken<List<RefereeRegistrationDTO>>() {}.getType();
      List<RefereeRegistrationDTO> referees = gson.fromJson(json, listType);

      RefereeList.clear();
      RefereeList.addAll(referees);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void handleBack(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/HomeScreen.fxml"));
      Parent homeRoot = loader.load();
      Scene homeScene = new Scene(homeRoot);
      Stage stage = (Stage) btnBack.getScene().getWindow();
      stage.setScene(homeScene);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void handleAddReferee(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddRefereeForm.fxml"));
      Parent root = loader.load();

      AddRefereeFormController dialogController = loader.getController();
      dialogController.setRefereeDataListener(
          (name, age) -> {
            addRefereeToBackend(name, age);
          });

      Stage dialogStage = new Stage();
      dialogStage.setTitle("Add Referee");
      dialogStage.setScene(new Scene(root));
      dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
      dialogStage.setResizable(false);
      dialogStage.showAndWait();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void addRefereeToBackend(String name, int age) {
    String endpoint = "http://localhost:8080/api/auth/register/referee";
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
      String jsonInputString = gson.toJson(player);

      try (OutputStream os = conn.getOutputStream()) {
        byte[] input = jsonInputString.getBytes("utf-8");
        os.write(input, 0, input.length);
      }

      int responseCode = conn.getResponseCode();
      conn.disconnect();

      if (responseCode == 201 || responseCode == 200) {
        fetchRefereesFromBackend();

        // Show success dialog
        Platform.runLater(
            () -> {
              Alert alert = new Alert(AlertType.INFORMATION);
              alert.setTitle("Referee Added");
              alert.setHeaderText(null);
              alert.setContentText("Referee added successfully!");
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
            alert.setTitle("Add Referee Failed");
            alert.setHeaderText(null);
            alert.setContentText("Failed to add referee: " + e.getMessage());
            alert.showAndWait();
          });
    }
  }

  @FXML
  private void handleEditReferee(ActionEvent event) {
    RefereeRegistrationDTO selected = tableReferees.getSelectionModel().getSelectedItem();
    if (selected == null) {
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
          new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/EditRefereeStatsForm.fxml"));
      javafx.scene.Parent root = loader.load();

      com.soccernow.fx.controller.EditRefereeStatsFormController dialogController =
          loader.getController();
      dialogController.setRefereeInfo(
          selected.getId() != null ? selected.getId().longValue() : 0L,
          selected.getName(),
          selected.getGamesParticipated() != null
              ? Long.parseLong(selected.getGamesParticipated())
              : 0L);
      dialogController.setOnStatsUpdated(this::fetchRefereesFromBackend);

      javafx.stage.Stage dialogStage = new javafx.stage.Stage();
      dialogStage.setTitle("Edit Referee Stats");
      dialogStage.setScene(new javafx.scene.Scene(root));
      dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
      dialogStage.setResizable(false);
      dialogStage.showAndWait();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void handleDeleteReferee() {
    RefereeRegistrationDTO selected = tableReferees.getSelectionModel().getSelectedItem();
    if (selected == null) {
      javafx.scene.control.Alert alert =
          new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
      alert.setTitle("No Selection");
      alert.setHeaderText(null);
      alert.setContentText("Please select a referee to delete.");
      alert.showAndWait();
      return;
    }
    javafx.scene.control.Alert confirmationAlert =
        new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
    confirmationAlert.setTitle("Confirm Delete");
    confirmationAlert.setHeaderText(null);
    confirmationAlert.setContentText(
        "Are you sure you want to delete referee: "
            + selected.getName()
            + " (ID: "
            + selected.getId()
            + ")?");
    confirmationAlert
        .showAndWait()
        .ifPresent(
            response -> {
              if (response == ButtonType.OK) {
                int code = sendDeleteReferee(selected.getId());
                if (code == 200 || code == 204) {
                  javafx.scene.control.Alert confirmAlert =
                      new javafx.scene.control.Alert(
                          javafx.scene.control.Alert.AlertType.INFORMATION);
                  confirmAlert.setTitle("Referee Deleted");
                  confirmAlert.setHeaderText(null);
                  confirmAlert.setContentText("Referee deleted successfully.");
                  confirmAlert.showAndWait();
                  fetchRefereesFromBackend();
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

  private int sendDeleteReferee(Long refereeId) {
    try {
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create("http://localhost:8080/api/users/" + refereeId))
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
