package pt.ul.fc.css.soccernow.controller;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.soccernow.model.Championship;
import pt.ul.fc.css.soccernow.model.ChampionshipStatus;
import pt.ul.fc.css.soccernow.model.Game;
import pt.ul.fc.css.soccernow.model.GameStatus;
import pt.ul.fc.css.soccernow.model.Player;
import pt.ul.fc.css.soccernow.model.Referee;
import pt.ul.fc.css.soccernow.model.Team;
import pt.ul.fc.css.soccernow.services.ChampionshipService;
import pt.ul.fc.css.soccernow.services.GameService;
import pt.ul.fc.css.soccernow.services.PlayerService;
import pt.ul.fc.css.soccernow.services.RefereeService;
import pt.ul.fc.css.soccernow.services.TeamService;

@Controller
public class WebPageController {

  @Autowired private PlayerService playerService;
  @Autowired private RefereeService refereeService;
  @Autowired private TeamService teamService;
  @Autowired private GameService gameService;
  @Autowired private ChampionshipService championshipService;

  // Home page (always visible)
  @GetMapping("/")
  public String showIndex() {
    return "index";
  }

  // Dashboard (requires login)
  @GetMapping("/dashboard")
  public String showDashboard(HttpSession session) {
    if (session.getAttribute("user") == null) {
      return "redirect:/login";
    }
    return "dashboard";
  }

  // Login page
  @GetMapping("/login")
  public String showLoginPage(HttpSession session) {
    if (session.getAttribute("user") != null) {
      return "redirect:/dashboard";
    }
    return "login";
  }

  @PostMapping("/login")
  public String doLogin(
      @RequestParam("username") String username, HttpSession session, Model model) {
    if (username == null || username.trim().isEmpty()) {
      model.addAttribute("error", "Username is required");
      return "login";
    }
    session.setAttribute("user", username.trim());
    return "redirect:/dashboard";
  }

  @GetMapping("/logout")
  public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/login";
  }

  // Players page with filters (requires login)
  @GetMapping("/players")
  public String showPlayers(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String position,
      @RequestParam(required = false) Integer minGoals,
      @RequestParam(required = false) Integer maxCards,
      @RequestParam(required = false) Integer minGames,
      HttpSession session,
      Model model) {

    if (session.getAttribute("user") == null) {
      return "redirect:/login";
    }

    List<Player> players =
        playerService.filterPlayers(name, position, minGoals, maxCards, minGames);

    model.addAttribute("players", players);
    model.addAttribute("name", name);
    model.addAttribute("position", position);
    model.addAttribute("minGoals", minGoals);
    model.addAttribute("maxCards", maxCards);
    model.addAttribute("minGames", minGames);

    return "player";
  }

  @GetMapping("/referees")
  public String showReferees(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) Integer minGames,
      @RequestParam(required = false) Integer minCards,
      HttpSession session,
      Model model) {

    if (session.getAttribute("user") == null) {
      return "redirect:/login";
    }

    // You’ll create this filter method in RefereeService
    List<Referee> referees = refereeService.filterReferees(name, minGames, minCards);

    model.addAttribute("referees", referees);
    model.addAttribute("name", name);
    model.addAttribute("minGames", minGames);
    model.addAttribute("minCards", minCards);

    return "referee";
  }

  @GetMapping("/teams")
  public String showTeams(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) Integer numPlayers,
      @RequestParam(required = false) Integer minWins,
      @RequestParam(required = false) Integer minDraws,
      @RequestParam(required = false) Integer minLosses,
      @RequestParam(required = false) Integer minTitles,
      @RequestParam(required = false) String missingPosition,
      HttpSession session,
      Model model) {

    if (session.getAttribute("user") == null) {
      return "redirect:/login";
    }

    List<Team> teams =
        teamService.filterTeams(
            name, numPlayers, minWins, minDraws, minLosses, minTitles, missingPosition);

    model.addAttribute("teams", teams);
    model.addAttribute("name", name);
    model.addAttribute("numPlayers", numPlayers);
    model.addAttribute("minWins", minWins);
    model.addAttribute("minDraws", minDraws);
    model.addAttribute("minLosses", minLosses);
    model.addAttribute("minTitles", minTitles);
    model.addAttribute("missingPosition", missingPosition);

    return "team";
  }

  @GetMapping("/games")
  public String showGames(
      @RequestParam(required = false) String teamName,
      @RequestParam(required = false) Integer minGoals,
      @RequestParam(required = false) String location,
      @RequestParam(required = false) String timeOfDay,
      @RequestParam(required = false) GameStatus status,
      HttpSession session,
      Model model) {

    if (session.getAttribute("user") == null) {
      return "redirect:/login";
    }

    List<Game> games = gameService.filterGames(teamName, minGoals, location, timeOfDay, status);

    model.addAttribute("games", games);
    model.addAttribute("teamName", teamName);
    model.addAttribute("minGoals", minGoals);
    model.addAttribute("location", location);
    model.addAttribute("timeOfDay", timeOfDay);
    model.addAttribute("status", status);

    return "game";
  }

  @GetMapping("/championships")
  public String showChampionships(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String location,
      @RequestParam(required = false) ChampionshipStatus status,
      HttpSession session,
      Model model) {

    if (session.getAttribute("user") == null) {
      return "redirect:/login";
    }

    List<Championship> championships =
        championshipService.filterChampionships(name, location, status);

    model.addAttribute("championships", championships);
    model.addAttribute("name", name);
    model.addAttribute("location", location);
    model.addAttribute("status", status);

    return "championship";
  }
}
