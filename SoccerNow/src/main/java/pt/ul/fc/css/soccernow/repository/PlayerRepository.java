package pt.ul.fc.css.soccernow.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.model.Player;
import pt.ul.fc.css.soccernow.model.Position;

@Repository
public interface PlayerRepository extends UserRepository {

  Player findByName(String name);

  @Query("SELECT p FROM Player p")
  List<Player> findAllPlayers();

  @Query("SELECT p FROM Player p WHERE p.preferredPosition = :position")
  List<Player> findByPosition(Position position);

  @Query(
      "SELECT AVG(p.goalsScored * 1.0 / p.gamesPlayed) "
          + "FROM Player p "
          + "WHERE p.name = :name AND p.gamesPlayed > 0")
  Double findAverageGoalsPerGameByName(String name);

  @Query("SELECT p FROM Player p ORDER BY p.redCards DESC")
  List<Player> findPlayersByMostRedCards();

  @Query("SELECT p FROM Player p ORDER BY p.redCards DESC LIMIT :limit")
  List<Player> findTopPlayersByRedCards(int limit);

  @Query("SELECT p FROM Player p WHERE p.goalsScored = :goals")
  List<Player> findByGoalsScored(int goals);

  @Query("SELECT p FROM Player p WHERE (p.yellowCards + p.redCards) = :cards")
  List<Player> findByTotalCards(int cards);

  @Query("SELECT p FROM Player p WHERE p.gamesPlayed = :games")
  List<Player> findByGamesPlayed(int games);
}
