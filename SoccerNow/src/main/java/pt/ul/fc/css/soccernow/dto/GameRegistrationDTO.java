package pt.ul.fc.css.soccernow.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public class GameRegistrationDTO {

  @NotBlank(message = "Home team name is required")
  private String homeTeamName;

  @NotBlank(message = "Away team name is required")
  private String awayTeamName;

  @FutureOrPresent(message = "Game time must be in the present or future")
  private LocalDateTime gameTime;

  private String location;

  @Positive(message = "Championship ID must be a positive number")
  private Long championshipId;

  public GameRegistrationDTO() {}

  public GameRegistrationDTO(
      String homeTeamName,
      String awayTeamName,
      LocalDateTime gameTime,
      String location,
      Long championshipId) {
    this.homeTeamName = homeTeamName;
    this.awayTeamName = awayTeamName;
    this.gameTime = gameTime;
    this.location = location;
    this.championshipId = championshipId;
  }

  public String getHomeTeamName() {
    return homeTeamName;
  }

  public void setHomeTeamName(String homeTeamName) {
    this.homeTeamName = homeTeamName;
  }

  public String getAwayTeamName() {
    return awayTeamName;
  }

  public void setAwayTeamName(String awayTeamName) {
    this.awayTeamName = awayTeamName;
  }

  public LocalDateTime getGameTime() {
    return gameTime;
  }

  public void setGameTime(LocalDateTime gameTime) {
    this.gameTime = gameTime;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Long getChampionshipId() {
    return championshipId;
  }

  public void setChampionshipId(Long championshipId) {
    this.championshipId = championshipId;
  }
}
