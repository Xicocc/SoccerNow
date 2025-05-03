package pt.ul.fc.css.soccernow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.soccernow.model.Player;
import pt.ul.fc.css.soccernow.model.Position;
import pt.ul.fc.css.soccernow.services.PlayerService;

@RestController
@RequestMapping("/api/players")
@Tag(name = "Player", description = "Player endpoints")
public class PlayerController {
  private final PlayerService playerService;

  public PlayerController(PlayerService playerService) {
    this.playerService = playerService;
  }

  @Operation(summary = "Get all players")
  @ApiResponse(responseCode = "200", description = "List of all players")
  @GetMapping
  public ResponseEntity<List<Player>> getAllPlayers() {
    return ResponseEntity.ok(playerService.getAllPlayers());
  }

  @Operation(summary = "Get player's average goals per game")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Average goals calculated"),
        @ApiResponse(responseCode = "404", description = "Player not found")
      })
  @GetMapping("/avg-goals")
  public ResponseEntity<Double> getAverageGoals(
      @Parameter(description = "Player's name") @RequestParam String name) {
    return ResponseEntity.ok(playerService.getAverageGoals(name));
  }

  @Operation(summary = "Get players by position")
  @ApiResponse(responseCode = "200", description = "List of players in specified position")
  @GetMapping("/by-position")
  public ResponseEntity<List<Player>> getPlayersByPosition(
      @Parameter(description = "Playing position") @RequestParam Position position) {
    return ResponseEntity.ok(playerService.getPlayersByPosition(position));
  }

  @Operation(summary = "Get players with most red cards")
  @ApiResponse(responseCode = "200", description = "List of players ordered by red cards")
  @GetMapping("/most-red-cards")
  public ResponseEntity<List<Player>> getPlayersWithMostRedCards() {
    return ResponseEntity.ok(playerService.getPlayersWithMostRedCards());
  }

  @Operation(summary = "Add goals to a player")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Goals added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Player not found")
      })
  @PatchMapping("/{id}/goals")
  public ResponseEntity<Player> addGoals(
      @Parameter(description = "ID of the player") @PathVariable Long id,
      @Parameter(description = "Number of goals to add") @RequestParam int goals) {
    return ResponseEntity.ok(playerService.addGoals(id, goals));
  }

  @Operation(summary = "Add games played to a player")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Games added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Player not found")
      })
  @PatchMapping("/{id}/games-played")
  public ResponseEntity<Player> addGamesPlayed(
      @Parameter(description = "ID of the player") @PathVariable Long id,
      @Parameter(description = "Number of goals to add") @RequestParam int games) {
    return ResponseEntity.ok(playerService.addGamesPlayed(id, games));
  }

  @Operation(summary = "Add yellow cards to a player")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Yellow Cards added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Player not found")
      })
  @PatchMapping("/{id}/yellow-cards")
  public ResponseEntity<Player> addYellowCards(
      @Parameter(description = "ID of the player") @PathVariable Long id,
      @Parameter(description = "Number of goals to add") @RequestParam int ycards) {
    return ResponseEntity.ok(playerService.addYellowCards(id, ycards));
  }

  @Operation(summary = "Add red cards to a player")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Red Cards added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Player not found")
      })
  @PatchMapping("/{id}/red-cards")
  public ResponseEntity<Player> addRedCards(
      @Parameter(description = "ID of the player") @PathVariable Long id,
      @Parameter(description = "Number of goals to add") @RequestParam int rcards) {
    return ResponseEntity.ok(playerService.addRedCards(id, rcards));
  }
}
