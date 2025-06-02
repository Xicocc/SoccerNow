package pt.ul.fc.css.soccernow.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

  // Basic CRUD operations are inherited from JpaRepository

  // Add @Query to explicitly check team names only
  @Query("SELECT COUNT(t) > 0 FROM Team t WHERE LOWER(t.name) = LOWER(:name)")
  boolean existsByName(String name);

  // Find all teams (alternative to findAll() if you need custom logic later)
  @Query("SELECT t FROM Team t")
  List<Team> findAllTeams();

  // Change findByName to return Optional<Team> for single result
  @Query("SELECT t FROM Team t WHERE t.name = :name")
  Optional<Team> findByName(String name);

  // Find teams containing a specific player (by player ID)
  @Query("SELECT t FROM Team t JOIN t.players p WHERE p.id = :playerId")
  List<Team> findByPlayerId(Long playerId);

  // Find teams with the most players (sorted by team size)
  @Query("SELECT t FROM Team t ORDER BY SIZE(t.players) DESC")
  List<Team> findTeamsByMostPlayers();

  // Find top N teams with the most players
  @Query("SELECT t FROM Team t ORDER BY SIZE(t.players) DESC LIMIT :limit")
  List<Team> findTopTeamsByPlayerCount(int limit);

  // Find teams where a player with a specific name is a member
  @Query("SELECT t FROM Team t JOIN t.players p WHERE p.name = :playerName")
  List<Team> findByPlayerName(String playerName);

  // Teams with less than 5 players
  @Query("SELECT t FROM Team t WHERE SIZE(t.players) < 5")
  List<Team> findTeamsWithLessThan5Players();

  // Teams that receive the most cards (yellow + red)
  @Query(
      "SELECT t FROM Team t "
          + "JOIN t.players p "
          + "GROUP BY t.id "
          + "ORDER BY SUM(p.yellowCards + p.redCards) DESC")
  List<Team> findTeamsWithMostCards();

  // Teams with most wins (using direct counter)
  @Query("SELECT t FROM Team t WHERE t.wins > 0 ORDER BY t.wins DESC")
  List<Team> findTeamsWithMostWins();
}
