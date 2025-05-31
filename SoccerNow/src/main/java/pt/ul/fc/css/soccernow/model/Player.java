package pt.ul.fc.css.soccernow.model;

import jakarta.persistence.*;

@Entity
public class Player extends User {
  @Column(name = "games_played")
  private int gamesPlayed = 0;

  @Column(name = "goals_scored")
  private int goalsScored = 0;

  @Column(name = "yellow_cards")
  private int yellowCards = 0;

  @Column(name = "red_cards")
  private int redCards = 0;

  @Enumerated(EnumType.STRING)
  @Column(name = "preferred_position")
  private Position preferredPosition;

  public Player() {}

  public Player(String name, Integer age, Position preferredPosition) {
    super(name, age, UserType.PLAYER);
    this.preferredPosition = preferredPosition;
  }

  public int getGamesPlayed() {
    return gamesPlayed;
  }

  public void setGamesPlayed(int gamesPlayed) {
    this.gamesPlayed = gamesPlayed;
  }

  public int getGoalsScored() {
    return goalsScored;
  }

  public void setGoalsScored(int goalsScored) {
    this.goalsScored = goalsScored;
  }

  public int getYellowCards() {
    return yellowCards;
  }

  public void setYellowCards(int yellowCards) {
    this.yellowCards = yellowCards;
  }

  public int getRedCards() {
    return redCards;
  }

  public void setRedCards(int redCards) {
    this.redCards = redCards;
  }

  public Position getPreferredPosition() {
    return preferredPosition;
  }

  public void setPreferredPosition(Position preferredPosition) {
    this.preferredPosition = preferredPosition;
  }
}
