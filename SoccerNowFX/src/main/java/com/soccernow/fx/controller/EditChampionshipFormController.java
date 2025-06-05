package com.soccernow.fx.controller;

import com.soccernow.fx.dto.ChampionshipRegistrationDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditChampionshipFormController {

  @FXML private Label lblChampionshipId;
  @FXML private ComboBox<String> comboStatus;
  @FXML private Button btnConfirm;
  @FXML private Button btnCancel;

  private Long championshipId;
  private Runnable onChampionshipUpdated; // Callback to refresh table

  public void setChampionshipInfo(ChampionshipRegistrationDTO dto) {
    this.championshipId = dto.getId();
    lblChampionshipId.setText(championshipId + " - " + dto.getName());
    comboStatus.setValue(dto.getStatus()); // Set current status
  }

  public void setOnChampionshipUpdated(Runnable callback) {
    this.onChampionshipUpdated = callback;
  }

  @FXML
  public void initialize() {
    comboStatus
        .getItems()
        .setAll("UPCOMING", "ONGOING", "COMPLETED", "CANCELLED"); // Adjust to your enum
    Platform.runLater(() -> btnCancel.requestFocus());
  }

  @FXML
  private void onConfirm() {
    String status = comboStatus.getValue();
    if (status == null || status.isEmpty()) {
      comboStatus.setStyle("-fx-border-color: red;");
      return;
    }

    boolean success = sendPatchStatus(championshipId, status);
    if (success) {
      if (onChampionshipUpdated != null) onChampionshipUpdated.run();
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

  private boolean sendPatchStatus(Long id, String status) {
    try {
      String endpoint =
          "http://localhost:8080/api/championships/" + id + "/status?status=" + status;
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
              alert.setTitle("Status Updated");
              alert.setHeaderText(null);
              alert.setContentText("Championship status updated successfully!");
              alert.showAndWait();
            });
        return true;
      } else {
        throw new RuntimeException("Failed: HTTP error code: " + code);
      }
    } catch (Exception e) {
      e.printStackTrace();
      Platform.runLater(
          () -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Failed");
            alert.setHeaderText(null);
            alert.setContentText("Failed to update status: " + e.getMessage());
            alert.showAndWait();
          });
      return false;
    }
  }
}
