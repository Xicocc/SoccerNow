package com.soccernow.fx.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddPlayerFormController {

  @FXML private TextField txtName;
  @FXML private TextField txtAge;
  @FXML private ComboBox<String> comboPosition;
  @FXML private Button btnConfirm;
  @FXML private Button btnCancel;

  private PlayerDataListener listener;

  public interface PlayerDataListener {
    void onPlayerDataEntered(String name, int age, String position);
  }

  public void setPlayerDataListener(PlayerDataListener listener) {
    this.listener = listener;
  }

  @FXML
  public void initialize() {
    // Set allowed values in ComboBox
    comboPosition
        .getItems()
        .addAll("GOALKEEPER", "SWEEPER", "RIGHT_WINGER", "LEFT_WINGER", "FORWARD", "UNKNOWN");
    // Optionally, set a default (uncomment to use):
    comboPosition.setValue("UNKNOWN");
    Platform.runLater(
        () -> {
          btnCancel.requestFocus();
        });
  }

  @FXML
  private void onConfirm() {
    String name = txtName.getText().trim();
    String ageStr = txtAge.getText().trim();
    String position = comboPosition.getValue();

    boolean valid = true;

    // --- Name check ---
    if (name.isEmpty()) {
      txtName.setStyle("-fx-border-color: red;");
      javafx.scene.control.Alert alert =
          new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
      alert.setTitle("Invalid Name");
      alert.setHeaderText(null);
      alert.setContentText("Player name cannot be empty.");
      alert.showAndWait();
      valid = false;
      return;
    } else {
      txtName.setStyle("");
    }

    // --- Age numeric check ---
    int age = 0;
    try {
      age = Integer.parseInt(ageStr);
      txtAge.setStyle("");
    } catch (NumberFormatException e) {
      txtAge.setStyle("-fx-border-color: red;");
      javafx.scene.control.Alert alert =
          new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
      alert.setTitle("Invalid Age");
      alert.setHeaderText(null);
      alert.setContentText("Age must be a valid number.");
      alert.showAndWait();
      valid = false;
      return;
    }

    // --- Age range check ---
    if (age < 16) {
      txtAge.setStyle("-fx-border-color: red;");
      javafx.scene.control.Alert alert =
          new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
      alert.setTitle("Invalid Age");
      alert.setHeaderText(null);
      alert.setContentText("Player must be at least 16 years old.");
      alert.showAndWait();
      valid = false;
      return;
    } else {
      txtAge.setStyle("");
    }

    // If all checks passed, proceed
    if (valid && listener != null) {
      listener.onPlayerDataEntered(name, age, position);
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
}
