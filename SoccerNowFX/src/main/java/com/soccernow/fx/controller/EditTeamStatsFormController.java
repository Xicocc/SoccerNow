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

public class EditTeamStatsFormController {

  @FXML private Label lblTeamId;
  @FXML private TextField txtGamesWon;
  @FXML private TextField txtGamesLoss;
  @FXML private TextField txtGamesDrawn;
  @FXML private TextField txtTitles;
  @FXML private Button btnConfirm;
  @FXML private Button btnCancel;

  private Long teamId;
  private Runnable onStatsUpdated; // Callback to refresh table

  public void setTeamInfo(
      long teamId, String teamName, long gamesWon, long GamesLoss, long gamesDrawn, long titles) {
    this.teamId = teamId;
    lblTeamId.setText(teamId + " - " + teamName);
    txtGamesWon.setText("");
    txtGamesLoss.setText("");
    txtGamesDrawn.setText("");
    txtTitles.setText("");
    txtGamesWon.setPromptText("Games Won: " + gamesWon);
    txtGamesLoss.setPromptText("Games Lost: " + GamesLoss);
    txtGamesDrawn.setPromptText("Games Drawn: " + gamesDrawn);
    txtTitles.setPromptText("Titles: " + titles);
  }

  public void setOnStatsUpdated(Runnable callback) {
    this.onStatsUpdated = callback;
  }

  @FXML
  public void initialize() {
    Platform.runLater(() -> btnCancel.requestFocus());
  }

  @FXML
  private void onConfirm() {
    StringBuilder result = new StringBuilder();
    boolean anyFieldUpdated = false;
    boolean anyError = false;

    // Validate and patch each field if not empty
    if (!txtGamesWon.getText().trim().isEmpty()) {
      Long value = parseNonNegative(txtGamesWon.getText().trim(), "Games Won");
      if (value == null) {
        anyError = true;
      } else {
        anyFieldUpdated = true;
        boolean success = sendPatch("wins", value);
        result.append(success ? "Games won updated!\n" : "Failed to update games won.\n");
        if (!success) anyError = true;
      }
    }
    if (!txtGamesLoss.getText().trim().isEmpty()) {
      Long value = parseNonNegative(txtGamesLoss.getText().trim(), "Games Lost");
      if (value == null) {
        anyError = true;
      } else {
        anyFieldUpdated = true;
        boolean success = sendPatch("losses", value);
        result.append(success ? "Games lost updated!\n" : "Failed to update games lost.\n");
        if (!success) anyError = true;
      }
    }
    if (!txtGamesDrawn.getText().trim().isEmpty()) {
      Long value = parseNonNegative(txtGamesDrawn.getText().trim(), "Games Drawn");
      if (value == null) {
        anyError = true;
      } else {
        anyFieldUpdated = true;
        boolean success = sendPatch("draws", value);
        result.append(success ? "Games drawn updated!\n" : "Failed to update games drawn.\n");
        if (!success) anyError = true;
      }
    }
    if (!txtTitles.getText().trim().isEmpty()) {
      Long value = parseNonNegative(txtTitles.getText().trim(), "Titles");
      if (value == null) {
        anyError = true;
      } else {
        anyFieldUpdated = true;
        boolean success = sendPatch("titles", value);
        result.append(success ? "Titles updated!\n" : "Failed to update titles.\n");
        if (!success) anyError = true;
      }
    }

    if (!anyFieldUpdated) {
      showAlert(Alert.AlertType.WARNING, "No Input", "Please enter a value in at least one field.");
      return;
    }

    // Show results
    if (anyError) {
      showAlert(Alert.AlertType.WARNING, "Update Result", result.toString());
    } else {
      showAlert(Alert.AlertType.INFORMATION, "Success", result.toString());
      if (onStatsUpdated != null) onStatsUpdated.run();
      closeWindow();
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

  // Send PATCH to the appropriate endpoint
  private boolean sendPatch(String type, long value) {
    // Endpoint mapping
    String endpoint;
    switch (type) {
      case "wins":
        endpoint = String.format("http://localhost:8080/api/teams/%d/wins?wins=%d", teamId, value);
        break;
      case "losses":
        endpoint =
            String.format("http://localhost:8080/api/teams/%d/losses?losses=%d", teamId, value);
        break;
      case "draws":
        endpoint =
            String.format("http://localhost:8080/api/teams/%d/draws?draws=%d", teamId, value);
        break;
      case "titles":
        endpoint =
            String.format("http://localhost:8080/api/teams/%d/titles?titles=%d", teamId, value);
        break;
      default:
        return false;
    }
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
      e.printStackTrace();
      return false;
    }
  }

  // Utility: parse and validate non-negative integer
  private Long parseNonNegative(String text, String fieldName) {
    try {
      long value = Long.parseLong(text);
      if (value < 0) {
        showAlert(Alert.AlertType.WARNING, "Invalid Input", fieldName + " cannot be negative.");
        return null;
      }
      return value;
    } catch (NumberFormatException e) {
      showAlert(Alert.AlertType.WARNING, "Invalid Input", fieldName + " must be a number.");
      return null;
    }
  }

  private void showAlert(Alert.AlertType type, String title, String msg) {
    Platform.runLater(
        () -> {
          Alert alert = new Alert(type);
          alert.setTitle(title);
          alert.setHeaderText(null);
          alert.setContentText(msg);
          alert.showAndWait();
        });
  }
}
