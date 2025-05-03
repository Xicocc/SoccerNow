package pt.ul.fc.css.soccernow.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.soccernow.dto.PlayerRegistrationDTO;
import pt.ul.fc.css.soccernow.dto.RefereeRegistrationDTO;
import pt.ul.fc.css.soccernow.model.Player;
import pt.ul.fc.css.soccernow.model.Referee;
import pt.ul.fc.css.soccernow.service.PlayerService;
import pt.ul.fc.css.soccernow.service.RefereeService;

@RestController
@RequestMapping("/api/auth")
public class RegistrationController {
  private final PlayerService playerService;
  private final RefereeService refereeService;

  public RegistrationController(PlayerService playerService, RefereeService refereeService) {
    this.playerService = playerService;
    this.refereeService = refereeService;
  }

  @PostMapping("/register/player")
  public ResponseEntity<Player> registerPlayer(
      @Valid @RequestBody PlayerRegistrationDTO registrationDTO) {
    return ResponseEntity.ok(playerService.registerPlayer(registrationDTO));
  }

  @PostMapping("/register/referee")
  public ResponseEntity<Referee> registerReferee(
      @Valid @RequestBody RefereeRegistrationDTO registrationDTO) {
    return ResponseEntity.ok(refereeService.registerReferee(registrationDTO));
  }
}
