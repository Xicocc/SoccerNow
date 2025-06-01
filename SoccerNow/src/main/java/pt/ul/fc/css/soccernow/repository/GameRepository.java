package pt.ul.fc.css.soccernow.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.model.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

  // Find games by exact team name (either home or away)
  @Query(
      "SELECT g FROM Game g WHERE LOWER(g.homeTeam.name) = LOWER(:teamName) OR"
          + " LOWER(g.awayTeam.name) = LOWER(:teamName)")
  List<Game> findByTeamName(String teamName);

  // Find game by ID (already provided by JpaRepository, but useful to customize)
  @Query("SELECT g FROM Game g WHERE g.id = :id")
  Optional<Game> findById(Long id);

  // Get all scheduled games
  @Query("SELECT g FROM Game g WHERE g.status = pt.ul.fc.css.soccernow.model.GameStatus.SCHEDULED")
  List<Game> findScheduledGames();

  // Get all completed games
  @Query("SELECT g FROM Game g WHERE g.status = pt.ul.fc.css.soccernow.model.GameStatus.COMPLETED")
  List<Game> findCompletedGames();

  // Get all cancelled games
  @Query("SELECT g FROM Game g WHERE g.status = pt.ul.fc.css.soccernow.model.GameStatus.CANCELLED")
  List<Game> findCancelledGames();
}
