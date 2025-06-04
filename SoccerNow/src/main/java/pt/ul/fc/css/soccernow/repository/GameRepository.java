package pt.ul.fc.css.soccernow.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pt.ul.fc.css.soccernow.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {

  @Query("SELECT g FROM Game g WHERE g.homeTeam.name = :teamName OR g.awayTeam.name = :teamName")
  List<Game> findByTeamName(String teamName);

  @Query("SELECT g FROM Game g WHERE g.championshipId = :championshipId")
  List<Game> findByChampionshipId(Long championshipId);

  @Query("SELECT g FROM Game g WHERE g.championshipId = 0")
  List<Game> findStandaloneGames();

  @Query("SELECT g FROM Game g WHERE g.status = 'SCHEDULED'")
  List<Game> findScheduledGames();

  @Query("SELECT g FROM Game g WHERE g.status = 'SCHEDULED' AND g.championshipId = :championshipId")
  List<Game> findScheduledGamesByChampionship(Long championshipId);

  @Query("SELECT g FROM Game g WHERE g.status = 'COMPLETED'")
  List<Game> findCompletedGames();

  @Query("SELECT g FROM Game g WHERE g.status = 'CANCELLED'")
  List<Game> findCancelledGames();
}
