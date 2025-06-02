package com.soccernow.fx.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginScreenController implements Initializable {

  @FXML private TextField usernameField;

  @FXML private PasswordField passwordField;

  @FXML private Button loginButton;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Focus dummy button so no text field is selected
    Platform.runLater(
        () -> {
          loginButton.requestFocus();
        });
  }

  @FXML
  private void handleLoginButton(ActionEvent event) throws IOException {
    // If you have authentication logic, add it here
    // After successful login, switch to HomeScreen
    Parent homeRoot = FXMLLoader.load(getClass().getResource("/fxml/HomeScreen.fxml"));
    Scene homeScene = new Scene(homeRoot);

    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(homeScene);
  }
}
