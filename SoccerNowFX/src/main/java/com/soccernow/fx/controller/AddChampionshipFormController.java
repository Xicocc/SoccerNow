package com.soccernow.fx.controller;

import java.time.LocalDate;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddChampionshipFormController {

  @FXML private TextField txtName;
  @FXML private TextArea txtDescription;
  @FXML private DatePicker dateStart;
  @FXML private DatePicker dateEnd;
  @FXML private TextField txtLocation;
  @FXML private Button btnConfirm;
  @FXML private Button btnCancel;

  private ChampionshipDataListener listener;

  public interface ChampionshipDataListener {
    void onChampionshipDataEntered(
        String name, String description, LocalDate startDate, LocalDate endDate, String location);
  }

  public void setChampionshipDataListener(ChampionshipDataListener listener) {
    this.listener = listener;
  }

  @FXML
  public void initialize() {
    Platform.runLater(() -> btnCancel.requestFocus());
  }

  @FXML
  private void onConfirm() {
    String name = txtName.getText().trim();
    String description = txtDescription.getText().trim();
    LocalDate startDate = dateStart.getValue();
    LocalDate endDate = dateEnd.getValue();
    String location = txtLocation.getText().trim();

    boolean valid = true;
    StringBuilder errorMsg = new StringBuilder();

    // Reset styles
    txtName.setStyle("");
    dateStart.setStyle("");
    dateEnd.setStyle("");

    // Validation
    if (name.isEmpty()) {
      txtName.setStyle("-fx-border-color: red;");
      errorMsg.append("Name cannot be empty.\n");
      valid = false;
    }
    if (startDate == null) {
      dateStart.setStyle("-fx-border-color: red;");
      errorMsg.append("Start date cannot be empty.\n");
      valid = false;
    }
    if (endDate == null) {
      dateEnd.setStyle("-fx-border-color: red;");
      errorMsg.append("End date cannot be empty.\n");
      valid = false;
    }
    if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
      dateStart.setStyle("-fx-border-color: red;");
      dateEnd.setStyle("-fx-border-color: red;");
      errorMsg.append("Start date must be before or equal to end date.\n");
      valid = false;
    }

    if (!valid) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Invalid Championship Data");
      alert.setHeaderText(null);
      alert.setContentText(errorMsg.toString().trim());
      alert.showAndWait();
      return;
    }

    if (listener != null) {
      listener.onChampionshipDataEntered(name, description, startDate, endDate, location);
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
