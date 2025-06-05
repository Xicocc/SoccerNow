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

  @Operation(summary = "Get referee by name")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Referee found"),
    @ApiResponse(responseCode = "404", description = "Referee not found")
  })
  @GetMapping("/by-name")
  public ResponseEntity<Referee> getRefereeByName(
      @Parameter(description = "Referee's name") @RequestParam String name) {
    return ResponseEntity.ok(refereeService.getRefereeByName(name));
  }

  @Operation(summary = "Get referees by number of games officiated")
  @ApiResponse(responseCode = "200", description = "Referees found")
  @GetMapping("/by-games-officiated")
  public ResponseEntity<List<Referee>> getRefereesByGamesParticipated(
      @Parameter(description = "Exact number of games officiated") @RequestParam int games) {
    return ResponseEntity.ok(refereeService.getRefereesByGamesParticipated(games));
  }

  @Operation(summary = "Get referees by number of cards shown")
  @ApiResponse(responseCode = "200", description = "Referees found")
  @GetMapping("/by-cards-shown")
  public ResponseEntity<List<Referee>> getRefereesByCardsShown(
      @Parameter(description = "Exact number of cards shown") @RequestParam int cards) {
    return ResponseEntity.ok(refereeService.getRefereesByCardsShown(cards));
  }

  @Operation(summary = "Add cards shown to a referee")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Cards added successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid input"),
    @ApiResponse(responseCode = "404", description = "Referee not found")
  })
  @PatchMapping("/{id}/cards-shown")
  public ResponseEntity<Referee> addCardsShown(
      @Parameter(description = "ID of the referee") @PathVariable Long id,
      @Parameter(description = "Number of cards to add") @RequestParam int cards) {
    return ResponseEntity.ok(refereeService.addCardsShown(id, cards));
  }
}
