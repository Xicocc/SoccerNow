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

  @Operation(summary = "Register a new game")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Game registered successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid input or team conflict")
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
}
