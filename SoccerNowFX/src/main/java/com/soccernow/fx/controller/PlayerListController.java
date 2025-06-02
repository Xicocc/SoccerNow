package com.soccernow.fx.controller;

import com.soccernow.fx.dto.PlayerRegistrationDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PlayerListController {
  @FXML private TableView<PlayerRegistrationDTO> tablePlayers;
  @FXML private TableColumn<PlayerRegistrationDTO, String> colName;
  @FXML private TableColumn<PlayerRegistrationDTO, String> colPosition;
  @FXML private Button btnAddPlayer;
  @FXML private Button btnEditPlayer;
  @FXML private Button btnDeletePlayer;

  @FXML
  public void initialize() {
    // Set up columns (assuming PlayerRegistrationDTO has getName/getPosition)
    colName.setCellValueFactory(
        cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
    colPosition.setCellValueFactory(
        cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPosition()));
    // For now: no real data, just test
  }
}
