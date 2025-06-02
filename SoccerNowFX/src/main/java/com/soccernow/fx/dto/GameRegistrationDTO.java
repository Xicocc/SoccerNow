package com.soccernow.fx.dto;

public class GameRegistrationDTO {
  private String date;
  private String time;
  private String location;
  private Long teamAId;
  private Long teamBId;
  private Long refereeId;

  public GameRegistrationDTO() {}

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Long getTeamAId() {
    return teamAId;
  }

  public void setTeamAId(Long teamAId) {
    this.teamAId = teamAId;
  }

  public Long getTeamBId() {
    return teamBId;
  }

  public void setTeamBId(Long teamBId) {
    this.teamBId = teamBId;
  }

  public Long getRefereeId() {
    return refereeId;
  }

  public void setRefereeId(Long refereeId) {
    this.refereeId = refereeId;
  }
}
