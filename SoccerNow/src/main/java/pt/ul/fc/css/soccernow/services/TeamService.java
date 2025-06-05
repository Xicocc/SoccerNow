package pt.ul.fc.css.soccernow.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.soccernow.dto.TeamRegistrationDTO;
import pt.ul.fc.css.soccernow.exceptions.TeamNotFoundException;
import pt.ul.fc.css.soccernow.model.Player;
import pt.ul.fc.css.soccernow.model.Position;
import pt.ul.fc.css.soccernow.model.Team;
import pt.ul.fc.css.soccernow.model.User;
import pt.ul.fc.css.soccernow.repository.PlayerRepository;
import pt.ul.fc.css.soccernow.repository.TeamRepository;
import pt.ul.fc.css.soccernow.repository.UserRepository;

@Service
public class TeamService {
  private final TeamRepository teamRepository;
  private final PlayerRepository playerRepository;
  private final UserRepository userRepository;

  public TeamService(
      TeamRepository teamRepository,
      PlayerRepository playerRepository,
      UserRepository userRepository) {
    this.teamRepository = teamRepository;
    this.playerRepository = playerRepository;
    this.userRepository = userRepository;
  }

  public Team registerTeam(TeamRegistrationDTO dto) {
    if (dto == null || dto.getName() == null || dto.getName().trim().isEmpty()) {
      throw new IllegalArgumentException("Team name cannot be empty");
    }

    String name = dto.getName().trim();

    if (teamRepository.existsByName(name)) {
      throw new IllegalArgumentException("Team name already exists");
    }

    if (userRepository.existsByName(name)) {
      throw new IllegalArgumentException("Name conflicts with existing user");
    }

    Team team = new Team();
    team.setName(name);
    team.setPlayers(new ArrayList<>());
    return teamRepository.save(team);
  }

  public List<Team> getAllTeams() {
    return teamRepository.findAllTeams();
  }

  public Team getTeamById(Long teamId) {
    return teamRepository.findById(teamId).orElseThrow(() -> new TeamNotFoundException(teamId));
  }

  public Team addPlayerToTeam(Long teamId, Long userId) {
    Team team = getTeamById(teamId);
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (!(user instanceof Player)) {
      throw new IllegalArgumentException("User is not a Player");
    }

    Player player = (Player) user;
    if (!team.getPlayers().contains(player)) {
      team.getPlayers().add(player);
      return teamRepository.save(team);
    }
    return team;
  }

  public Team removePlayerFromTeam(Long teamId, Long userId) {
    Team team = getTeamById(teamId);
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (!(user instanceof Player)) {
      throw new IllegalArgumentException("User is not a Player");
    }

    Player player = (Player) user;
    team.getPlayers().remove(player);
    return teamRepository.save(team);
  }

  public List<Player> getTeamPlayers(Long teamId) {
    return getTeamById(teamId).getPlayers();
  }

  public List<Team> getTeamsWithPlayer(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (!(user instanceof Player)) {
      throw new IllegalArgumentException("User is not a Player");
    }

    return teamRepository.findByPlayerId(userId);
  }

  public List<Team> getTeamsWithLessThan5Players() {
    return teamRepository.findTeamsWithLessThan5Players();
  }

  public List<Team> getTeamsWithMostCards() {
    return teamRepository.findTeamsWithMostCards();
  }

  public List<Team> getTeamsWithMostWins() {
    return teamRepository.findTeamsWithMostWins();
  }

  public Team addWins(Long teamId, int wins) {
    if (wins <= 0) {
      throw new IllegalArgumentException("Wins must be a positive number");
    }

    Team team = getTeamById(teamId);
    team.setWins(team.getWins() + wins);
    return teamRepository.save(team);
  }

  public int getTeamCardCount(Long teamId) {
    return getTeamById(teamId).getPlayers().stream()
        .mapToInt(p -> p.getYellowCards() + p.getRedCards())
        .sum();
  }

  public List<Team> getTeamsWithPlayerName(String playerName) {
    return teamRepository.findByPlayerName(playerName);
  }

  public void removeTeam(Long teamId) {
    Team team = getTeamById(teamId);
    teamRepository.delete(team);
  }

  public List<Team> getTeamsByNumberOfPlayers(int count) {
    return teamRepository.findByNumberOfPlayers(count);
  }

  public List<Team> getTeamsByWins(int wins) {
    return teamRepository.findByWins(wins);
  }

  public List<Team> getTeamsByDraws(int draws) {
    return teamRepository.findByDraws(draws);
  }

  public List<Team> getTeamsByLosses(int losses) {
    return teamRepository.findByLosses(losses);
  }

  public List<Team> getTeamsByTitles(int titles) {
    return teamRepository.findByTitles(titles);
  }

  public List<Team> getTeamsMissingPosition(Position position) {
    return teamRepository.findTeamsMissingPosition(position);
  }

  public Team addDraws(Long teamId, int draws) {
    if (draws <= 0) {
      throw new IllegalArgumentException("Draws must be a positive number");
    }

    Team team = getTeamById(teamId);
    team.setDraws(team.getDraws() + draws);
    return teamRepository.save(team);
  }

  public Team addLosses(Long teamId, int losses) {
    if (losses <= 0) {
      throw new IllegalArgumentException("Losses must be a positive number");
    }

    Team team = getTeamById(teamId);
    team.setLosses(team.getLosses() + losses);
    return teamRepository.save(team);
  }

  public Team addTitles(Long teamId, int titles) {
    if (titles <= 0) {
      throw new IllegalArgumentException("Titles must be a positive number");
    }

    Team team = getTeamById(teamId);
    team.setTitles(team.getTitles() + titles);
    return teamRepository.save(team);
  }

  public List<Team> filterTeams(
      String name,
      Integer numPlayers,
      Integer minWins,
      Integer minDraws,
      Integer minLosses,
      Integer minTitles,
      String missingPosition) {
    return teamRepository.findAll().stream()
        .filter(team -> name == null || team.getName().toLowerCase().contains(name.toLowerCase()))
        .filter(team -> numPlayers == null || team.getPlayers().size() == numPlayers)
        .filter(team -> minWins == null || team.getWins() >= minWins)
        .filter(team -> minDraws == null || team.getDraws() >= minDraws)
        .filter(team -> minLosses == null || team.getLosses() >= minLosses)
        .filter(team -> minTitles == null || team.getTitles() >= minTitles)
        .filter(
            team ->
                missingPosition == null
                    || team.getPlayers().stream()
                        .noneMatch(
                            p -> p.getPreferredPosition().name().equalsIgnoreCase(missingPosition)))
        .collect(Collectors.toList());
  }
}
