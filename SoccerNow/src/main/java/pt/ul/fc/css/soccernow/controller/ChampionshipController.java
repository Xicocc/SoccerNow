package pt.ul.fc.css.soccernow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.soccernow.dto.ChampionshipRegistrationDTO;
import pt.ul.fc.css.soccernow.model.Championship;
import pt.ul.fc.css.soccernow.model.ChampionshipStatus;
import pt.ul.fc.css.soccernow.services.ChampionshipService;

@RestController
@RequestMapping("/api/championships")
@Tag(name = "Championship", description = "Championship management endpoints")
public class ChampionshipController {

  private final ChampionshipService championshipService;

  public ChampionshipController(ChampionshipService championshipService) {
    this.championshipService = championshipService;
  }

  @Operation(summary = "Register a new championship")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Championship registered successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid input data")
  })
  @PostMapping
  public ResponseEntity<Championship> registerChampionship(
      @Parameter(description = "Championship registration data") @RequestBody
          ChampionshipRegistrationDTO dto) {
    return ResponseEntity.ok(championshipService.registerChampionship(dto));
  }

  @Operation(summary = "Get championship by ID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Championship found"),
    @ApiResponse(responseCode = "404", description = "Championship not found")
  })
  @GetMapping("/{id}")
  public ResponseEntity<Championship> getChampionshipById(
      @Parameter(description = "ID of the championship") @PathVariable Long id) {
    return ResponseEntity.ok(championshipService.getChampionshipById(id));
  }

  @Operation(summary = "Get all championships")
  @ApiResponse(responseCode = "200", description = "List of all championships")
  @GetMapping
  public ResponseEntity<List<Championship>> getAllChampionships() {
    return ResponseEntity.ok(championshipService.getAllChampionships());
  }

  @Operation(summary = "Get championships by status")
  @ApiResponse(responseCode = "200", description = "List of championships with the given status")
  @GetMapping("/status/{status}")
  public ResponseEntity<List<Championship>> getChampionshipsByStatus(
      @Parameter(
              description = "Status of the championships (PLANNED, ONGOING, COMPLETED, CANCELLED)")
          @PathVariable
          ChampionshipStatus status) {
    return ResponseEntity.ok(championshipService.getChampionshipsByStatus(status));
  }

  @Operation(summary = "Get championships a team is participating in")
  @ApiResponse(responseCode = "200", description = "List of championships for the team")
  @GetMapping("/by-team/{teamId}")
  public ResponseEntity<List<Championship>> getChampionshipsByTeam(
      @Parameter(description = "ID of the team") @PathVariable Long teamId) {
    return ResponseEntity.ok(championshipService.getChampionshipsByTeam(teamId));
  }

  @Operation(summary = "Update championship status")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Championship status updated"),
    @ApiResponse(responseCode = "404", description = "Championship not found")
  })
  @PatchMapping("/{id}/status")
  public ResponseEntity<Championship> updateStatus(
      @Parameter(description = "ID of the championship") @PathVariable Long id,
      @RequestParam ChampionshipStatus status) {
    return ResponseEntity.ok(championshipService.updateStatus(id, status));
  }

  @Operation(summary = "Delete a championship")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Championship deleted successfully"),
    @ApiResponse(responseCode = "404", description = "Championship not found")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteChampionship(
      @Parameter(description = "ID of the championship") @PathVariable Long id) {
    championshipService.deleteChampionship(id);
    return ResponseEntity.ok().build();
  }
}
