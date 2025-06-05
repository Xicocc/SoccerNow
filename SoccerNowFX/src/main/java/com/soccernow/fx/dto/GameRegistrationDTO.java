package com.soccernow.fx.dto;

public class GameRegistrationDTO {
  private Long id;
  private TeamRegistrationDTO homeTeam;
  private TeamRegistrationDTO awayTeam;
  private RefereeRegistrationDTO referee;
  private Integer homeScore;
  private Integer awayScore;
  private String gameTime;
  private String location;
  private Long championshipId;
  private Boolean championshipGame;
  private String status;
  private String result;

  // --- GETTERS ---
  public Long getId() {
    return id;
  }

  public TeamRegistrationDTO getHomeTeam() {
    return homeTeam;
  }

  public TeamRegistrationDTO getAwayTeam() {
    return awayTeam;
  }

  public RefereeRegistrationDTO getReferee() {
    return referee;
  }

  public Integer getHomeScore() {
    return homeScore;
  }

  public Integer getAwayScore() {
    return awayScore;
  }

  public String getGameTime() {
    return gameTime;
  }

  public String getLocation() {
    return location;
  }

  public Long getChampionshipId() {
    return championshipId;
  }

  public Boolean getChampionshipGame() {
    return championshipGame;
  }

  public String getStatus() {
    return status;
  }

  public String getResult() {
    return result;
  }

  // --- NAME GETTERS FOR TABLE ---
  public String getHomeTeamName() {
    return homeTeam != null ? homeTeam.getName() : "-";
  }

  public Long getHomeTeamId() {
    return homeTeam != null ? homeTeam.getId() : null;
  }

  public String getAwayTeamName() {
    return awayTeam != null ? awayTeam.getName() : "-";
  }

  public Long getAwayTeamId() {
    return awayTeam != null ? awayTeam.getId() : null;
  }

  public String getRefereeName() {
    return referee != null ? referee.getName() : "-";
  }

  public Long getRefereeId() {
    return referee != null ? referee.getId() : null;
  }

  public String getFormattedDate() {
    if (gameTime == null) return "-";
    // Just take the date part
    return gameTime.length() >= 10 ? gameTime.substring(0, 10) : gameTime;
  }

  // --- SETTERS ---
  public void setId(Long id) {
    this.id = id;
  }

  public void setHomeTeam(TeamRegistrationDTO homeTeam) {
    this.homeTeam = homeTeam;
  }

  public void setAwayTeam(TeamRegistrationDTO awayTeam) {
    this.awayTeam = awayTeam;
  }

  public void setReferee(RefereeRegistrationDTO referee) {
    this.referee = referee;
  }

  public void setHomeScore(Integer homeScore) {
    this.homeScore = homeScore;
  }

  public void setAwayScore(Integer awayScore) {
    this.awayScore = awayScore;
  }

  public void setGameTime(String gameTime) {
    this.gameTime = gameTime;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public void setChampionshipId(Long championshipId) {
    this.championshipId = championshipId;
  }

  public void setChampionshipGame(Boolean championshipGame) {
    this.championshipGame = championshipGame;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setResult(String result) {
    this.result = result;
  }
}
