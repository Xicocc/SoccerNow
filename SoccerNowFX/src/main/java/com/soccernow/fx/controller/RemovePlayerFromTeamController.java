package com.soccernow.fx.controller;

import com.soccernow.fx.dto.PlayerRegistrationDTO;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RemovePlayerFromTeamController {

  @FXML private TableView<PlayerRegistrationDTO> tablePlayers;
  @FXML private TableColumn<PlayerRegistrationDTO, Number> colId;
  @FXML private TableColumn<PlayerRegistrationDTO, String> colName;
  @FXML private TableColumn<PlayerRegistrationDTO, Number> colAge;
  @FXML private Button btnRemove;
  @FXML private Button btnCancel;

  private Long teamId;
  private Runnable onPlayersRemoved;

  public void setTeamId(Long teamId) {
    this.teamId = teamId;
  }

  public void setOnPlayersRemoved(Runnable callback) {
    this.onPlayersRemoved = callback;
  }

  public void setPlayersInTeam(List<PlayerRegistrationDTO> players) {
    ObservableList<PlayerRegistrationDTO> observablePlayers =
        FXCollections.observableArrayList(players);
    tablePlayers.setItems(observablePlayers);
  }

  @FXML
  public void initialize() {
    colId.setCellValueFactory(
        data -> new javafx.beans.property.SimpleLongProperty(data.getValue().getId()));
    colName.setCellValueFactory(
        data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
    colAge.setCellValueFactory(
        data ->
            new javafx.beans.property.SimpleIntegerProperty(
                data.getValue().getAge() != null ? data.getValue().getAge() : 0));
    tablePlayers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  @FXML
  private void handleRemove() {
    List<PlayerRegistrationDTO> selectedPlayers =
        tablePlayers.getSelectionModel().getSelectedItems();
    if (selectedPlayers.isEmpty()) {
      showAlert(Alert.AlertType.WARNING, "No Selection", "Please select at least one player.");
      return;
    }
    if (teamId == null) {
      showAlert(Alert.AlertType.ERROR, "Error", "No team specified!");
      return;
    }

    StringBuilder failed = new StringBuilder();
    for (PlayerRegistrationDTO player : selectedPlayers) {
      String result = removePlayerFromTeam(teamId, player.getId());
      if (result != null) {
        failed
            .append("ID ")
            .append(player.getId())
            .append(" (")
            .append(player.getName())
            .append("): ")
            .append(result)
            .append("\n");
      }
    }

    if (failed.length() == 0) {
      showAlert(Alert.AlertType.INFORMATION, "Success", "Player(s) removed from team!");
      if (onPlayersRemoved != null) onPlayersRemoved.run();
      close();
    } else {
      showAlert(
          Alert.AlertType.ERROR,
          "Some Failed",
          "The following player(s) could not be removed:\n\n" + failed);
    }
  }

  // NOTE: Assume DELETE endpoint is /api/teams/{teamId}/players/{playerId}
  private String removePlayerFromTeam(Long teamId, Long playerId) {
    String endpoint =
        String.format("http://localhost:8080/api/teams/%d/players/%d", teamId, playerId);
    try {
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create(endpoint))
              .DELETE()
              .header("Accept", "*/*")
              .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      int status = response.statusCode();
      if (status == 200 || status == 204) return null;
      return "HTTP " + status + ": " + response.body();
    } catch (Exception e) {
      e.printStackTrace();
      return e.getMessage();
    }
  }

  @FXML
  private void handleCancel() {
    close();
  }

  private void close() {
    Stage stage = (Stage) btnCancel.getScene().getWindow();
    stage.close();
  }

  private void showAlert(Alert.AlertType type, String title, String msg) {
    javafx.application.Platform.runLater(
        () -> {
          Alert alert = new Alert(type);
          alert.setTitle(title);
          alert.setHeaderText(null);
          alert.setContentText(msg);
          alert.showAndWait();
        });
  }
}
