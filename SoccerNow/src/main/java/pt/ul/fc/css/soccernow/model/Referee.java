package pt.ul.fc.css.soccernow.model;

import jakarta.persistence.*;

@Entity
public class Referee extends User {
  private int gamesParticipated = 0;
  private int cardsShown = 0;

  public Referee() {}

  public Referee(String name, Integer age) {
    super(name, age);
  }

  public int getGamesParticipated() {
    return gamesParticipated;
  }

  public int getCardsShown() {
    return cardsShown;
  }

  public void setGamesParticipated(int gamesParticipated) {
    this.gamesParticipated = gamesParticipated;
  }

  public void setCardsShown(int cardsShown) {
    this.cardsShown = cardsShown;
  }
}
