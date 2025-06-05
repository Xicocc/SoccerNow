package pt.ul.fc.css.soccernow.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.soccernow.dto.TeamRegistrationDTO;
import pt.ul.fc.css.soccernow.model.Player;
import pt.ul.fc.css.soccernow.model.Position;
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

  @PostMapping
  public ResponseEntity<Team> registerTeam(@RequestBody TeamRegistrationDTO dto) {
    return ResponseEntity.ok(teamService.registerTeam(dto));
  }

  @GetMapping
  public ResponseEntity<List<Team>> getAllTeams() {
    return ResponseEntity.ok(teamService.getAllTeams());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
    return ResponseEntity.ok(teamService.getTeamById(id));
  }

  @GetMapping("/small-teams")
  public ResponseEntity<List<Team>> getTeamsWithLessThan5Players() {
    return ResponseEntity.ok(teamService.getTeamsWithLessThan5Players());
  }

  @GetMapping("/most-cards")
  public ResponseEntity<List<Team>> getTeamsWithMostCards() {
    return ResponseEntity.ok(teamService.getTeamsWithMostCards());
  }

  @GetMapping("/most-wins")
  public ResponseEntity<List<Team>> getTeamsWithMostWins() {
    return ResponseEntity.ok(teamService.getTeamsWithMostWins());
  }

  @PatchMapping("/{id}/wins")
  public ResponseEntity<Team> addWins(@PathVariable Long id, @RequestParam int wins) {
    return ResponseEntity.ok(teamService.addWins(id, wins));
  }

  @PatchMapping("/{teamId}/players/{playerId}")
  public ResponseEntity<String> addPlayerToTeam(
      @PathVariable Long teamId, @PathVariable Long playerId) {
    Team team = teamService.getTeamById(teamId);
    Player player = playerService.getPlayerById(playerId);
    if (team.getPlayers().contains(player)) {
      return ResponseEntity.badRequest().body("Player is already in the team");
    }
    teamService.addPlayerToTeam(teamId, playerId);
    return ResponseEntity.ok("Player added successfully");
  }

  @DeleteMapping("/{teamId}/players/{playerId}")
  public ResponseEntity<Team> removePlayerFromTeam(
      @PathVariable Long teamId, @PathVariable Long playerId) {
    return ResponseEntity.ok(teamService.removePlayerFromTeam(teamId, playerId));
  }

  @GetMapping("/{id}/players")
  public ResponseEntity<List<Player>> getTeamPlayers(@PathVariable Long id) {
    return ResponseEntity.ok(teamService.getTeamPlayers(id));
  }

  @GetMapping("/by-player/{playerId}")
  public ResponseEntity<List<Team>> getTeamsWithPlayer(@PathVariable Long playerId) {
    return ResponseEntity.ok(teamService.getTeamsWithPlayer(playerId));
  }

  @DeleteMapping("/{id}/{name}")
  public ResponseEntity<String> removeTeam(@PathVariable Long id, @PathVariable String name) {
    Team team = teamService.getTeamById(id);
    if (!team.getName().equals(name)) {
      return ResponseEntity.badRequest().body("Invalid team name");
    }
    teamService.removeTeam(id);
    return ResponseEntity.ok("Team removed successfully");
  }

  @GetMapping("/by-players")
  public ResponseEntity<List<Team>> getTeamsByPlayerCount(@RequestParam int count) {
    return ResponseEntity.ok(teamService.getTeamsByNumberOfPlayers(count));
  }

  @GetMapping("/by-wins")
  public ResponseEntity<List<Team>> getTeamsByWins(@RequestParam int wins) {
    return ResponseEntity.ok(teamService.getTeamsByWins(wins));
  }

  @GetMapping("/by-draws")
  public ResponseEntity<List<Team>> getTeamsByDraws(@RequestParam int draws) {
    return ResponseEntity.ok(teamService.getTeamsByDraws(draws));
  }

  @GetMapping("/by-losses")
  public ResponseEntity<List<Team>> getTeamsByLosses(@RequestParam int losses) {
    return ResponseEntity.ok(teamService.getTeamsByLosses(losses));
  }

  @GetMapping("/by-titles")
  public ResponseEntity<List<Team>> getTeamsByTitles(@RequestParam int titles) {
    return ResponseEntity.ok(teamService.getTeamsByTitles(titles));
  }

  @GetMapping("/missing-position")
  public ResponseEntity<List<Team>> getTeamsMissingPosition(@RequestParam Position position) {
    return ResponseEntity.ok(teamService.getTeamsMissingPosition(position));
  }

  @PatchMapping("/{id}/draws")
  public ResponseEntity<Team> addDraws(@PathVariable Long id, @RequestParam int draws) {
    return ResponseEntity.ok(teamService.addDraws(id, draws));
  }

  @PatchMapping("/{id}/losses")
  public ResponseEntity<Team> addLosses(@PathVariable Long id, @RequestParam int losses) {
    return ResponseEntity.ok(teamService.addLosses(id, losses));
  }

  @PatchMapping("/{id}/titles")
  public ResponseEntity<Team> addTitles(@PathVariable Long id, @RequestParam int titles) {
    return ResponseEntity.ok(teamService.addTitles(id, titles));
  }
}
