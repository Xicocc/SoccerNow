package pt.ul.fc.css.soccernow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.soccernow.dto.TeamRegistrationDTO;
import pt.ul.fc.css.soccernow.model.Player;
import pt.ul.fc.css.soccernow.model.Team;
import pt.ul.fc.css.soccernow.services.PlayerService;
import pt.ul.fc.css.soccernow.services.TeamService;

@RestController
@RequestMapping("/api/teams")
@Tag(name = "Team", description = "Team management endpoints")
public class TeamController {
  private final TeamService teamService;
  private final PlayerService playerService;

  public TeamController(TeamService teamService, PlayerService playerService) {
    this.teamService = teamService;
    this.playerService = playerService;
  }

  @Operation(summary = "Register a new team")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Team registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or name exists")
      })
  @PostMapping
  public ResponseEntity<Team> registerTeam(
      @Parameter(description = "Team registration data") @RequestBody TeamRegistrationDTO dto) {
    return ResponseEntity.ok(teamService.registerTeam(dto));
  }

  @Operation(summary = "Get all teams")
  @ApiResponse(responseCode = "200", description = "List of all teams")
  @GetMapping
  public ResponseEntity<List<Team>> getAllTeams() {
    return ResponseEntity.ok(teamService.getAllTeams());
  }

  @Operation(summary = "Get team by ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Team found"),
        @ApiResponse(responseCode = "404", description = "Team not found")
      })
  @GetMapping("/{id}")
  public ResponseEntity<Team> getTeamById(
      @Parameter(description = "ID of the team") @PathVariable Long id) {
    return ResponseEntity.ok(teamService.getTeamById(id));
  }

  @Operation(summary = "Get teams with less than 5 players")
  @ApiResponse(responseCode = "200", description = "List of small teams")
  @GetMapping("/small-teams")
  public ResponseEntity<List<Team>> getTeamsWithLessThan5Players() {
    return ResponseEntity.ok(teamService.getTeamsWithLessThan5Players());
  }

  @Operation(summary = "Get teams ordered by most cards")
  @ApiResponse(responseCode = "200", description = "List of teams ordered by cards")
  @GetMapping("/most-cards")
  public ResponseEntity<List<Team>> getTeamsWithMostCards() {
    return ResponseEntity.ok(teamService.getTeamsWithMostCards());
  }

  @Operation(summary = "Get teams ordered by most wins")
  @ApiResponse(responseCode = "200", description = "List of teams ordered by wins")
  @GetMapping("/most-wins")
  public ResponseEntity<List<Team>> getTeamsWithMostWins() {
    return ResponseEntity.ok(teamService.getTeamsWithMostWins());
  }

  @Operation(summary = "Add wins to a team")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Wins added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid win count"),
        @ApiResponse(responseCode = "404", description = "Team not found")
      })
  @PatchMapping("/{id}/wins")
  public ResponseEntity<Team> addWins(
      @Parameter(description = "ID of the team") @PathVariable Long id,
      @Parameter(description = "Number of wins to add") @RequestParam int wins) {
    return ResponseEntity.ok(teamService.addWins(id, wins));
  }

  @Operation(summary = "Add player to team")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Player added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid player"),
        @ApiResponse(responseCode = "404", description = "Team not found")
      })
  @PatchMapping("/{teamId}/players/{playerId}")
  public ResponseEntity<String> addPlayerToTeam(
      @Parameter(description = "ID of the team") @PathVariable Long teamId,
      @Parameter(description = "ID of the player") @PathVariable Long playerId) {
    Team team = teamService.getTeamById(teamId);
    Player player = playerService.getPlayerById(playerId);
    if (team.getPlayers().contains(player)) {
      return ResponseEntity.badRequest().body("Player is already in the team");
    }
    teamService.addPlayerToTeam(teamId, playerId);
    return ResponseEntity.ok("Player added successfully");
  }

  @Operation(summary = "Remove player from team")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Player removed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid player"),
        @ApiResponse(responseCode = "404", description = "Team not found")
      })
  @DeleteMapping("/{teamId}/players/{playerId}")
  public ResponseEntity<Team> removePlayerFromTeam(
      @Parameter(description = "ID of the team") @PathVariable Long teamId,
      @Parameter(description = "ID of the player") @PathVariable Long playerId) {
    return ResponseEntity.ok(teamService.removePlayerFromTeam(teamId, playerId));
  }

  @Operation(summary = "Get players in a team")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "List of players"),
        @ApiResponse(responseCode = "404", description = "Team not found")
      })
  @GetMapping("/{id}/players")
  public ResponseEntity<List<Player>> getTeamPlayers(
      @Parameter(description = "ID of the team") @PathVariable Long id) {
    return ResponseEntity.ok(teamService.getTeamPlayers(id));
  }

  @Operation(summary = "Get teams containing a specific player")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "List of teams"),
        @ApiResponse(responseCode = "404", description = "Player not found")
      })
  @GetMapping("/by-player/{playerId}")
  public ResponseEntity<List<Team>> getTeamsWithPlayer(
      @Parameter(description = "ID of the player") @PathVariable Long playerId) {
    return ResponseEntity.ok(teamService.getTeamsWithPlayer(playerId));
  }

  @Operation(summary = "Remove a team")
  @DeleteMapping("/{id}/{name}")
  public ResponseEntity<String> removeTeam(
      @Parameter(description = "ID of the team") @PathVariable Long id,
      @Parameter(description = "Team's name") @PathVariable String name) {
    Team team = teamService.getTeamById(id);
    if (!team.getName().equals(name)) {
      return ResponseEntity.badRequest().body("Invalid team name");
    }
    teamService.removeTeam(id);
    return ResponseEntity.ok("Team removed successfully");
  }
}
