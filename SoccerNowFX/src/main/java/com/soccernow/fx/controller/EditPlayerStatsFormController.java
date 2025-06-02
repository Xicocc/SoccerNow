package com.soccernow.fx.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditPlayerStatsFormController {

  @FXML private Label lblPlayerId;
  @FXML private TextField txtYellowCards;
  @FXML private TextField txtRedCards;
  @FXML private TextField txtGoals;
  @FXML private TextField txtGamesPlayed;
  @FXML private Button btnConfirm;
  @FXML private Button btnCancel;

  private Long playerId;
  private Runnable onStatsUpdated; // Callback to refresh table

  public void setPlayerInfo(long playerId, String playerName) {
    this.playerId = playerId;
    lblPlayerId.setText(playerId + " - " + playerName);
  }

  public void setOnStatsUpdated(Runnable callback) {
    this.onStatsUpdated = callback;
  }

  @FXML
  public void initialize() {
    Platform.runLater(
        () -> {
          btnCancel.requestFocus();
        });
  }

  @FXML
  private void onConfirm() {
    boolean updated = false;
    boolean success = true;
    StringBuilder errorMessages = new StringBuilder();

    if (!txtYellowCards.getText().trim().isEmpty()) {
      Integer yc = parseIntSafe(txtYellowCards.getText().trim());
      if (yc != null) {
        updated |= sendPatch(playerId, "yellow-cards", "ycards", yc);
      } else {
        errorMessages.append("Invalid Yellow Cards value.\n");
        success = false;
      }
    }
    if (!txtRedCards.getText().trim().isEmpty()) {
      Integer rc = parseIntSafe(txtRedCards.getText().trim());
      if (rc != null) {
        updated |= sendPatch(playerId, "red-cards", "rcards", rc);
      } else {
        errorMessages.append("Invalid Red Cards value.\n");
        success = false;
      }
    }
    if (!txtGoals.getText().trim().isEmpty()) {
      Integer g = parseIntSafe(txtGoals.getText().trim());
      if (g != null) {
        updated |= sendPatch(playerId, "goals", "goals", g);
      } else {
        errorMessages.append("Invalid Goals value.\n");
        success = false;
      }
    }
    if (!txtGamesPlayed.getText().trim().isEmpty()) {
      Integer gp = parseIntSafe(txtGamesPlayed.getText().trim());
      if (gp != null) {
        updated |= sendPatch(playerId, "games-played", "games", gp);
      } else {
        errorMessages.append("Invalid Games Played value.\n");
        success = false;
      }
    }

    if (!success) {
      showAlert(Alert.AlertType.ERROR, "Invalid Input", errorMessages.toString());
      return;
    }

    if (updated) {
      showAlert(Alert.AlertType.INFORMATION, "Success", "Stats updated!");
      if (onStatsUpdated != null) onStatsUpdated.run();
      closeWindow();
    } else {
      showAlert(Alert.AlertType.WARNING, "No Update", "Please fill at least one field to update.");
    }
  }

  @FXML
  private void onCancel() {
    closeWindow();
  }

  private void closeWindow() {
    Stage stage = (Stage) btnCancel.getScene().getWindow();
    stage.close();
  }

  private Integer parseIntSafe(String s) {
    try {
      return Integer.parseInt(s);
    } catch (Exception e) {
      return null;
    }
  }

  private boolean sendPatch(Long id, String endpointSuffix, String paramName, int value) {
    String endpoint =
        String.format(
            "http://localhost:8080/api/players/%d/%s?%s=%d", id, endpointSuffix, paramName, value);
    try {
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create(endpoint))
              .method("PATCH", HttpRequest.BodyPublishers.noBody())
              .header("Accept", "*/*")
              .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      int responseCode = response.statusCode();

      return (responseCode == 200 || responseCode == 204);
    } catch (Exception e) {
      showAlert(
          javafx.scene.control.Alert.AlertType.ERROR,
          "Update Failed",
          "Error updating stat at " + endpointSuffix + ": " + e.getMessage());
      return false;
    }
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
