package com.soccernow.fx.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.soccernow.fx.dto.ChampionshipRegistrationDTO;
import com.soccernow.fx.dto.RefereeRegistrationDTO;
import com.soccernow.fx.dto.TeamRegistrationDTO;
import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class AddGameFormController {

  @FXML private ComboBox<Object> comboChampionship;
  @FXML private ComboBox<TeamRegistrationDTO> comboTeamA;
  @FXML private ComboBox<TeamRegistrationDTO> comboTeamB;
  @FXML private ComboBox<RefereeRegistrationDTO> comboReferee;
  @FXML private DatePicker dateGame;
  @FXML private ComboBox<Integer> comboHour;
  @FXML private ComboBox<Integer> comboMinute;
  @FXML private TextField txtLocation;
  @FXML private Button btnConfirm;
  @FXML private Button btnCancel;

  @FXML
  public void initialize() {
    populateChampionships();
    populateTeams();
    populateReferees();

    comboHour.getItems().clear();
    for (int h = 0; h < 24; h++) comboHour.getItems().add(h);
    comboHour.setPromptText("Hour");

    comboMinute.getItems().clear();
    for (int m = 0; m < 60; m++) comboMinute.getItems().add(m);
    comboMinute.setPromptText("Minute");

    comboChampionship.setConverter(
        new StringConverter<>() {
          @Override
          public String toString(Object obj) {
            if (obj instanceof ChampionshipRegistrationDTO dto)
              return dto.getName() + " (ID:" + dto.getId() + ")";
            else if (obj instanceof String str) return str;
            else return "";
          }

          @Override
          public Object fromString(String s) {
            return s;
          }
        });

    comboTeamA.setConverter(
        new StringConverter<>() {
          @Override
          public String toString(TeamRegistrationDTO dto) {
            return dto == null ? "" : dto.getName() + " (ID:" + dto.getId() + ")";
          }

          @Override
          public TeamRegistrationDTO fromString(String s) {
            return null;
          }
        });

    comboTeamB.setConverter(
        new StringConverter<>() {
          @Override
          public String toString(TeamRegistrationDTO dto) {
            return dto == null ? "" : dto.getName() + " (ID:" + dto.getId() + ")";
          }

          @Override
          public TeamRegistrationDTO fromString(String s) {
            return null;
          }
        });

    comboReferee.setConverter(
        new StringConverter<>() {
          @Override
          public String toString(RefereeRegistrationDTO dto) {
            return dto == null ? "" : dto.getName() + " (ID:" + dto.getId() + ")";
          }

          @Override
          public RefereeRegistrationDTO fromString(String s) {
            return null;
          }
        });

    Platform.runLater(() -> btnCancel.requestFocus());
  }

  @FXML
  private void onConfirm() {
    Object selectedChampionship = comboChampionship.getValue();
    TeamRegistrationDTO selectedTeamA = comboTeamA.getValue();
    TeamRegistrationDTO selectedTeamB = comboTeamB.getValue();
    RefereeRegistrationDTO selectedReferee = comboReferee.getValue();
    LocalDate date = dateGame.getValue();
    Integer hour = comboHour.getValue();
    Integer minute = comboMinute.getValue();
    String location = txtLocation.getText().trim();

    boolean valid = true;
    StringBuilder errorMsg = new StringBuilder();

    comboTeamA.setStyle("");
    comboTeamB.setStyle("");
    dateGame.setStyle("");
    comboReferee.setStyle("");
    comboHour.setStyle("");
    comboMinute.setStyle("");
    txtLocation.setStyle("");

    if (hour == null) {
      comboHour.setStyle("-fx-border-color: red;");
      errorMsg.append("Hour must be selected.\n");
      valid = false;
    }
    if (minute == null) {
      comboMinute.setStyle("-fx-border-color: red;");
      errorMsg.append("Minute must be selected.\n");
      valid = false;
    }
    if (selectedTeamA == null) {
      comboTeamA.setStyle("-fx-border-color: red;");
      errorMsg.append("Home Team must be selected.\n");
      valid = false;
    }
    if (selectedTeamB == null) {
      comboTeamB.setStyle("-fx-border-color: red;");
      errorMsg.append("Away Team must be selected.\n");
      valid = false;
    }
    if (selectedReferee == null) {
      comboReferee.setStyle("-fx-border-color: red;");
      errorMsg.append("A referee must be selected.\n");
      valid = false;
    }
    if (date == null) {
      dateGame.setStyle("-fx-border-color: red;");
      errorMsg.append("Date must be selected.\n");
      valid = false;
    }
    if (selectedTeamA != null
        && selectedTeamB != null
        && selectedTeamA.getId().equals(selectedTeamB.getId())) {
      comboTeamA.setStyle("-fx-border-color: red;");
      comboTeamB.setStyle("-fx-border-color: red;");
      errorMsg.append("Home Team and Away Team cannot be the same.\n");
      valid = false;
    }
    if (location.isEmpty()) {
      txtLocation.setStyle("-fx-border-color: red;");
      errorMsg.append("Location must be filled.\n");
      valid = false;
    }

    if (!valid) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Invalid Game Data");
      alert.setHeaderText(null);
      alert.setContentText(errorMsg.toString().trim());
      alert.showAndWait();
      return;
    }

    // Combine date, hour, minute into UTC ISO8601 string
    String gameTime;
    {
      LocalDateTime dt = date.atTime(hour, minute);
      ZonedDateTime zdt = dt.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
      gameTime = zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    }

    // Championship ID (0 if "No Championship")
    long championshipId = 0L;
    if (selectedChampionship instanceof ChampionshipRegistrationDTO dto) {
      championshipId = dto.getId();
    }

    // Prepare JSON payload as a Map (no unused POJO, direct to Gson)
    var payload = new java.util.LinkedHashMap<String, Object>();
    payload.put("homeTeamName", selectedTeamA.getName());
    payload.put("awayTeamName", selectedTeamB.getName());
    payload.put("gameTime", gameTime);
    payload.put("location", location);
    payload.put("championshipId", championshipId);
    payload.put("refereeId", selectedReferee.getId());

    String jsonPayload = new Gson().toJson(payload);

    // POST in a background thread
    new Thread(
            () -> {
              boolean ok = sendAddGameRequest(jsonPayload);
              Platform.runLater(
                  () -> {
                    Alert alert =
                        new Alert(ok ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
                    alert.setTitle(ok ? "Success" : "Failed");
                    alert.setHeaderText(null);
                    alert.setContentText(ok ? "Game added successfully!" : "Failed to add game.");
                    alert.showAndWait();
                    if (ok) closeWindow();
                  });
            })
        .start();
  }

  private boolean sendAddGameRequest(String jsonPayload) {
    try {
      String endpoint = "http://localhost:8080/api/games";
      URI uri = URI.create(endpoint);
      HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setDoOutput(true);
      try (OutputStream os = conn.getOutputStream()) {
        byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
        os.write(input, 0, input.length);
      }
      int code = conn.getResponseCode();
      conn.disconnect();
      return code == 200 || code == 201;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
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

  private void populateChampionships() {
    new Thread(
            () -> {
              try {
                String endpoint = "http://localhost:8080/api/championships";
                URI uri = URI.create(endpoint);
                URL url = uri.toURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                BufferedReader br =
                    new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder jsonBuilder = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) jsonBuilder.append(output);
                conn.disconnect();

                Gson gson =
                    new GsonBuilder()
                        .registerTypeAdapter(
                            java.time.LocalDate.class,
                            (com.google.gson.JsonDeserializer<java.time.LocalDate>)
                                (json, type, context) ->
                                    java.time.LocalDate.parse(json.getAsString()))
                        .registerTypeAdapter(
                            java.time.LocalDate.class,
                            (com.google.gson.JsonSerializer<java.time.LocalDate>)
                                (date, type, context) ->
                                    new com.google.gson.JsonPrimitive(date.toString()))
                        .create();

                Type listType = new TypeToken<List<ChampionshipRegistrationDTO>>() {}.getType();
                List<ChampionshipRegistrationDTO> championships =
                    gson.fromJson(jsonBuilder.toString(), listType);

                Platform.runLater(
                    () -> {
                      comboChampionship.getItems().clear();
                      comboChampionship.getItems().add("No Championship (Optional)");
                      comboChampionship.getItems().addAll(championships);
                      comboChampionship.getSelectionModel().selectFirst();
                    });
              } catch (Exception e) {
                e.printStackTrace();
              }
            })
        .start();
  }

  private void populateTeams() {
    new Thread(
            () -> {
              try {
                String endpoint = "http://localhost:8080/api/teams";
                URI uri = URI.create(endpoint);
                URL url = uri.toURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                BufferedReader br =
                    new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder jsonBuilder = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) jsonBuilder.append(output);
                conn.disconnect();

                Gson gson =
                    new GsonBuilder()
                        .registerTypeAdapter(
                            java.time.LocalDate.class,
                            (com.google.gson.JsonDeserializer<java.time.LocalDate>)
                                (json, type, context) ->
                                    java.time.LocalDate.parse(json.getAsString()))
                        .registerTypeAdapter(
                            java.time.LocalDate.class,
                            (com.google.gson.JsonSerializer<java.time.LocalDate>)
                                (date, type, context) ->
                                    new com.google.gson.JsonPrimitive(date.toString()))
                        .create();

                Type listType = new TypeToken<List<TeamRegistrationDTO>>() {}.getType();
                List<TeamRegistrationDTO> teams = gson.fromJson(jsonBuilder.toString(), listType);

                Platform.runLater(
                    () -> {
                      comboTeamA.getItems().setAll(teams);
                      comboTeamB.getItems().setAll(teams);
                    });
              } catch (Exception e) {
                e.printStackTrace();
              }
            })
        .start();
  }

  private void populateReferees() {
    new Thread(
            () -> {
              try {
                String endpoint = "http://localhost:8080/api/referees";
                URI uri = URI.create(endpoint);
                URL url = uri.toURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                BufferedReader br =
                    new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder jsonBuilder = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) jsonBuilder.append(output);
                conn.disconnect();

                Gson gson =
                    new GsonBuilder()
                        .registerTypeAdapter(
                            java.time.LocalDate.class,
                            (com.google.gson.JsonDeserializer<java.time.LocalDate>)
                                (json, type, context) ->
                                    java.time.LocalDate.parse(json.getAsString()))
                        .registerTypeAdapter(
                            java.time.LocalDate.class,
                            (com.google.gson.JsonSerializer<java.time.LocalDate>)
                                (date, type, context) ->
                                    new com.google.gson.JsonPrimitive(date.toString()))
                        .create();

                Type listType = new TypeToken<List<RefereeRegistrationDTO>>() {}.getType();
                List<RefereeRegistrationDTO> referees =
                    gson.fromJson(jsonBuilder.toString(), listType);

                Platform.runLater(() -> comboReferee.getItems().setAll(referees));
              } catch (Exception e) {
                e.printStackTrace();
              }
            })
        .start();
  }
}
