package com.soccernow.fx.dto;

public class PlayerRegistrationDTO {
  private String name;
  private String position;

  public PlayerRegistrationDTO() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }
}
