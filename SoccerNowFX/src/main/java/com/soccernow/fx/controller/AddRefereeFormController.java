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

    int age = 0;
    try {
      age = Integer.parseInt(ageStr);
    } catch (Exception e) {
    }

    if (listener != null && !name.isEmpty() && age > 0) {
      listener.onRefereeDataEntered(name, age);
      closeWindow();
    } else {
      // Show validation error
      txtName.setStyle(name.isEmpty() ? "-fx-border-color: red;" : "");
      txtAge.setStyle((age <= 0) ? "-fx-border-color: red;" : "");
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
