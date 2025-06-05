package com.soccernow.fx.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddRefereeFormController {

  @FXML private TextField txtName;
  @FXML private TextField txtAge;
  @FXML private Button btnConfirm;
  @FXML private Button btnCancel;

  private RefereeDataListener listener;

  public interface RefereeDataListener {
    void onRefereeDataEntered(String name, int age);
  }

  public void setRefereeDataListener(RefereeDataListener listener) {
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
    String ageStr = txtAge.getText().trim();

    // --- Name check ---
    if (name.isEmpty()) {
      txtName.setStyle("-fx-border-color: red;");
      javafx.scene.control.Alert alert =
          new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
      alert.setTitle("Invalid Name");
      alert.setHeaderText(null);
      alert.setContentText("Referee name cannot be empty.");
      alert.showAndWait();
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
      return;
    }

    // --- Age minimum check ---
    if (age < 18) {
      txtAge.setStyle("-fx-border-color: red;");
      javafx.scene.control.Alert alert =
          new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
      alert.setTitle("Invalid Age");
      alert.setHeaderText(null);
      alert.setContentText("Referee must be at least 18 years old.");
      alert.showAndWait();
      return;
    } else {
      txtAge.setStyle("");
    }

    // All checks passed, proceed
    if (listener != null) {
      listener.onRefereeDataEntered(name, age);
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
