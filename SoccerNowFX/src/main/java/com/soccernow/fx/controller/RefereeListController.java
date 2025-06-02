package com.soccernow.fx.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soccernow.fx.dto.ReferereeRegistrationDTO;
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

public class RefereeListController {
  @FXML private TableView<ReferereeRegistrationDTO> tableReferees;
  @FXML private TableColumn<ReferereeRegistrationDTO, Number> colID;
  @FXML private TableColumn<ReferereeRegistrationDTO, String> colName;
  @FXML private TableColumn<ReferereeRegistrationDTO, Number> colAge;
  @FXML private TableColumn<ReferereeRegistrationDTO, Number> colGamesPart;

  @FXML private Button btnAddReferee;
  @FXML private Button btnEditReferee;
  @FXML private Button btnDeleteReferee;
  @FXML private Button btnBack;

  private final ObservableList<ReferereeRegistrationDTO> RefereeList =
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
      Type listType = new TypeToken<List<ReferereeRegistrationDTO>>() {}.getType();
      List<ReferereeRegistrationDTO> referees = gson.fromJson(json, listType);

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

        // Show success dialog on JavaFX thread
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
    System.out.println("Edit Referee button clicked");
  }

  @FXML
  private void handleDeleteReferee(ActionEvent event) {
    System.out.println("Delete Referee button clicked");
  }
}
