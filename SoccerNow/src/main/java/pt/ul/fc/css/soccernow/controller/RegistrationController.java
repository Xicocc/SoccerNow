package pt.ul.fc.css.soccernow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.soccernow.dto.PlayerRegistrationDTO;
import pt.ul.fc.css.soccernow.dto.RefereeRegistrationDTO;
import pt.ul.fc.css.soccernow.model.Player;
import pt.ul.fc.css.soccernow.model.Referee;
import pt.ul.fc.css.soccernow.services.PlayerService;
import pt.ul.fc.css.soccernow.services.RefereeService;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User registration endpoints")
public class RegistrationController {
  private final PlayerService playerService;
  private final RefereeService refereeService;

  public RegistrationController(PlayerService playerService, RefereeService refereeService) {
    this.playerService = playerService;
    this.refereeService = refereeService;
  }

  @Operation(summary = "Register a new player")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Player registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Username/email already exists")
      })
  @PostMapping("/register/player")
  public ResponseEntity<Player> registerPlayer(
      @Parameter(description = "Player registration data", required = true) @Valid @RequestBody
          PlayerRegistrationDTO registrationDTO) {
    return ResponseEntity.ok(playerService.registerPlayer(registrationDTO));
  }

  @Operation(summary = "Register a new referee")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Referee registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Username/email already exists")
      })
  @PostMapping("/register/referee")
  public ResponseEntity<Referee> registerReferee(
      @Parameter(description = "Referee registration data", required = true) @Valid @RequestBody
          RefereeRegistrationDTO registrationDTO) {
    return ResponseEntity.ok(refereeService.registerReferee(registrationDTO));
  }
}
