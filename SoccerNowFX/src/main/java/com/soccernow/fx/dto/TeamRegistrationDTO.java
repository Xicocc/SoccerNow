package com.soccernow.fx.dto;

import java.util.List;

public class TeamRegistrationDTO {
  private Long id;
  private String name;
  private Integer wins;
  private List<PlayerRegistrationDTO> players;

  public TeamRegistrationDTO() {}

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

  public Integer getWins() {
    return wins;
  }

  public void setWins(Integer wins) {
    this.wins = wins;
  }

  public List<PlayerRegistrationDTO> getPlayers() {
    return players;
  }

  public void setPlayers(List<PlayerRegistrationDTO> players) {
    this.players = players;
  }
}
