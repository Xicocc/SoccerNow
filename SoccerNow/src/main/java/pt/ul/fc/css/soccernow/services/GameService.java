package pt.ul.fc.css.soccernow.services;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.soccernow.dto.GameRegistrationDTO;
import pt.ul.fc.css.soccernow.exceptions.GameNotFoundException;
import pt.ul.fc.css.soccernow.exceptions.TeamNotFoundException;
import pt.ul.fc.css.soccernow.model.Game;
import pt.ul.fc.css.soccernow.model.GameStatus;
import pt.ul.fc.css.soccernow.model.Team;
import pt.ul.fc.css.soccernow.repository.GameRepository;
import pt.ul.fc.css.soccernow.repository.TeamRepository;

@Service
public class GameService {

  private final GameRepository gameRepository;
  private final TeamRepository teamRepository;

  public GameService(GameRepository gameRepository, TeamRepository teamRepository) {
    this.gameRepository = gameRepository;
    this.teamRepository = teamRepository;
  }

  // Register a new game
  public Game registerGame(GameRegistrationDTO dto) {
    if (dto == null || dto.getHomeTeamName() == null || dto.getAwayTeamName() == null) {
      throw new IllegalArgumentException("Both team names must be provided");
    }

    Team homeTeam =
        teamRepository
            .findByName(dto.getHomeTeamName())
            .orElseThrow(() -> new TeamNotFoundException(dto.getHomeTeamName()));

    Team awayTeam =
        teamRepository
            .findByName(dto.getAwayTeamName())
            .orElseThrow(() -> new TeamNotFoundException(dto.getAwayTeamName()));

    if (homeTeam.equals(awayTeam)) {
      throw new IllegalArgumentException("A team cannot play against itself");
    }

    LocalDateTime gameTime = dto.getGameTime() != null ? dto.getGameTime() : LocalDateTime.now();

    Game game = new Game(homeTeam, awayTeam, gameTime, dto.getLocation());
    return gameRepository.save(game);
  }

  // Get all games a team participated in by team name
  public List<Game> getGamesByTeamName(String teamName) {
    if (teamName == null || teamName.isBlank()) {
      throw new IllegalArgumentException("Team name cannot be empty");
    }
    return gameRepository.findByTeamName(teamName.trim());
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

  // Optional: get all scheduled/completed/cancelled games
  public List<Game> getScheduledGames() {
    return gameRepository.findScheduledGames();
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
}
