package pt.ul.fc.css.soccernow.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", unique = true) // Add unique constraint
  private String name;

  @Column(name = "wins", columnDefinition = "integer default 0")
  private int wins = 0;

  @ManyToMany
  @JoinTable(
      name = "team_players",
      joinColumns = @JoinColumn(name = "team_id"),
      inverseJoinColumns = @JoinColumn(name = "player_id"))
  private List<Player> players = new ArrayList<>();

  // Constructors, getters, setters
  public Team() {}

  public Team(String name, List<Player> players) {
    this.name = name;
    this.players = players;
  }

  // Getters & Setters
  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getWins() {
    return wins;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setWins(int wins) {
    this.wins = wins;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
  }

  // Helper method to get only player IDs
  public List<Long> getPlayerIds() {
    return players.stream().map(Player::getId).toList();
  }
}
