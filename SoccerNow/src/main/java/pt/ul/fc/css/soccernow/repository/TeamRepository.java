package pt.ul.fc.css.soccernow.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.model.Position;
import pt.ul.fc.css.soccernow.model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

  // Check if team name exists (case insensitive)
  @Query("SELECT COUNT(t) > 0 FROM Team t WHERE LOWER(t.name) = LOWER(:name)")
  boolean existsByName(String name);

  // Find all teams
  @Query("SELECT t FROM Team t")
  List<Team> findAllTeams();

  // Find team by name
  @Query("SELECT t FROM Team t WHERE t.name = :name")
  Optional<Team> findByName(String name);

  // Find teams containing a specific player (by player ID)
  @Query("SELECT t FROM Team t JOIN t.players p WHERE p.id = :playerId")
  List<Team> findByPlayerId(Long playerId);

  // Teams with most players
  @Query("SELECT t FROM Team t ORDER BY SIZE(t.players) DESC")
  List<Team> findTeamsByMostPlayers();

  // Top N teams by player count
  @Query("SELECT t FROM Team t ORDER BY SIZE(t.players) DESC LIMIT :limit")
  List<Team> findTopTeamsByPlayerCount(int limit);

  // Find teams by player name
  @Query("SELECT t FROM Team t JOIN t.players p WHERE p.name = :playerName")
  List<Team> findByPlayerName(String playerName);

  // Teams with less than 5 players
  @Query("SELECT t FROM Team t WHERE SIZE(t.players) < 5")
  List<Team> findTeamsWithLessThan5Players();

  // Teams with most cards (sum of yellow and red)
  @Query(
      "SELECT t FROM Team t "
          + "JOIN t.players p "
          + "GROUP BY t.id "
          + "ORDER BY SUM(p.yellowCards + p.redCards) DESC")
  List<Team> findTeamsWithMostCards();

  // Teams with most wins
  @Query("SELECT t FROM Team t WHERE t.wins > 0 ORDER BY t.wins DESC")
  List<Team> findTeamsWithMostWins();

  // Filter by exact number of players
  @Query("SELECT t FROM Team t WHERE SIZE(t.players) = :count")
  List<Team> findByNumberOfPlayers(int count);

  // Filter by number of victories
  @Query("SELECT t FROM Team t WHERE t.wins = :wins")
  List<Team> findByWins(int wins);

  // Filter by number of draws
  @Query("SELECT t FROM Team t WHERE t.draws = :draws")
  List<Team> findByDraws(int draws);

  // Filter by number of losses
  @Query("SELECT t FROM Team t WHERE t.losses = :losses")
  List<Team> findByLosses(int losses);

  // Filter by number of titles
  @Query("SELECT t FROM Team t WHERE t.titles = :titles")
  List<Team> findByTitles(int titles);

  @Query(
      "SELECT t FROM Team t WHERE NOT EXISTS (SELECT p FROM t.players p WHERE p.preferredPosition ="
          + " :position)")
  List<Team> findTeamsMissingPosition(Position position);
}
