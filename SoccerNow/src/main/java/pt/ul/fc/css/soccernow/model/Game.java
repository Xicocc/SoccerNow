package pt.ul.fc.css.soccernow.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Game {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "home_team_id", nullable = false)
  private Team homeTeam;

  @ManyToOne
  @JoinColumn(name = "away_team_id", nullable = false)
  private Team awayTeam;

  @Column(name = "home_score", columnDefinition = "integer default 0")
  private int homeScore = 0;

  @Column(name = "away_score", columnDefinition = "integer default 0")
  private int awayScore = 0;

  @Column(name = "game_time", nullable = false)
  private LocalDateTime gameTime;

  @Column(name = "location")
  private String location;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private GameStatus status = GameStatus.SCHEDULED;

  @ManyToOne
  @JoinColumn(name = "championship_id")
  private Championship championship;

  public Game() {}

  public Game(Team homeTeam, Team awayTeam, LocalDateTime gameTime, String location) {
    this.homeTeam = homeTeam;
    this.awayTeam = awayTeam;
    this.gameTime = gameTime;
    this.location = location;
    this.status = GameStatus.SCHEDULED;
  }

  public Long getId() {
    return id;
  }

  public Team getHomeTeam() {
    return homeTeam;
  }

  public void setHomeTeam(Team homeTeam) {
    this.homeTeam = homeTeam;
  }

  public Team getAwayTeam() {
    return awayTeam;
  }

  public void setAwayTeam(Team awayTeam) {
    this.awayTeam = awayTeam;
  }

  public int getHomeScore() {
    return homeScore;
  }

  public void setHomeScore(int homeScore) {
    this.homeScore = homeScore;
  }

  public int getAwayScore() {
    return awayScore;
  }

  public void setAwayScore(int awayScore) {
    this.awayScore = awayScore;
  }

  public LocalDateTime getGameTime() {
    return gameTime;
  }

  public void setGameTime(LocalDateTime gameTime) {
    this.gameTime = gameTime;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public GameStatus getStatus() {
    return status;
  }

  public void setStatus(GameStatus status) {
    this.status = status;
  }

  public Championship getChampionship() {
    return championship;
  }

  public void setChampionship(Championship championship) {
    this.championship = championship;
  }

  // Helper method to check if game is part of a championship
  public boolean isChampionshipGame() {
    return championship != null;
  }
}
