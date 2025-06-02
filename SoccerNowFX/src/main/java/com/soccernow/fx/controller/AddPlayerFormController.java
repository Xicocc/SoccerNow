package com.soccernow.fx.controller;

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
    // comboPosition.setValue("UNKNOWN");
  }

  @FXML
  private void onConfirm() {
    String name = txtName.getText().trim();
    String ageStr = txtAge.getText().trim();
    String position = comboPosition.getValue();

    int age = 0;
    try {
      age = Integer.parseInt(ageStr);
    } catch (Exception e) {
    }

    if (listener != null && !name.isEmpty() && age > 0 && position != null && !position.isEmpty()) {
      listener.onPlayerDataEntered(name, age, position);
      closeWindow();
    } else {
      // Show validation error
      txtName.setStyle(name.isEmpty() ? "-fx-border-color: red;" : "");
      txtAge.setStyle((age <= 0) ? "-fx-border-color: red;" : "");
      comboPosition.setStyle(
          (position == null || position.isEmpty()) ? "-fx-border-color: red;" : "");
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
