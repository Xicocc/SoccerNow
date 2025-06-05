package com.soccernow.fx.controller;

import com.soccernow.fx.dto.GameRegistrationDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditGameStatsFormController {

  @FXML private Label lblGameId;
  @FXML private TextField txtHomeScore;
  @FXML private TextField txtAwayScore;
  @FXML private TextField txtLocation;
  @FXML private Button btnConfirm;
  @FXML private Button btnCancel;

  private Long gameId;
  private Runnable onGameUpdated;

  public void setGameInfo(GameRegistrationDTO dto) {
    this.gameId = dto.getId();
    lblGameId.setText(gameId + " - " + dto.getHomeTeamName() + " vs " + dto.getAwayTeamName());
    txtHomeScore.setText(dto.getHomeScore() != null ? dto.getHomeScore().toString() : "");
    txtAwayScore.setText(dto.getAwayScore() != null ? dto.getAwayScore().toString() : "");
    txtLocation.setText(dto.getLocation() != null ? dto.getLocation() : "");
  }

  public void setOnGameUpdated(Runnable callback) {
    this.onGameUpdated = callback;
  }

  @FXML
  public void initialize() {
    Platform.runLater(() -> btnCancel.requestFocus());
  }

  @FXML
  private void onConfirm() {
    try {
      Integer homeScore = Integer.valueOf(txtHomeScore.getText().trim());
      Integer awayScore = Integer.valueOf(txtAwayScore.getText().trim());
      String location = txtLocation.getText().trim();

      boolean valid = true;
      txtHomeScore.setStyle("");
      txtAwayScore.setStyle("");
      txtLocation.setStyle("");

      if (homeScore < 0 || awayScore < 0) {
        txtHomeScore.setStyle("-fx-border-color: red;");
        txtAwayScore.setStyle("-fx-border-color: red;");
        valid = false;
      }
      if (location.isEmpty()) {
        txtLocation.setStyle("-fx-border-color: red;");
        valid = false;
      }
      if (!valid) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText("Scores cannot be negative and location is required.");
        alert.showAndWait();
        return;
      }

      boolean scoreOk = sendPatchResult(gameId, homeScore, awayScore);
      boolean locationOk = sendPatchLocation(gameId, location);

      if (scoreOk && locationOk) {
        if (onGameUpdated != null) onGameUpdated.run();
        closeWindow();
      }
    } catch (NumberFormatException e) {
      txtHomeScore.setStyle("-fx-border-color: red;");
      txtAwayScore.setStyle("-fx-border-color: red;");
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

  private boolean sendPatchResult(Long id, int homeScore, int awayScore) {
    try {
      String endpoint =
          "http://localhost:8080/api/games/"
              + id
              + "/result?homeScore="
              + homeScore
              + "&awayScore="
              + awayScore;
      java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
      java.net.http.HttpRequest request =
          java.net.http.HttpRequest.newBuilder()
              .uri(java.net.URI.create(endpoint))
              .method("PATCH", java.net.http.HttpRequest.BodyPublishers.noBody())
              .header("Accept", "*/*")
              .build();

      java.net.http.HttpResponse<String> response =
          client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
      int code = response.statusCode();

      if (code == 200 || code == 204) {
        Platform.runLater(
            () -> {
              Alert alert = new Alert(Alert.AlertType.INFORMATION);
              alert.setTitle("Score Updated");
              alert.setHeaderText(null);
              alert.setContentText("Game score updated successfully!");
              alert.showAndWait();
            });
        return true;
      } else {
        throw new RuntimeException("Failed: HTTP error code : " + code);
      }
    } catch (Exception e) {
      e.printStackTrace();
      Platform.runLater(
          () -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Failed");
            alert.setHeaderText(null);
            alert.setContentText("Failed to update score: " + e.getMessage());
            alert.showAndWait();
          });
      return false;
    }
  }

  private boolean sendPatchLocation(Long id, String location) {
    try {
      String endpoint =
          "http://localhost:8080/api/games/"
              + id
              + "/location?location="
              + java.net.URLEncoder.encode(location, "UTF-8");
      java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
      java.net.http.HttpRequest request =
          java.net.http.HttpRequest.newBuilder()
              .uri(java.net.URI.create(endpoint))
              .method("PATCH", java.net.http.HttpRequest.BodyPublishers.noBody())
              .header("Accept", "*/*")
              .build();

      java.net.http.HttpResponse<String> response =
          client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
      int code = response.statusCode();

      if (code == 200 || code == 204) {
        Platform.runLater(
            () -> {
              Alert alert = new Alert(Alert.AlertType.INFORMATION);
              alert.setTitle("Location Updated");
              alert.setHeaderText(null);
              alert.setContentText("Game location updated successfully!");
              alert.showAndWait();
            });
        return true;
      } else {
        throw new RuntimeException("Failed: HTTP error code : " + code);
      }
    } catch (Exception e) {
      e.printStackTrace();
      Platform.runLater(
          () -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Failed");
            alert.setHeaderText(null);
            alert.setContentText("Failed to update location: " + e.getMessage());
            alert.showAndWait();
          });
      return false;
    }
  }
}
