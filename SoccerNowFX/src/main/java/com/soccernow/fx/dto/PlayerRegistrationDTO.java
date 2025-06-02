package com.soccernow.fx.dto;

public class PlayerRegistrationDTO {
  private Long id;
  private String name;
  private Integer age;
  private String preferredPosition;
  private Integer gamesPlayed;
  private Integer goalsScored;
  private Integer yellowCards;
  private Integer redCards;

  public PlayerRegistrationDTO() {}

  // Getters and setters for all fields

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getPreferredPosition() {
    return preferredPosition;
  }

  public void setPreferredPosition(String preferredPosition) {
    this.preferredPosition = preferredPosition;
  }

  public Integer getGamesPlayed() {
    return gamesPlayed;
  }

  public void setGamesPlayed(Integer gamesPlayed) {
    this.gamesPlayed = gamesPlayed;
  }

  public Integer getGoalsScored() {
    return goalsScored;
  }

  public void setGoalsScored(Integer goalsScored) {
    this.goalsScored = goalsScored;
  }

  public Integer getYellowCards() {
    return yellowCards;
  }

  public void setYellowCards(Integer yellowCards) {
    this.yellowCards = yellowCards;
  }

  public Integer getRedCards() {
    return redCards;
  }

  public void setRedCards(Integer redCards) {
    this.redCards = redCards;
  }
}
