package pt.ul.fc.css.soccernow.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.model.Game;

@Repository
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

  @Query("SELECT g FROM Game g WHERE g.status = 'COMPLETED'")
  List<Game> findPlayedGames();

  @Query("SELECT g FROM Game g WHERE g.status = 'SCHEDULED'")
  List<Game> findUpcomingGames();

  @Query("SELECT g FROM Game g WHERE (g.homeScore + g.awayScore) = :goalCount")
  List<Game> findByTotalGoals(int goalCount);

  @Query("SELECT g FROM Game g WHERE LOWER(g.location) LIKE LOWER(CONCAT('%', :location, '%'))")
  List<Game> findByLocation(String location);

  @Query("SELECT g FROM Game g WHERE FUNCTION('HOUR', g.gameTime) BETWEEN 6 AND 11")
  List<Game> findMorningGames();

  @Query("SELECT g FROM Game g WHERE FUNCTION('HOUR', g.gameTime) BETWEEN 12 AND 17")
  List<Game> findAfternoonGames();

  @Query("SELECT g FROM Game g WHERE FUNCTION('HOUR', g.gameTime) BETWEEN 18 AND 23")
  List<Game> findEveningGames();
}
