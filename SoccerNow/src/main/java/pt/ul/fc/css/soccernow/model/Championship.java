package pt.ul.fc.css.soccernow.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Championship {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false, unique = true)
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @Column(name = "location")
  private String location;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private ChampionshipStatus status = ChampionshipStatus.PLANNED;

  @ManyToMany
  @JoinTable(
      name = "championship_teams",
      joinColumns = @JoinColumn(name = "championship_id"),
      inverseJoinColumns = @JoinColumn(name = "team_id"))
  private Set<Team> participatingTeams = new HashSet<>();

  @OneToMany(mappedBy = "championship")
  private Set<Game> games = new HashSet<>();

  public Championship() {}

  public Championship(String name, LocalDate startDate, String location) {
    this.name = name;
    this.startDate = startDate;
    this.location = location;
    this.status = ChampionshipStatus.PLANNED;
  }

  // Getters and setters
  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public ChampionshipStatus getStatus() {
    return status;
  }

  public void setStatus(ChampionshipStatus status) {
    this.status = status;
  }

  public Set<Team> getParticipatingTeams() {
    return participatingTeams;
  }

  public void setParticipatingTeams(Set<Team> participatingTeams) {
    this.participatingTeams = participatingTeams;
  }

  public void addTeam(Team team) {
    this.participatingTeams.add(team);
  }

  public void removeTeam(Team team) {
    this.participatingTeams.remove(team);
  }

  public Set<Game> getGames() {
    return games;
  }

  public void addGame(Game game) {
    this.games.add(game);
    game.setChampionship(this);
  }

  public void removeGame(Game game) {
    this.games.remove(game);
    game.setChampionship(null);
  }
}
