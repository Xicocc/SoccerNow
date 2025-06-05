package com.soccernow.fx.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddTeamFormController {

  @FXML private TextField txtName;
  @FXML private Button btnConfirm;
  @FXML private Button btnCancel;

  private TeamDataListener listener;

  public interface TeamDataListener {
    void onTeamDataEntered(String name);
  }

  public void setTeamDataListener(TeamDataListener listener) {
    this.listener = listener;
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
    String name = txtName.getText().trim();

    // --- Name check ---
    if (name.isEmpty()) {
      txtName.setStyle("-fx-border-color: red;");
      javafx.scene.control.Alert alert =
          new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
      alert.setTitle("Invalid Name");
      alert.setHeaderText(null);
      alert.setContentText("Team name cannot be empty.");
      alert.showAndWait();
      return;
    } else {
      txtName.setStyle("");
    }

    if (listener != null) {
      listener.onTeamDataEntered(name);
      closeWindow();
    } else {
      // Show validation error
      txtName.setStyle(name.isEmpty() ? "-fx-border-color: red;" : "");
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
}
