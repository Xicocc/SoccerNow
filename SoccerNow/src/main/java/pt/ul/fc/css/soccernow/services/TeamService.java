package pt.ul.fc.css.soccernow.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.soccernow.dto.TeamRegistrationDTO;
import pt.ul.fc.css.soccernow.exceptions.TeamNotFoundException;
import pt.ul.fc.css.soccernow.model.Player;
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
    // Validate input
    if (dto == null || dto.getName() == null || dto.getName().trim().isEmpty()) {
      throw new IllegalArgumentException("Team name cannot be empty");
    }

    String name = dto.getName().trim();

    // Case-insensitive check for existing team
    if (teamRepository.existsByName(name)) {
      throw new IllegalArgumentException("Team name already exists");
    }

    // Check against user names (case-sensitive as per your User model)
    if (userRepository.existsByName(name)) {
      throw new IllegalArgumentException("Name conflicts with existing user");
    }

    Team team = new Team();
    team.setName(name);
    team.setPlayers(new ArrayList<>());
    return teamRepository.save(team);
  }

  // Get all teams
  public List<Team> getAllTeams() {
    return teamRepository.findAllTeams();
  }

  // Get team by ID with not found handling
  public Team getTeamById(Long teamId) {
    return teamRepository.findById(teamId).orElseThrow(() -> new TeamNotFoundException(teamId));
  }

  // Add existing Player (User subclass) to team
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

  // Remove player from team
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

  // Get all players in a team
  public List<Player> getTeamPlayers(Long teamId) {
    return getTeamById(teamId).getPlayers();
  }

  // Get teams with specific player
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

  // Teams with less than 5 players
  public List<Team> getTeamsWithLessThan5Players() {
    return teamRepository.findTeamsWithLessThan5Players();
  }

  // Teams ordered by total cards (yellow + red)
  public List<Team> getTeamsWithMostCards() {
    return teamRepository.findTeamsWithMostCards();
  }

  // Teams ordered by wins
  public List<Team> getTeamsWithMostWins() {
    return teamRepository.findTeamsWithMostWins();
  }

  // Increment team's win count
  public Team addWins(Long teamId, int wins) {
    if (wins <= 0) {
      throw new IllegalArgumentException("Wins must be a positive number");
    }

    Team team = getTeamById(teamId);
    team.setWins(team.getWins() + wins); // Add multiple wins at once
    return teamRepository.save(team);
  }

  // Calculate total cards for a team
  public int getTeamCardCount(Long teamId) {
    return getTeamById(teamId).getPlayers().stream()
        .mapToInt(p -> p.getYellowCards() + p.getRedCards())
        .sum();
  }

  // Get teams where player name matches
  public List<Team> getTeamsWithPlayerName(String playerName) {
    return teamRepository.findByPlayerName(playerName);
  }
}
