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
  @FXML private Button btnConfirm;
  @FXML private Button btnCancel;

  private Long teamId;
  private Runnable onStatsUpdated; // Callback to refresh table

  public void setTeamInfo(long teamId, String teamName, long gamesWon) {
    this.teamId = teamId;
    lblTeamId.setText(teamId + " - " + teamName);
    txtGamesWon.setText(""); // Clear field
    txtGamesWon.setPromptText("Games Won: " + gamesWon);
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
    String gamesWonText = txtGamesWon.getText().trim();
    if (gamesWonText.isEmpty()) {
      showAlert(Alert.AlertType.WARNING, "No Input", "Please enter a value.");
      return;
    }

    long gamesWon = Long.parseLong(gamesWonText);

    if (gamesWon < 0) {
      showAlert(Alert.AlertType.WARNING, "Invalid Input", "Games won cannot be negative.");
      return;
    }

    boolean success = sendPatch(teamId, gamesWon);
    if (success) {
      showAlert(Alert.AlertType.INFORMATION, "Success", "Games won updated!");
      if (onStatsUpdated != null) onStatsUpdated.run();
      closeWindow();
    } else {
      showAlert(Alert.AlertType.ERROR, "Failed", "Failed to update games won.");
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

  private boolean sendPatch(Long id, long gamesWon) {
    String endpoint =
        String.format("http://localhost:8080/api/teams/%d/wins?wins=%d", id, gamesWon);
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
