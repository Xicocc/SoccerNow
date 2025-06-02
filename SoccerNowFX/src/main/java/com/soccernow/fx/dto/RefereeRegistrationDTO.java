package com.soccernow.fx.dto;

public class RefereeRegistrationDTO {
  private String name;
  private boolean certified;

  public RefereeRegistrationDTO() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isCertified() {
    return certified;
  }

  public void setCertified(boolean certified) {
    this.certified = certified;
  }
}
