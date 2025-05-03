package pt.ul.fc.css.soccernow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.soccernow.model.Referee;
import pt.ul.fc.css.soccernow.services.RefereeService;

@RestController
@RequestMapping("/api/referees")
@Tag(name = "Referee", description = "Referee endpoints")
public class RefereeController {
  private final RefereeService refereeService;

  public RefereeController(RefereeService refereeService) {
    this.refereeService = refereeService;
  }

  @Operation(summary = "Get all referees")
  @ApiResponse(responseCode = "200", description = "List of all referees")
  @GetMapping
  public ResponseEntity<List<Referee>> getAllReferees() {
    return ResponseEntity.ok(refereeService.getAllReferees());
  }

  @Operation(summary = "Get top referees")
  @ApiResponse(responseCode = "200", description = "List of most ecperienced referees")
  @GetMapping("/top")
  public Referee getTopReferee() {
    return refereeService.getTopReferee();
  }

  @Operation(summary = "Add games participated to a referee")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Games added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Referee not found")
      })
  @PatchMapping("/{id}/games-participated")
  public ResponseEntity<Referee> addGamesParticipated(
      @Parameter(description = "ID of the referee") @PathVariable Long id,
      @Parameter(description = "Number of games to add") @RequestParam int games) {
    return ResponseEntity.ok(refereeService.addGamesParticipated(id, games));
  }
}
