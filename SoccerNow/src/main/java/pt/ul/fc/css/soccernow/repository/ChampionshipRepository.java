package pt.ul.fc.css.soccernow.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.model.Championship;
import pt.ul.fc.css.soccernow.model.ChampionshipStatus;

@Repository
public interface ChampionshipRepository extends JpaRepository<Championship, Long> {

  // Find championship by exact name (case-insensitive)
  @Query("SELECT c FROM Championship c WHERE LOWER(c.name) = LOWER(:name)")
  Optional<Championship> findByNameIgnoreCase(String name);

  // Find championship by partial name (case-insensitive)
  @Query("SELECT c FROM Championship c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
  List<Championship> findByNameContainingIgnoreCase(String name);

  // Find championships by status
  @Query("SELECT c FROM Championship c WHERE c.status = :status")
  List<Championship> findByStatus(ChampionshipStatus status);

  // Find all championships that are planned
  @Query(
      "SELECT c FROM Championship c WHERE c.status ="
          + " pt.ul.fc.css.soccernow.model.ChampionshipStatus.PLANNED")
  List<Championship> findPlannedChampionships();

  // Find all ongoing championships
  @Query(
      "SELECT c FROM Championship c WHERE c.status ="
          + " pt.ul.fc.css.soccernow.model.ChampionshipStatus.ONGOING")
  List<Championship> findOngoingChampionships();

  // Find all completed championships
  @Query(
      "SELECT c FROM Championship c WHERE c.status ="
          + " pt.ul.fc.css.soccernow.model.ChampionshipStatus.COMPLETED")
  List<Championship> findCompletedChampionships();

  // Find all championships a team is participating in
  @Query("SELECT c FROM Championship c JOIN c.participatingTeams t WHERE t.id = :teamId")
  List<Championship> findByParticipatingTeamId(Long teamId);

  // Filter by team participation (alias to findByParticipatingTeamId)
  @Query("SELECT c FROM Championship c JOIN c.participatingTeams t WHERE t.id = :teamId")
  List<Championship> findByTeam(Long teamId);

  // Filter by number of games played (games with status FINISHED)
  @Query(
      "SELECT c FROM Championship c WHERE c.id IN ("
          + "SELECT g.championshipId FROM Game g WHERE g.status = 'FINISHED' "
          + "GROUP BY g.championshipId HAVING COUNT(g.id) = :played)")
  List<Championship> findByGamesPlayed(long played);

  // Filter by number of games to be played (not FINISHED)
  @Query(
      "SELECT c FROM Championship c WHERE c.id IN ("
          + "SELECT g.championshipId FROM Game g WHERE g.status != 'FINISHED' "
          + "GROUP BY g.championshipId HAVING COUNT(g.id) = :remaining)")
  List<Championship> findByGamesRemaining(long remaining);
}
