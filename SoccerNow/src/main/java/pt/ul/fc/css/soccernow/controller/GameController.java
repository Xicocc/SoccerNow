package pt.ul.fc.css.soccernow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.soccernow.dto.GameRegistrationDTO;
import pt.ul.fc.css.soccernow.model.Game;
import pt.ul.fc.css.soccernow.services.GameService;

@RestController
@RequestMapping("/api/games")
@Tag(name = "Game", description = "Game management endpoints")
public class GameController {

  private final GameService gameService;

  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @Operation(summary = "Register a new game (with or without championship)")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Game registered successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid input or team conflict"),
    @ApiResponse(responseCode = "404", description = "Team or championship not found")
  })
  @PostMapping
  public ResponseEntity<Game> registerGame(
      @Parameter(description = "Game registration data") @RequestBody GameRegistrationDTO dto) {
    return ResponseEntity.ok(gameService.registerGame(dto));
  }

  @Operation(summary = "Get games by team name")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "List of games"),
    @ApiResponse(responseCode = "400", description = "Invalid team name")
  })
  @GetMapping("/by-team/{teamName}")
  public ResponseEntity<List<Game>> getGamesByTeamName(
      @Parameter(description = "Name of the team") @PathVariable String teamName) {
    return ResponseEntity.ok(gameService.getGamesByTeamName(teamName));
  }

  @Operation(summary = "Get games by championship ID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "List of games in the championship"),
    @ApiResponse(responseCode = "400", description = "Invalid championship ID")
  })
  @GetMapping("/by-championship/{championshipId}")
  public ResponseEntity<List<Game>> getGamesByChampionship(
      @Parameter(description = "ID of the championship") @PathVariable Long championshipId) {
    return ResponseEntity.ok(gameService.getGamesByChampionship(championshipId));
  }

  @Operation(summary = "Get standalone games (not in any championship)")
  @ApiResponse(responseCode = "200", description = "List of standalone games")
  @GetMapping("/standalone")
  public ResponseEntity<List<Game>> getStandaloneGames() {
    return ResponseEntity.ok(gameService.getStandaloneGames());
  }

  @Operation(summary = "Get scheduled games for a championship")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "List of scheduled games"),
    @ApiResponse(responseCode = "400", description = "Invalid championship ID")
  })
  @GetMapping("/scheduled/by-championship/{championshipId}")
  public ResponseEntity<List<Game>> getScheduledGamesByChampionship(
      @Parameter(description = "ID of the championship") @PathVariable Long championshipId) {
    return ResponseEntity.ok(gameService.getScheduledGamesByChampionship(championshipId));
  }

  @Operation(summary = "Update the result of a game")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Game result updated"),
    @ApiResponse(responseCode = "400", description = "Invalid score"),
    @ApiResponse(responseCode = "404", description = "Game not found")
  })
  @PatchMapping("/{id}/result")
  public ResponseEntity<Game> updateGameResult(
      @Parameter(description = "ID of the game") @PathVariable Long id,
      @RequestParam int homeScore,
      @RequestParam int awayScore) {
    return ResponseEntity.ok(gameService.updateResult(id, homeScore, awayScore));
  }

  @Operation(summary = "Cancel a game")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Game cancelled successfully"),
    @ApiResponse(responseCode = "404", description = "Game not found")
  })
  @PatchMapping("/{id}/cancel")
  public ResponseEntity<Game> cancelGame(
      @Parameter(description = "ID of the game") @PathVariable Long id) {
    return ResponseEntity.ok(gameService.cancelGame(id));
  }

  @Operation(summary = "Get all games")
  @ApiResponse(responseCode = "200", description = "List of all games")
  @GetMapping
  public ResponseEntity<List<Game>> getAllGames() {
    return ResponseEntity.ok(gameService.getAllGames());
  }

  @Operation(summary = "Get scheduled games")
  @ApiResponse(responseCode = "200", description = "List of scheduled games")
  @GetMapping("/scheduled")
  public ResponseEntity<List<Game>> getScheduledGames() {
    return ResponseEntity.ok(gameService.getScheduledGames());
  }

  @Operation(summary = "Get completed games")
  @ApiResponse(responseCode = "200", description = "List of completed games")
  @GetMapping("/completed")
  public ResponseEntity<List<Game>> getCompletedGames() {
    return ResponseEntity.ok(gameService.getCompletedGames());
  }

  @Operation(summary = "Get cancelled games")
  @ApiResponse(responseCode = "200", description = "List of cancelled games")
  @GetMapping("/cancelled")
  public ResponseEntity<List<Game>> getCancelledGames() {
    return ResponseEntity.ok(gameService.getCancelledGames());
  }

  @Operation(summary = "Get games that have been played")
  @ApiResponse(responseCode = "200", description = "List of completed games")
  @GetMapping("/played")
  public ResponseEntity<List<Game>> getPlayedGames() {
    return ResponseEntity.ok(gameService.getPlayedGames());
  }

  @Operation(summary = "Get upcoming (scheduled) games")
  @ApiResponse(responseCode = "200", description = "List of scheduled games")
  @GetMapping("/upcoming")
  public ResponseEntity<List<Game>> getUpcomingGames() {
    return ResponseEntity.ok(gameService.getUpcomingGames());
  }

  @Operation(summary = "Get games with a specific total number of goals")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "List of games with given goal total"),
    @ApiResponse(responseCode = "400", description = "Invalid goal count")
  })
  @GetMapping("/by-goals")
  public ResponseEntity<List<Game>> getGamesByGoals(
      @Parameter(description = "Total number of goals") @RequestParam int goals) {
    if (goals < 0) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(gameService.getGamesByTotalGoals(goals));
  }

  @Operation(summary = "Get games by location")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "List of games in the location"),
    @ApiResponse(responseCode = "400", description = "Invalid location")
  })
  @GetMapping("/by-location")
  public ResponseEntity<List<Game>> getGamesByLocation(
      @Parameter(description = "Location name") @RequestParam String location) {
    if (location == null || location.isBlank()) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(gameService.getGamesByLocation(location.trim()));
  }

  @Operation(summary = "Get morning games (before 12:00)")
  @ApiResponse(responseCode = "200", description = "List of morning games")
  @GetMapping("/morning")
  public ResponseEntity<List<Game>> getMorningGames() {
    return ResponseEntity.ok(gameService.getMorningGames());
  }

  @Operation(summary = "Get afternoon games (12:00–17:59)")
  @ApiResponse(responseCode = "200", description = "List of afternoon games")
  @GetMapping("/afternoon")
  public ResponseEntity<List<Game>> getAfternoonGames() {
    return ResponseEntity.ok(gameService.getAfternoonGames());
  }

  @Operation(summary = "Get evening games (18:00 and later)")
  @ApiResponse(responseCode = "200", description = "List of evening games")
  @GetMapping("/evening")
  public ResponseEntity<List<Game>> getEveningGames() {
    return ResponseEntity.ok(gameService.getEveningGames());
  }

  @PatchMapping("/{id}/location")
  @Operation(summary = "Update the location of a game")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Game location updated"),
    @ApiResponse(responseCode = "400", description = "Invalid location"),
    @ApiResponse(responseCode = "404", description = "Game not found")
  })
  public ResponseEntity<Game> updateGameLocation(
      @Parameter(description = "ID of the game") @PathVariable Long id,
      @RequestParam String location) {
    return ResponseEntity.ok(gameService.updateGameLocation(id, location));
  }
}
