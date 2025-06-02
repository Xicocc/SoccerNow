package pt.ul.fc.css.soccernow.services;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.soccernow.dto.GameRegistrationDTO;
import pt.ul.fc.css.soccernow.exceptions.ChampionshipNotFoundException;
import pt.ul.fc.css.soccernow.exceptions.GameNotFoundException;
import pt.ul.fc.css.soccernow.exceptions.TeamNotFoundException;
import pt.ul.fc.css.soccernow.model.Championship;
import pt.ul.fc.css.soccernow.model.Game;
import pt.ul.fc.css.soccernow.model.GameStatus;
import pt.ul.fc.css.soccernow.model.Team;
import pt.ul.fc.css.soccernow.repository.ChampionshipRepository;
import pt.ul.fc.css.soccernow.repository.GameRepository;
import pt.ul.fc.css.soccernow.repository.TeamRepository;

@Service
public class GameService {

  private final GameRepository gameRepository;
  private final TeamRepository teamRepository;
  private final ChampionshipRepository championshipRepository;

  public GameService(
      GameRepository gameRepository,
      TeamRepository teamRepository,
      ChampionshipRepository championshipRepository) {
    this.gameRepository = gameRepository;
    this.teamRepository = teamRepository;
    this.championshipRepository = championshipRepository;
  }

  // Register a new game (can be with or without championship)
  public Game registerGame(GameRegistrationDTO dto) {
    validateGameDTO(dto);

    Team homeTeam = getTeamByName(dto.getHomeTeamName());
    Team awayTeam = getTeamByName(dto.getAwayTeamName());

    validateTeams(homeTeam, awayTeam);

    LocalDateTime gameTime = dto.getGameTime() != null ? dto.getGameTime() : LocalDateTime.now();
    Game game = new Game(homeTeam, awayTeam, gameTime, dto.getLocation());

    // Set championship if provided
    if (dto.getChampionshipId() != null) {
      Championship championship =
          championshipRepository
              .findById(dto.getChampionshipId())
              .orElseThrow(() -> new ChampionshipNotFoundException(dto.getChampionshipId()));
      game.setChampionship(championship);
    }

    return gameRepository.save(game);
  }

  // Get all games a team participated in by team name
  public List<Game> getGamesByTeamName(String teamName) {
    if (teamName == null || teamName.isBlank()) {
      throw new IllegalArgumentException("Team name cannot be empty");
    }
    return gameRepository.findByTeamName(teamName.trim());
  }

  // Get games by championship
  public List<Game> getGamesByChampionship(Long championshipId) {
    if (championshipId == null) {
      throw new IllegalArgumentException("Championship ID cannot be null");
    }
    return gameRepository.findByChampionshipId(championshipId);
  }

  // Get standalone games (not part of any championship)
  public List<Game> getStandaloneGames() {
    return gameRepository.findStandaloneGames();
  }

  // Update the result of a game
  public Game updateResult(Long gameId, int homeScore, int awayScore) {
    if (homeScore < 0 || awayScore < 0) {
      throw new IllegalArgumentException("Scores must be non-negative");
    }

    Game game = getGameById(gameId);
    game.setHomeScore(homeScore);
    game.setAwayScore(awayScore);
    game.setStatus(GameStatus.COMPLETED);
    return gameRepository.save(game);
  }

  // Cancel a game
  public Game cancelGame(Long gameId) {
    Game game = getGameById(gameId);
    game.setStatus(GameStatus.CANCELLED);
    return gameRepository.save(game);
  }

  // Get game by ID with error handling
  public Game getGameById(Long gameId) {
    return gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
  }

  // Get all scheduled/completed/cancelled games
  public List<Game> getScheduledGames() {
    return gameRepository.findScheduledGames();
  }

  public List<Game> getScheduledGamesByChampionship(Long championshipId) {
    return gameRepository.findScheduledGamesByChampionship(championshipId);
  }

  public List<Game> getCompletedGames() {
    return gameRepository.findCompletedGames();
  }

  public List<Game> getCancelledGames() {
    return gameRepository.findCancelledGames();
  }

  public List<Game> getAllGames() {
    return gameRepository.findAll();
  }

  // Helper methods
  private void validateGameDTO(GameRegistrationDTO dto) {
    if (dto == null || dto.getHomeTeamName() == null || dto.getAwayTeamName() == null) {
      throw new IllegalArgumentException("Both team names must be provided");
    }
  }

  private Team getTeamByName(String teamName) {
    return teamRepository
        .findByName(teamName)
        .orElseThrow(() -> new TeamNotFoundException(teamName));
  }

  private void validateTeams(Team homeTeam, Team awayTeam) {
    if (homeTeam.equals(awayTeam)) {
      throw new IllegalArgumentException("A team cannot play against itself");
    }
  }
}
