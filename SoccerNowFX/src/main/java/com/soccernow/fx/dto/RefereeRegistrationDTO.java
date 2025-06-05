package com.soccernow.fx.dto;

public class RefereeRegistrationDTO {
  private Long id;
  private String name;
  private Integer age;
  private String gamesParticipated;

  public RefereeRegistrationDTO() {}

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

  public String getGamesParticipated() {
    return gamesParticipated;
  }

  public void setGamesParticipated(String gamesParticipated) {
    this.gamesParticipated = gamesParticipated;
  }

  @Override
  public String toString() {
    return name + " (ID:" + id + ")";
  }
}
