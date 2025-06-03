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

public class EditRefereeStatsFormController {

  @FXML private Label lblRefereeId;
  @FXML private TextField txtGamesParticipated;
  @FXML private Button btnConfirm;
  @FXML private Button btnCancel;

  private Long refereeId;
  private Runnable onStatsUpdated; // Callback to refresh table

  public void setRefereeInfo(long refereeId, String refereeName, long gamesParticipated) {
    this.refereeId = refereeId;
    lblRefereeId.setText(refereeId + " - " + refereeName);
    txtGamesParticipated.setText(""); // Clear field
    txtGamesParticipated.setPromptText("Games Participated: " + gamesParticipated);
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
    String value = txtGamesParticipated.getText().trim();
    if (value.isEmpty()) {
      showAlert(Alert.AlertType.WARNING, "No Input", "Please enter a value.");
      return;
    }
    Integer games = parseIntSafe(value);
    if (games == null || games < 0) {
      showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number.");
      return;
    }
    boolean success = sendPatch(refereeId, games);
    if (success) {
      showAlert(Alert.AlertType.INFORMATION, "Success", "Games participated updated!");
      if (onStatsUpdated != null) onStatsUpdated.run();
      closeWindow();
    } else {
      showAlert(Alert.AlertType.ERROR, "Failed", "Failed to update games participated.");
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

  private boolean sendPatch(Long id, int games) {
    String endpoint =
        String.format(
            "http://localhost:8080/api/referees/%d/games-participated?games=%d", id, games);
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
