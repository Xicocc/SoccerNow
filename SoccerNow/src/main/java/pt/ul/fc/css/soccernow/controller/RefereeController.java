package pt.ul.fc.css.soccernow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ul.fc.css.soccernow.model.Referee;
import pt.ul.fc.css.soccernow.service.RefereeService;

@RestController
@RequestMapping("/api/referees")
public class RefereeController {
  private final RefereeService refereeService;

  public RefereeController(RefereeService refereeService) {
    this.refereeService = refereeService;
  }

  @GetMapping("/top")
  public Referee getTopReferee() {
    return refereeService.getTopReferee();
  }
}
