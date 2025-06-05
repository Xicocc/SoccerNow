package com.soccernow.fx.dto;

import java.util.List;

public class TeamRegistrationDTO {
  private Long id;
  private String name;
  private Integer wins;
  private List<PlayerRegistrationDTO> players;
  private Integer losses;
  private Integer draws;
  private Integer titles;

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

  public Integer getLosses() {
    return losses;
  }

  public void setLosses(Integer losses) {
    this.losses = losses;
  }

  public Integer getDraws() {
    return draws;
  }

  public void setDraws(Integer draws) {
    this.draws = draws;
  }

  public Integer getTitles() {
    return titles;
  }

  public void setTitles(Integer titles) {
    this.titles = titles;
  }

  public List<PlayerRegistrationDTO> getPlayers() {
    return players;
  }

  public void setPlayers(List<PlayerRegistrationDTO> players) {
    this.players = players;
  }

  @Override
  public String toString() {
    return name + " (ID:" + id + ")";
  }
}
