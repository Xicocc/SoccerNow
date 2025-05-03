package pt.ul.fc.css.soccernow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.soccernow.model.Player;
import pt.ul.fc.css.soccernow.model.Position;
import pt.ul.fc.css.soccernow.service.PlayerService;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
  private final PlayerService playerService;

  public PlayerController(PlayerService playerService) {
    this.playerService = playerService;
  }

  @GetMapping("/avg-goals")
  public Double getAverageGoals(@RequestParam String name) {
    return playerService.getAverageGoals(name);
  }

  @GetMapping("/by-position")
  public List<Player> getPlayersByPosition(@RequestParam Position position) {
    return playerService.getPlayersByPosition(position);
  }

  @GetMapping("/most-red-cards")
  public List<Player> getPlayersWithMostRedCards() {
    return playerService.getPlayersWithMostRedCards();
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
}
